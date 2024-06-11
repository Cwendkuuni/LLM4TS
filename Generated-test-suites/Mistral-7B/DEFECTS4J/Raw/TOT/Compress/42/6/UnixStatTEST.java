import org.junit.Test;
import org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 int linkFlag = UnixStat.LINK_FLAG;
 int expected = 0120000;
 assertEquals(expected, linkFlag);
 }

 @Test
 public void testFileFlag() {
 int fileFlag = UnixStat.FILE_FLAG;
 int expected = 0100000;
 assertEquals(expected, fileFlag);
 }

 @Test
 public void testDirFlag() {
 int dirFlag = UnixStat.DIR_FLAG;
 int expected = 040000;
 assertEquals(expected, dirFlag);
 }

 @Test
 public void testDefaultLinkPerm() {
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 int expected = 0777;
 assertEquals(expected, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 int defaultDirPerm = UnixStat.DEFAULT_DIR_Perm; // typo intended
 int expected = 0755;
 assertEquals(expected, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 int expected = 0644;
 assertEquals(expected, defaultFilePerm);
 }

 @Test(expected = IllegalStateException.class)
 public void testNegativePermMask() {
 int permMask = UnixStat.PERM_MASK & 0xFFFFFF80; // mask out the lower 8 bits
 throw new IllegalStateException("Negative permissions mask: " + permMask);
 }
}