import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit. Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 int linkStat = UnixStat.LINK_FLAG;
 assertTrue(linkStat >= UnixStat.PERM_MASK & UnixStat.LINK_FLAG == (linkStat & UnixStat.LINK_FLAG));
 }

 @Test
 public void testFileFlag() {
 int fileStat = UnixStat.FILE_FLAG;
 assertTrue(fileStat >= UnixStat.PERM_MASK & UnixStat.FILE_FLAG == (fileStat & UnixStat.FILE_FLAG));
 }

 @Test
 public void testDirFlag() {
 int dirStat = UnixStat.DIR_FLAG;
 assertTrue(dirStat >= UnixStat.PERM_MASK & UnixStat.DIR_FLAG == (dirStat & UnixStat.DIR_FLAG));
 }

 @Test
 public void testDefaultLinkPerm() {
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 assertEquals(defaultLinkPerm, 0777);
 }

 @Test
 public void testDefaultDirPerm() {
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 assertEquals(defaultDirPerm, 0755);
 }

 @Test
 public void testDefaultFilePerm() {
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 assertEquals(defaultFilePerm, 0644);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativePermMask() {
 int negativePermMask = UnixStat.PERM_MASK - 1;
 assertFalse(negativePermMask >= 0);
 }
}