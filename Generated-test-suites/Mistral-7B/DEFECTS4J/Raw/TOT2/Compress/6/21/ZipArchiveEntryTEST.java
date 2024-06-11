import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testConstructorWithName() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testConstructorWithZipEntry() throws ZipException {
 ZipEntry zipEntry = new ZipEntry("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testConstructorWithZipArchiveEntry() throws ZipException {
 ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry copyEntry = (ZipArchiveEntry) originalEntry.clone();
 assertEquals(originalEntry, copyEntry);
 }

 @Test
 public void testConstructorWithFile() {
 File inputFile = new File("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipEntry.STORED);
 assertTrue(entry.isSupportedCompressionMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertTrue(entry.isSupportedCompressionMethod());
 }

 @Test
 public void testGetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, entry.getMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertEquals(ZipEntry.DEFLATED, entry.getMethod());
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(-1);
 assertNotSame(-1, entry.getMethod());
 entry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, entry.getMethod());
 entry.setMethod(ZipEntry.DEFLATED);
 assertEquals(ZipEntry.DEFLATED, entry.getMethod());
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(0, entry.getInternalAttributes());
 }

 @Test
 public void testSetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setInternalAttributes(0x01);
 assertEquals(0x01, entry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(0L, entry.getExternalAttributes());
 }

 @Test
 public void testSetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setExternalAttributes(0x01);
 assertEquals(0x01, entry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(0755);
 assertEquals(0755, entry.getUnixMode());
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
 entry.setUnixMode(0755);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField("TestExtraField");
 entry.addExtraField(extraField);
 assertNotNull(entry.getExtraField(extraField.getHeaderId()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddExtraFieldWithNegativeId() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtraField(-1);
 entry.addExtraField(extraField);
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testRemoveExtraFieldWithNonExistingId() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.removeExtraField(new ZipShort((short) 1));
 }

 @Test
 public void testMergeExtraFields() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 // Add some extra fields
 ZipExtraField extraField1 = new ZipExtraField("TestExtraField1");
 ZipExtraField extraField2 = new ZipExtraField("TestExtraField2");
 ZipExtraField extraField3 = new ZipExtraField("TestExtraField3");
 entry.addExtraField(extraField1);
 entry.addExtraField(extraField2);
 // Merge with another set of extra fields
 ZipExtraField extraField4 = new ZipExtraField("TestExtraField1");
 ZipExtraField extraField5 = new ZipExtraField("TestExtraField4");
 entry.mergeExtraFields(new ZipExtraField[] { extraField4, extraField5 });
 // Check that all extra fields have been merged
 ZipExtraField mergedField1 = entry.getExtraField(extraField1.getHeaderId());
 ZipExtraField mergedField2 = entry.getExtraField(extraField2.getHeaderId());
 ZipExtraField mergedField3 = entry.getExtraField(extraField3.getHeaderId());
 ZipExtraField mergedField4 = entry.getExtraField(extraField4.getHeaderId());
 ZipExtraField mergedField5 = entry.getExtraField(extraField5.getHeaderId());
 assertEquals(extraField1, mergedField1);
 assertEquals(extraField2, mergedField2);
 assertEquals(extraField3, mergedField3);
 assertEquals(extraField4, mergedField4);
 assertEquals(extraField5, mergedField5);
 }

 @Test
 public void testSetExtra() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = new byte[] { 0x00, 0x01, 0x02, 0x03 };
 entry.setExtra(extraData);
 assertEquals(extraData, entry.getLocalFileDataExtra());
 }

 @Test(expected = RuntimeException.class)
 public void testSetExtraWithInvalidData() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = new byte[] { 0x00, 0x01, 0xFF };
 entry.setExtra(extraData);
 }

 @Test
 public void testSetCentralDirectoryExtra() throws ZipException {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] centralDirectoryExtraData = new byte[] { 0x00, 0x01, 0x02, 0x03 };
 entry.setCentralDirectoryExtra(centralDirectoryExtraData);
 assertEquals(centralDirectoryExtraData, entry.getCentralDirectoryExtra());
 }
}