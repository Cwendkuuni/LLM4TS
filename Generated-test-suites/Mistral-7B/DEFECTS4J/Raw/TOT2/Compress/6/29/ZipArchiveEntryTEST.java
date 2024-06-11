import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveEntryTest {

 @Test
 public void testCreateNewEntry() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 Assert.assertEquals("entry.txt", entry.getName());
 Assert.assertNull(entry.getMethod());
 Assert.assertEquals(0, entry.getInternalAttributes());
 Assert.assertEquals(0, entry.getExternalAttributes());
 Assert.assertNull(entry.getExtraFields());
 }

 @Test
 public void testCreateEntryFromZipEntry() throws ZipException {
 java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("entry.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
 Assert.assertEquals("entry.txt", entry.getName());
 Assert.assertNull(entry.getMethod());
 Assert.assertEquals(0, entry.getInternalAttributes());
 Assert.assertEquals(0, entry.getExternalAttributes());
 Assert.assertNull(entry.getExtraFields());
 }

 @Test
 public void testCreateEntryFromAnotherEntry() throws ZipException {
 ZipArchiveEntry source = new ZipArchiveEntry("entry.txt");
 ZipArchiveEntry destination = new ZipArchiveEntry(source);
 Assert.assertEquals(source.getName(), destination.getName());
 Assert.assertNotSame(source.getMethod(), destination.getMethod());
 Assert.assertNotSame(source.getInternalAttributes(), destination.getInternalAttributes());
 Assert.assertNotSame(source.getExternalAttributes(), destination.getExternalAttributes());
 Assert.assertNotSame(source.getExtraFields(), destination.getExtraFields());
 }

 @Test
 public void testSetMethod() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 entry.setMethod(ZipEntry.STORED);
 Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMethod() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 entry.setMethod(-1);
 }

 @Test
 public void testSetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 entry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_SPARSE);
 Assert.assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_SPARSE, entry.getInternalAttributes());
 }

 @Test
 public void testSetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 entry.setExternalAttributes(0x0100L);
 Assert.assertEquals(0x0100L, entry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 entry.setUnixMode(0755);
 Assert.assertEquals(0755, entry.getUnixMode());
 }

 @Test
 public void testGetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 entry.setUnixMode(0755);
 Assert.assertEquals(0755, entry.getUnixMode());
 }

 @Test
 public void testSetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 ZipExtraField field1 = new GenericZipLengthExtraField(ZipShort.UNIX_NAME_LENGTH, "name.txt".getBytes());
 ZipExtraField field2 = new CustomExtraField("custom", new byte[10]);
 entry.setExtraFields(new ZipExtraField[] { field1, field2 });
 Assert.assertNotNull(entry.getExtraFields());
 Assert.assertEquals(2, entry.getExtraFields().length);
 Assert.assertEquals("name.txt", field1.toString());
 Assert.assertEquals("custom", field2.getName());
 }

 @Test
 public void testGetExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 ZipExtraField field = new GenericZipLengthExtraField(ZipShort.UNIX_NAME_LENGTH, "name.txt".getBytes());
 entry.addExtraField(field);
 Assert.assertEquals(field, entry.getExtraField(ZipShort.UNIX_NAME_LENGTH));
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testGetNonExistentExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 Assert.assertNull(entry.getExtraField(ZipShort.UNIX_NAME_LENGTH));
 }

 @Test
 public void testSetAndGetLocalFileDataExtra() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 byte[] extraData = "test extra data".getBytes();
 entry.setExtra(extraData);
 byte[] localExtraData = entry.getLocalFileDataExtra();
 Assert.assertArrayEquals(extraData, localExtraData);
 }

 @Test
 public void testSetAndGetCentralDirectoryExtra() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 byte[] centralDirectoryExtraData = "test central directory extra data".getBytes();
 entry.setCentralDirectoryExtra(centralDirectoryExtraData);
 byte[] localExtraData = entry.getLocalFileDataExtra();
 Assert.assertNotSame(centralDirectoryExtraData, localExtraData);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidExtraData() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("entry.txt");
 byte[] invalidExtraData = new byte[] { (byte) 0xFF };
 entry.setExtra(invalidExtraData);
 }
}