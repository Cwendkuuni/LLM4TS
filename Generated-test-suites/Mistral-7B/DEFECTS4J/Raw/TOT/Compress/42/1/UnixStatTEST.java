import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 UnixStat unixStat = new UnixStat();
 int linkPermission = unixStat.LINK_FLAG;
 assertTrue(linkPermission >= 0120000);
 }

 @Test
 public void testFileFlag() {
 UnixStat unixStat = new UnixStat();
 int filePermission = unixStat.FILE_FLAG;
 assertTrue(filePermission >= 0100000);
 }

 @Test
 public void testDirFlag() {
 UnixStat unixStat = new UnixStat();
 int dirPermission = unixStat.DIR_FLAG;
 assertTrue(dirPermission >= 040000);
 }

 @Test
 public void testDefaultLinkPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultLinkPerm = unixStat.DEFAULT_LINK_PERM;
 assertEquals(defaultLinkPerm, 0777);
 }

 @Test
 public void testDefaultDirPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultDirPerm = unixStat.DEFAULT_DIR_PERM;
 assertEquals(defaultDirPerm, 0755);
 }

 @Test
 public void testDefaultFilePerm() {
 UnixStat unixStat = new UnixStat();
 int defaultFilePerm = unixStat.DEFAULT_FILE_PERM;
 assertEquals(defaultFilePerm, 0644);
 }

 @Test(expected = IllegalStateException.class)
 public void testNegativePermissions() {
 UnixStat unixStat = new UnixStat();
 int invalidPermission = unixStat.PERM_MASK - 1;
 assertFalse(true); // must not reach this line
 }
}