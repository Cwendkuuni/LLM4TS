import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 UnixStat unixStat = new UnixStat();
 int linkFlag = UnixStat.LINK_FLAG;
 int expectedLinkFlag = 0120000;
 assertEquals("Link flag should be set correctly", expectedLinkFlag, linkFlag);
 }

 @Test
 public void testFileFlag() {
 UnixStat unixStat = new UnixStat();
 int fileFlag = UnixStat.FILE_FLAG;
 int expectedFileFlag = 0100000;
 assertEquals("File flag should be set correctly", expectedFileFlag, fileFlag);
 }

 @Test
 public void testDirFlag() {
 UnixStat unixStat = new UnixStat();
 int dirFlag = UnixStat.DIR_FLAG;
 int expectedDirFlag = 040000;
 assertEquals("Directory flag should be set correctly", expectedDirFlag, dirFlag);
 }

 @Test
 public void testDefaultLinkPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 int expectedDefaultLinkPerm = 0777;
 assertEquals("Default link permissions should be set correctly", expectedDefaultLinkPerm, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 int expectedDefaultDirPerm = 0755;
 assertEquals("Default directory permissions should be set correctly", expectedDefaultDirPerm, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 UnixStat unixStat = new UnixStat();
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 int expectedDefaultFilePerm = 0644;
 assertEquals("Default file permissions should be set correctly", expectedDefaultFilePerm, defaultFilePerm);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyingUnchangeableConstants() {
 UnixStat unixStat = new UnixStat() {
 @Override
 public int PERM_MASK() {
 throw new UnsupportedOperationException("Constants are read-only");
 }
 };
 UnixStat.PERM_MASK = 0; // This should throw an exception
 }
}