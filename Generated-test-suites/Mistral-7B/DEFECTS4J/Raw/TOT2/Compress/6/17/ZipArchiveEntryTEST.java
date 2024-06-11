import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testDefaultConstructor() {
 ZipArchiveEntry entry = new ZipArchiveEntry("");
 assertEquals("", entry.getName());
 assertNull(entry.getExtraFields());
 assertEquals(0, entry.getMethod());
 assertEquals(0, entry.getInternalAttributes());
 assertEquals(0, entry.getExternalAttributes());
 }

 @Test
 public void testFileNameConstructor() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", entry.getName());
 assertNull(entry.getExtraFields());
 assertEquals(0, entry.getMethod());
 assertEquals(0, entry.getInternalAttributes());
 assertEquals(0, entry.getExternalAttributes());
 }

 @Test
 public void testZipEntryConstructor() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry(new ZipEntry("test.txt"));
 assertEquals("test.txt", entry.getName());
 assertNull(entry.getExtraFields());
 assertEquals(ZipEntry.STORED, entry.getMethod());
 assertEquals(0, entry.getInternalAttributes());
 assertEquals(0, entry.getExternalAttributes());
 }

 @Test
 public void testCloneableConstructor() throws ZipException {
 ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
 assertEquals("test.txt", clonedEntry.getName());
 assertArrayEquals(originalEntry.getExtraFields(), clonedEntry.getExtraFields());
 assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
 assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
 assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, entry.getMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertEquals(ZipEntry.DEFLATED, entry.getMethod());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMethodNegativeValue() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(-1);
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPlatformInvalidValue() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(4);
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(0755);
 assertEquals(0755, entry.getUnixMode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUnixModeNegativeValue() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(-1);
 }

 @Test
 public void testSetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField field = new ZipExtendedInformation();
 entry.setExtraFields(new ZipExtraField[]{field});
 assertEquals(1, entry.getExtraFields().length);
 }

 @Test
 public void testSetExtraFieldsEmptyArray() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setExtraFields(new ZipExtraField[0]);
 assertEquals(0, entry.getExtraFields().length);
 }

 @Test(expected = NullPointerException.class)
 public void testSetExtraFieldsNullArray() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setExtraFields(null);
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField field = new ZipExtendedInformation();
 entry.addExtraField(field);
 assertEquals(1, entry.getExtraFields().length);
 }

 @Test(expected = NullPointerException.class)
 public void testAddExtraFieldNullField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.addExtraField(null);
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField field = new ZipExtendedInformation();
 entry.addExtraField(field);
 entry.removeExtraField(ZipShort.UNIX_USER_ID);
 assertEquals(0, entry.getExtraFields().length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveExtraFieldInvalidType() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.addExtraField(new ZipExtendedInformation());
 entry.removeExtraField(ZipShort.COMMENT);
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testRemoveExtraFieldNonExistingField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.removeExtraField(ZipShort.UNIX_USER_ID);
 }

 @Test(expected = RuntimeException.class)
 public void testSetExtraInvalidData() {
 byte[] invalidData = new byte[10];
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 try {
 entry.setExtra(invalidData);
 } catch (RuntimeException e) {
 assertEquals("The extra data could not be parsed into extra fields.", e.getMessage());
 }
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertFalse(entry.isSupportedCompressionMethod());
 entry.setMethod(ZipEntry.STORED);
 assertTrue(entry.isSupportedCompressionMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertTrue(entry.isSupportedCompressionMethod());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsSupportedCompressionMethodUnsupportedMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(1);
 }

 @Test
 public void testToString() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals("ZipArchiveEntry{name='test.txt', method=-1, internalAttributes=0, platform=PLATFORM_FAT, externalAttributes=0, extraFields=<null>}", entry.toString());
 }

 @Test
 public void testGetName() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testIsDirectory() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertFalse(entry.isDirectory());
 entry.setName("test/");
 assertTrue(entry.isDirectory());
 }

 @Test
 public void testHashCode() {
 ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
 assertEquals(entry1.hashCode(), entry2.hashCode());
 entry1.setName("test2.txt");
 assertNotSame(entry1.hashCode(), entry2.hashCode());
 }

 @Test
 public void testEquals() {
 ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
 assertTrue(entry1.equals(entry2));
 entry1.setName("test2.txt");
 assertFalse(entry1.equals(entry2));
 entry1.setName("test.txt");
 ZipArchiveEntry clone = (ZipArchiveEntry) entry1.clone();
 assertTrue(entry1.equals(clone));
 }

 @Test
 public void testGetLocalFileDataExtra() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = new byte[10];
 entry.setExtra(extraData);
 byte[] localExtraData = entry.getLocalFileDataExtra();
 assertArrayEquals(extraData, localExtraData);
 }

 @Test
 public void testGetCentralDirectoryExtra() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] centralDirectoryExtraData = new byte[10];
 entry.setCentralDirectoryExtra(centralDirectoryExtraData);
 byte[] centralExtraData = entry.getCentralDirectoryExtra();
 assertArrayEquals(centralDirectoryExtraData, centralExtraData);
 }

}