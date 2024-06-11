import org.junit.Test;
import org.apache.commons.compress.archivers.zip.UnixStat;

import static org.junit.Assert.assertEquals;

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

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyConstants() {
 UnixStat unixStat = new UnixStat() {
 @Override
 public int PERM_MASK() {
 throw new UnsupportedOperationException();
 }
 };
 UnixStat.PERM_MASK = 1; // this should not be possible
 }
}