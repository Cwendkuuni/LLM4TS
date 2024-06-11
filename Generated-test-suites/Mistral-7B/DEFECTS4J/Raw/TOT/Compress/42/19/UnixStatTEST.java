import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

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
 assertEquals("Default link permission should be set correctly", expectedDefaultLinkPerm, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 int expectedDefaultDirPerm = 0755;
 assertEquals("Default directory permission should be set correctly", expectedDefaultDirPerm, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 UnixStat unixStat = new UnixStat();
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 int expectedDefaultFilePerm = 0644;
 assertEquals("Default file permission should be set correctly", expectedDefaultFilePerm, defaultFilePerm);
 }

 @Test (expected = IllegalStateException.class)
 public void testInvalidMask() {
 UnixStat unixStat = new UnixStat();
 int invalidMask = UnixStat.PERM_MASK + 1;
 int expectedPermissions = 07777;
 int actualPermissions = unixStat.permissions(invalidMask);
 assertEquals("Invalid mask should return the same permissions as original", expectedPermissions, actualPermissions);
 // This test should throw an IllegalStateException as the mask is not valid
 unixStat.permissions(invalidMask | UnixStat.LINK_FLAG);
 }

 @Test
 public void testPermissionsWithLinkFlag() {
 UnixStat unixStat = new UnixStat();
 int permissionsWithLinkFlag = unixStat.permissions(UnixStat.PERM_MASK | UnixStat.LINK_FLAG);
 int expectedPermissions = UnixStat.DEFAULT_LINK_PERM;
 assertEquals("Permissions with link flag should be set correctly", expectedPermissions, permissionsWithLinkFlag);
 }

 @Test
 public void testPermissionsWithDirectoryFlag() {
 UnixStat unixStat = new UnixStat();
 int permissionsWithDirectoryFlag = unixStat.permissions(UnixStat.PERM_MASK | UnixStat.DIR_FLAG);
 int expectedPermissions = UnixStat.DEFAULT_DIR_PERM;
 assertEquals("Permissions with directory flag should be set correctly", expectedPermissions, permissionsWithDirectoryFlag);
 }

 @Test
 public void testPermissionsWithFileFlag() {
 UnixStat unixStat = new UnixStat();
 int permissionsWithFileFlag = unixStat.permissions(UnixStat.PERM_MASK | UnixStat.FILE_FLAG);
 int expectedPermissions = UnixStat.DEFAULT_FILE_PERM;
 assertEquals("Permissions with file flag should be set correctly", expectedPermissions, permissionsWithFileFlag);
 }

 @Test
 public void testPermissions() {
 UnixStat unixStat = new UnixStat();
 int permissions = unixStat.permissions(UnixStat.PERM_MASK);
 int expectedPermissions = UnixStat.DEFAULT_DIR_PERM | UnixStat.DEFAULT_FILE_PERM | UnixStat.DEFAULT_LINK_PERM;
 assertEquals("Permissions should be set correctly", expectedPermissions, permissions);
 }
}