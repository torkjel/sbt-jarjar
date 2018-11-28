import sbtrelease._
import ReleasePlugin._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys

sbtPlugin := true

name := "sbt-jarjar"

organization := "com.tapad"

scalaVersion := "2.10.6"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo := {
  if (isSnapshot.value)
    Some("Tapad Nexus Snapshots" at "https://nexus.tapad.com/repository/snapshots")
  else
    Some("Tapad Nexus Releases" at "https://nexus.tapad.com/repository/releases")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://github.com/Tapad/sbt-jarjar</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://opensource.org/licenses/BSD-3-Clause</url>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:Tapad/sbt-jarjar.git</url>
    <connection>scm:git:git@github.com:Tapad/sbt-jarjar.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jeffo@tapad.com</id>
      <name>Jeffrey Olchovy</name>
      <url>http://github.com/jeffreyolchovy</url>
    </developer>
  </developers>
)

PgpKeys.useGpg := false

releaseSettings

ReleaseKeys.nextVersion := { (version: String) => Version(version).map(_.bumpBugfix.asSnapshot.string).getOrElse(versionFormatError) }

ReleaseKeys.releaseProcess := Seq(
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)

ScriptedPlugin.scriptedSettings

scriptedLaunchOpts ++= Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value)

scriptedBufferLog := false
