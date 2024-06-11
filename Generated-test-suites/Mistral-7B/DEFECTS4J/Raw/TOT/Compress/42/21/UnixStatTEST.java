import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 UnixStat unixStat = new UnixStat();
 int linkFlag = UnixStat.LINK_FLAG;
 int expectedLinkFlag = 0120000;
 assertEquals(expectedLinkFlag, linkFlag);
 }

 @Test
 public void testFileFlag() {
 UnixStat unixStat = new UnixStat();
 int fileFlag = UnixStat.FILE_FLAG;
 int expectedFileFlag = 0100000;
 assertEquals(expectedFileFlag, fileFlag);
 }

 @Test
 public void testDirFlag() {
 UnixStat unixStat = new UnixStat();
 int dirFlag = UnixStat.DIR_FLAG;
 int expectedDirFlag = 040000;
 assertEquals(expectedDirFlag, dirFlag);
 }

 @Test
 public void testDefaultLinkPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 int expectedDefaultLinkPerm = 0777;
 assertEquals(expectedDefaultLinkPerm, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 int expectedDefaultDirPerm = 0755;
 assertEquals(expectedDefaultDirPerm, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 UnixStat unixStat = new UnixStat();
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 int expectedDefaultFilePerm = 0644;
 assertEquals(expectedDefaultFilePerm, defaultFilePerm);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyConstants() {
 UnixStat unixStat = new UnixStat() {
 @Override
 public int PERM_MASK() {
 throw new UnsupportedOperationException("Constants are final");
 }
 };
 UnixStat.PERM_MASK = 0;
 fail("Should throw UnsupportedOperationException");
 }
}