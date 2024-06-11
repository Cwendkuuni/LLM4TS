import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testConstructorWithName() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", zipEntry.getName());
 }

 @Test
 public void testConstructorWithZipEntry() {
 java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
 assertEquals("test.txt", zipArchiveEntry.getName());
 }

 @Test
 public void testConstructorWithZipArchiveEntry() {
 ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry();
 ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(zipArchiveEntry1);
 assertEquals(zipArchiveEntry1, zipArchiveEntry2);
 }

 @Test(expected = ZipException.class)
 public void testExceptionWhenSettingUnsupportedCompressionMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setMethod(42); // Unsupported compression method
 fail("ZipException should be thrown");
 }

 @Test
 public void testGettingAndSettingInternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setInternalAttributes(0xFFFF);
 assertEquals(0xFFFF, zipArchiveEntry.getInternalAttributes());
 }

 @Test
 public void testGettingAndSettingExternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setExternalAttributes(0xFFFFFFFFL);
 assertEquals(0xFFFFFFFFL, zipArchiveEntry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setUnixMode(0755); // rwxr-xr-x
 assertEquals(0755, zipArchiveEntry.getUnixMode());
 }

 @Test
 public void testGetUnixMode() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setUnixMode(0755); // rwxr-xr-x
 assertEquals(0755, zipArchiveEntry.getUnixMode());
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
 }

 @Test
 public void testSettingExtraFields() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipExtraField testExtraField = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, "Comment");
 zipArchiveEntry.addExtraField(testExtraField);
 assertEquals("Comment", zipArchiveEntry.getExtraField(ZipShort.LOCAL_FILE_COMMENT).getComment());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExceptionWhenSettingNegativeCompressionMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setMethod(-1); // Negative compression method
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testClone() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) zipArchiveEntry.clone();
 assertEquals(zipArchiveEntry.getName(), clonedZipArchiveEntry.getName());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
 }

 @Test
 public void testGetMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
 }
}