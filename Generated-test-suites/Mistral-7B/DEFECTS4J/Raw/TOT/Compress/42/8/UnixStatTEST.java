import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 UnixStat unixStat = new UnixStat();
 int linkFlag = UnixStat.LINK_FLAG;
 int expected = 0120000;
 assertEquals(expected, linkFlag);
 }

 @Test
 public void testFileFlag() {
 UnixStat unixStat = new UnixStat();
 int fileFlag = UnixStat.FILE_FLAG;
 int expected = 0100000;
 assertEquals(expected, fileFlag);
 }

 @Test
 public void testDirFlag() {
 UnixStat unixStat = new UnixStat();
 int dirFlag = UnixStat.DIR_FLAG;
 int expected = 040000;
 assertEquals(expected, dirFlag);
 }

 @Test
 public void testDefaultLinkPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 int expected = 0777;
 assertEquals(expected, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 int expected = 0755;
 assertEquals(expected, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 UnixStat unixStat = new UnixStat();
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 int expected = 0644;
 assertEquals(expected, defaultFilePerm);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidMask() {
 UnixStat unixStat = new UnixStat();
 int invalidMask = 010000; // less than 3 digits
 UnixStat.PERM_MASK = invalidMask;
 try {
 int fileFlag = UnixStat.FILE_FLAG;
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testMaskWithValidValues() {
 UnixStat unixStat = new UnixStat();
 int validMask = 07777;
 UnixStat.PERM_MASK = validMask;
 int fileFlag = UnixStat.FILE_FLAG;
 int dirFlag = UnixStat.DIR_FLAG;
 int linkFlag = UnixStat.LINK_FLAG;
 int expectedFile = fileFlag | UnixStat.DEFAULT_FILE_PERM;
 int expectedDir = dirFlag | UnixStat.DEFAULT_DIR_PERM;
 int expectedLink = linkFlag | UnixStat.DEFAULT_LINK_PERM;
 int combined = UnixStat.PERM_MASK & (expectedFile | expectedDir | expectedLink);
 int actual = unixStat.PERM_MASK;
 assertEquals(combined, actual);
 }
}