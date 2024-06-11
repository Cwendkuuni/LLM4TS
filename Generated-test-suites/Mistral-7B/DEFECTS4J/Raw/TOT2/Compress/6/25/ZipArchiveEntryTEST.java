import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveEntryTest {

 private static final String TEST_FILE = "test.zip";

 @Test
 public void testConstructorWithName() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assert.assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testConstructorWithFileAndName() {
 File file = new File("test.txt");
 ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
 Assert.assertEquals("test.txt", entry.getName());
 Assert.assertEquals(file.length(), entry.getSize());
 }

 @Test
 public void testClone() throws Exception {
 ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
 ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
 Assert.assertEquals(original.getName(), clone.getName());
 Assert.assertEquals(original.getSize(), clone.getSize());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(ZipEntry.STORED);
 Assert.assertTrue(entry.isSupportedCompressionMethod());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMethodNegative() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setMethod(-1);
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assert.assertEquals(0, entry.getInternalAttributes());
 }

 @Test
 public void testSetAndGetInternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setInternalAttributes(0xFFFF);
 Assert.assertEquals(0xFFFF, entry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assert.assertEquals(0L, entry.getExternalAttributes());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setUnixMode(0755);
 Assert.assertEquals(0755, entry.getUnixMode());
 }

 @Test
 public void testSetPlatformUnix() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
 Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
 }

 @Test
 public void testSetAndGetExtraFields() throws Exception {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 ZipExtraField extraField = new ZipExtendedTimestampsV1();
 entry.addExtraField(extraField);
 Assert.assertNotNull(entry.getExtraField(extraField.getHeaderId()));
 }

 @Test
 public void testSetAndGetLocalFileDataExtra() throws Exception {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 byte[] extraData = new byte[10]; // arbitrary data
 entry.setExtra(extraData);
 byte[] retrievedExtraData = entry.getLocalFileDataExtra();
 Assert.assertArrayEquals(extraData, retrievedExtraData);
 }

 @Test
 public void testSetAndGetCentralDirectoryExtra() throws Exception {
 ZipFile zipFile = new ZipFile(TEST_FILE);
 ZipArchiveEntry entry = (ZipArchiveEntry) zipFile.getEntry("test.txt");
 byte[] extraData = new byte[10]; // arbitrary data
 entry.setCentralDirectoryExtra(extraData);
 byte[] retrievedExtraData = entry.getCentralDirectoryExtra();
 Assert.assertArrayEquals(extraData, retrievedExtraData);
 }

 @Test
 public void testGetName() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assert.assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testIsDirectory() {
 ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
 Assert.assertFalse(entry.isDirectory());
 }

 @Test
 public void testHashCode() {
 ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
 ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
 Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
 Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
 Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
 Assert.assertEquals(entry2.hashCode(), entry2.hashCode());
 Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());
 }

 @Test
 public void testEquals() {
 ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
 ZipArchiveEntry entry2 = new ZipArchiveEntry("test1.txt");
 ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
 Assert.assertTrue(entry1.equals(entry1));
 Assert.assertTrue(entry1.equals(entry2));
 Assert.assertFalse(entry1.equals(entry3));
 }
}