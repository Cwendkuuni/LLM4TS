import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 // Test constructor with valid arguments
 @Test
 public void testZipArchiveEntryConstructorWithValidArguments() {
 String name = "testfile.txt";
 ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
 assertEquals(name, zipEntry.getName());
 assertNull(zipEntry.getExtraFields());
 assertEquals(-1, zipEntry.getMethod());
 }

 // Test constructor with ZipEntry
 @Test
 public void testZipArchiveEntryConstructorWithZipEntry() throws ZipException {
 String name = "testfile.txt";
 java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
 assertEquals(name, zipArchiveEntry.getName());
 assertNull(zipArchiveEntry.getExtraFields());
 assertEquals(-1, zipArchiveEntry.getMethod());
 }

 // Test constructor with Cloneable
 @Test
 public void testZipArchiveEntryConstructorWithCloneable() throws ZipException {
 String name = "testfile.txt";
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
 ZipArchiveEntry clonedZipArchiveEntry = zipArchiveEntry.clone();
 assertEquals(name, clonedZipArchiveEntry.getName());
 assertNotSame(zipArchiveEntry, clonedZipArchiveEntry);
 }

 // Test constructor with File
 @Test
 public void testZipArchiveEntryConstructorWithFile() {
 File file = new File("testfile.txt");
 ZipArchiveEntry zipEntry = new ZipArchiveEntry(file, "testfile.txt");
 assertEquals("testfile.txt", zipEntry.getName());
 assertNotNull(zipEntry.getExtraFields());
 }

 // Test isSupportedCompressionMethod()
 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 zipEntry.setMethod(ZipEntry.STORED);
 assertTrue(zipEntry.isSupportedCompressionMethod());
 zipEntry.setMethod(ZipEntry.DEFLATED);
 assertTrue(zipEntry.isSupportedCompressionMethod());
 zipEntry.setMethod(123); // unsupported compression method
 assertFalse(zipEntry.isSupportedCompressionMethod());
 }

 // Test getMethod() and setMethod()
 @Test
 public void testGetMethodAndSetMethod() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 int method = ZipEntry.STORED;
 zipEntry.setMethod(method);
 assertEquals(method, zipEntry.getMethod());
 method = ZipEntry.DEFLATED;
 zipEntry.setMethod(method);
 assertEquals(method, zipEntry.getMethod());
 }

 // Test getInternalAttributes() and setInternalAttributes()
 @Test
 public void testGetInternalAttributesAndSetInternalAttributes() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 int internalAttributes = 0b0110_0111;
 zipEntry.setInternalAttributes(internalAttributes);
 assertEquals(internalAttributes, zipEntry.getInternalAttributes());
 }

 // Test getExternalAttributes() and setExternalAttributes()
 @Test
 public void testGetExternalAttributesAndSetExternalAttributes() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 long externalAttributes = 0b1111_1111_1111_1111;
 zipEntry.setExternalAttributes(externalAttributes);
 assertEquals(externalAttributes, zipEntry.getExternalAttributes());
 }

 // Test getPlatform() and setPlatform()
 @Test
 public void testGetPlatformAndSetPlatform() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 int platform = ZipArchiveEntry.PLATFORM_UNIX;
 zipEntry.setPlatform(platform);
 assertEquals(platform, zipEntry.getPlatform());
 }

 // Test setUnixMode()
 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 int mode = 0b0111_1111;
 zipEntry.setUnixMode(mode);
 assertEquals(mode, zipEntry.getUnixMode());
 }

 // Test getName()
 @Test
 public void testGetName() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 assertEquals("testfile.txt", zipEntry.getName());
 }

 // Test isDirectory()
 @Test
 public void testIsDirectory() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 assertFalse(zipEntry.isDirectory());
 zipEntry = new ZipArchiveEntry("testfile/");
 assertTrue(zipEntry.isDirectory());
 }

 // Test hashCode()
 @Test
 public void testHashCode() {
 ZipArchiveEntry zipEntry1 = new ZipArchiveEntry("testfile1.txt");
 ZipArchiveEntry zipEntry2 = new ZipArchiveEntry("testfile2.txt");
 int hashCode1 = zipEntry1.hashCode();
 int hashCode2 = zipEntry2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 zipEntry1.setName("testfile1.txt");
 assertEquals(hashCode1, zipEntry1.hashCode());
 }

 // Test equals()
 @Test
 public void testEquals() {
 ZipArchiveEntry zipEntry1 = new ZipArchiveEntry("testfile1.txt");
 ZipArchiveEntry zipEntry2 = new ZipArchiveEntry("testfile1.txt");
 ZipArchiveEntry zipEntry3 = new ZipArchiveEntry("testfile2.txt");
 assertTrue(zipEntry1.equals(zipEntry1));
 assertTrue(zipEntry1.equals(zipEntry2));
 assertFalse(zipEntry1.equals(zipEntry3));
 }

 // Test getLocalFileDataExtra()
 @Test
 public void testGetLocalFileDataExtra() throws ZipException {
 byte[] extraData = {0x01, 0x02, 0x03};
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 zipEntry.setExtra(extraData);
 byte[] localFileDataExtra = zipEntry.getLocalFileDataExtra();
 assertArrayEquals(extraData, localFileDataExtra);
 }

 // Test getCentralDirectoryExtra()
 @Test
 public void testGetCentralDirectoryExtra() throws ZipException {
 byte[] extraData = {0x01, 0x02, 0x03};
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("testfile.txt");
 zipEntry.setCentralDirectoryExtra(extraData);
 byte[] centralDirectoryExtra = zipEntry.getCentralDirectoryExtra();
 assertArrayEquals(extraData, centralDirectoryExtra);
 }
}