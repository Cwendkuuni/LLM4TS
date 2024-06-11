import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testCreateNameEntry() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testCreateDirectoryEntry() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test/");
 assertTrue(entry.isDirectory());
 }

 @Test
 public void testCreateEntryWithFile() {
 File file = new File("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
 assertEquals("test.txt", entry.getName());
 assertEquals(file.length(), entry.getSize());
 assertEquals(file.lastModified(), entry.getTime());
 }

 @Test
 public void testCreateEntryWithDirectory() {
 File directory = new File("test");
 ZipArchiveEntry entry = new ZipArchiveEntry(directory, "test/");
 assertTrue(entry.isDirectory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("");
 entry.setMethod(-1);
 }

 @Test
 public void testSupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipArchiveEntry.DEFLATED);
 assertTrue(entry.isSupportedCompressionMethod());
 }

 @Test
 public void testUnsupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(0xFF);
 assertFalse(entry.isSupportedCompressionMethod());
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setInternalAttributes(ZipEntry.FLAG_HIDDEN);
 assertEquals(ZipEntry.FLAG_HIDDEN, entry.getInternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(0755);
 assertEquals(0755, entry.getUnixMode());
 }

 @Test
 public void testPlatform() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField field = new ZipExtendedTimestamp();
 entry.setExtraFields(new ZipExtraField[] {field});
 assertTrue(entry.getExtraFields().length > 0);
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField field = new ZipExtendedTimestamp();
 entry.addExtraField(field);
 assertTrue(entry.getExtraFields().length > 0);
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField field = new ZipExtendedTimestamp();
 entry.addExtraField(field);
 entry.removeExtraField(ZipShort.EXT_TIME);
 assertEquals(0, entry.getExtraFields().length);
 }

 @Test
 public void testParseExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = TestUtils.getResourceAsBytes("/test-extra.dat");
 entry.setExtra(extraData);
 ZipExtraField field = entry.getExtraField(ZipShort.EXT_TIME);
 assertNotNull(field);
 assertNotNull(field.getTimestamp());
 }

 @Test(expected = RuntimeException.class)
 public void testParseInvalidExtraFields() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = TestUtils.getResourceAsBytes("/test-invalid.dat");
 entry.setExtra(extraData);
 }
}