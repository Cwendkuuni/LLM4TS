import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testZipArchiveEntryConstructorWithName() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", zipArchiveEntry.getName());
 }

 @Test
 public void testZipArchiveEntryConstructorWithZipEntry() throws ZipException {
 java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
 assertEquals("test.txt", zipArchiveEntry.getName());
 }

 @Test
 public void testZipArchiveEntryConstructorWithZipArchiveEntry() throws ZipException {
 ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry clonedZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);
 assertEquals(originalZipArchiveEntry.getName(), clonedZipArchiveEntry.getName());
 }

 @Test
 public void testZipArchiveEntryConstructorWithFile() {
 File file = new File("test.txt");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
 assertEquals("test.txt", zipArchiveEntry.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testZipArchiveEntryConstructorWithNegativeMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
 fail("IllegalArgumentException expected");
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

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 assertEquals(0, zipArchiveEntry.getInternalAttributes());
 }

 @Test
 public void testSetInternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setInternalAttributes(File.FS_TASK);
 assertEquals(File.FS_TASK, zipArchiveEntry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 }

 @Test
 public void testSetExternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setExternalAttributes(File.ACCESS_READ);
 assertEquals(File.ACCESS_READ, zipArchiveEntry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setUnixMode(0644);
 assertEquals(0644, zipArchiveEntry.getUnixMode());
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
 public void testSetExtraFields() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipCustomField();
 zipArchiveEntry.setExtraFields(new ZipExtraField[] {extraField});
 assertEquals(1, zipArchiveEntry.getExtraFields().length);
 }

 @Test
 public void testGetExtraFields() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
 assertNull(extraFields);
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipCustomField();
 zipArchiveEntry.addExtraField(extraField);
 assertEquals(1, zipArchiveEntry.getExtraFields().length);
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testRemoveExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipShort type = new ZipShort((short) 0);
 zipArchiveEntry.removeExtraField(type);
 fail("java.util.NoSuchElementException expected");
 }

 @Test
 public void testGetExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 ZipCustomField extraField = new ZipCustomField();
 zipArchiveEntry.addExtraField(extraField);
 ZipExtraField retrievedExtraField = zipArchiveEntry.getExtraField(extraField.getHeaderId());
 assertEquals(extraField, retrievedExtraField);
 }

 @Test
 public void testSetExtra() throws RuntimeException {
 byte[] extraData = new byte[10];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setExtra(extraData);
 }

 @Test(expected = RuntimeException.class)
 public void testSetExtraWithInvalidData() {
 byte[] extraData = new byte[1];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 try {
 zipArchiveEntry.setExtra(extraData);
 fail("RuntimeException expected");
 } catch (RuntimeException e) {
 }
 }

 @Test
 public void testGetLocalFileDataExtra() {
 byte[] extraData = new byte[10];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setExtra(extraData);
 byte[] retrievedExtraData = zipArchiveEntry.getLocalFileDataExtra();
 assertArrayEquals(extraData, retrievedExtraData);
 }

 @Test
 public void testGetCentralDirectoryExtra() {
 byte[] extraData = new byte[10];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
 zipArchiveEntry.setCentralDirectoryExtra(extraData);
 byte[] retrievedExtraData = zipArchiveEntry.getCentralDirectoryExtra();
 assertArrayEquals(extraData, retrievedExtraData);
 }
}