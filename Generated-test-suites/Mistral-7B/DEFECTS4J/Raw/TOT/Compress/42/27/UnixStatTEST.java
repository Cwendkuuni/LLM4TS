import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 int expectedLinkFlag = UnixStat.LINK_FLAG;
 int actualLinkFlag = UnixStat.PERM_MASK & UnixStat.LINK_FLAG;
 assertEquals("Link flag is not correctly defined", expectedLinkFlag, actualLinkFlag);
 }

 @Test
 public void testDefaultPermissions() {
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;

 assertTrue("Default permissions for symbolic links should be greater than zero", defaultLinkPerm > 0);
 assertTrue("Default permissions for directories should be greater than zero", defaultDirPerm > 0);
 assertTrue("Default permissions for plain files should be greater than zero", defaultFilePerm > 0);

 int linkTest = defaultLinkPerm & UnixStat.PERM_MASK;
 int dirTest = defaultDirPerm & UnixStat.DIR_FLAG;
 int fileTest = defaultFilePerm & UnixStat.FILE_FLAG;

 assertEquals("Default permissions for symbolic links are not as expected", UnixStat.LINK_FLAG | defaultLinkPerm, linkTest);
 assertEquals("Default permissions for directories are not as expected", UnixStat.DIR_FLAG | defaultDirPerm, dirTest);
 assertEquals("Default permissions for plain files are not as expected", UnixStat.FILE_FLAG | defaultFilePerm, fileTest);
 }

 @Test(expected = IllegalStateException.class)
 public void testNegativePermissions() {
 int negativePermissions = -1;
 int expectedException = 0;
 try {
 int actual = negativePermissions & UnixStat.PERM_MASK;
 } catch (IllegalArgumentException e) {
 expectedException = 1;
 }

 assertEquals("An IllegalArgumentException should be thrown for negative permissions", expectedException, 1);
 }

 @Test
 public void testCheckMaskAndFlag() {
 int flag = UnixStat.FILE_FLAG | UnixStat.DIR_FLAG;
 int mask = UnixStat.PERM_MASK;
 int expected = flag & mask;
 int actual = ((UnixStat.FILE_FLAG & mask) | (UnixStat.DIR_FLAG & mask));
 assertEquals("Checking mask and flag with bitwise AND does not yield the expected result", expected, actual);
 }

 @Test
 public void testCheckMaskAndFlagWithLinkFlag() {
 int flag = UnixStat.LINK_FLAG | UnixStat.FILE_FLAG | UnixStat.DIR_FLAG;
 int mask = UnixStat.PERM_MASK;
 int expected = flag & mask;
 int actual = ((UnixStat.LINK_FLAG & mask) | (UnixStat.FILE_FLAG & mask) | (UnixStat.DIR_FLAG & mask));
 assertEquals("Checking mask and flag with bitwise AND does not yield the expected result for link flag", expected, actual);
 }
}