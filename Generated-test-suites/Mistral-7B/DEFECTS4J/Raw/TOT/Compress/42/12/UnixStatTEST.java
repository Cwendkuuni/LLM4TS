import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 UnixStat unixStat = new UnixStat();
 int linkFlag = UnixStat.LINK_FLAG;
 int expectedLinkFlag = 0120000;
 assertEquals("Incorrect Link Flag", expectedLinkFlag, linkFlag);
 }

 @Test
 public void testFileFlag() {
 UnixStat unixStat = new UnixStat();
 int fileFlag = UnixStat.FILE_FLAG;
 int expectedFileFlag = 0100000;
 assertEquals("Incorrect File Flag", expectedFileFlag, fileFlag);
 }

 @Test
 public void testDirFlag() {
 UnixStat unixStat = new UnixStat();
 int dirFlag = UnixStat.DIR_FLAG;
 int expectedDirFlag = 040000;
 assertEquals("Incorrect Dir Flag", expectedDirFlag, dirFlag);
 }

 @Test
 public void testDefaultLinkPermissions() {
 UnixStat unixStat = new UnixStat();
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 int expectedDefaultLinkPerm = 0777;
 assertEquals("Incorrect Default Link Permissions", expectedDefaultLinkPerm, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPermissions() {
 UnixStat unixStat = new UnixStat();
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 int expectedDefaultDirPerm = 0755;
 assertEquals("Incorrect Default Dir Permissions", expectedDefaultDirPerm, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePermissions() {
 UnixStat unixStat = new UnixStat();
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 int expectedDefaultFilePerm = 0644;
 assertEquals("Incorrect Default File Permissions", expectedDefaultFilePerm, defaultFilePerm);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyingUnixStatConstant() {
 UnixStat unixStat = new UnixStat();
 int constantValue = UnixStat.PERM_MASK;
 unixStat.PERM_MASK = 12345; //should throw an UnsupportedOperationException
 }
}