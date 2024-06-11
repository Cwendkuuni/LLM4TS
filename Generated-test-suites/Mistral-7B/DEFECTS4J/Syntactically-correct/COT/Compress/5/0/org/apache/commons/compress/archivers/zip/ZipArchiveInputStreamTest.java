import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

 @Test
 public void testConstructorWithInputStream() {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 System.in);
 assertNotNull(zipInputStream);
 }

 @Test
 public void testConstructorWithInputStreamEncodingUTF8AndUnicodeExtraFields() {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 System.in, "UTF-8", true);
 assertNotNull(zipInputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullInputStream() {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetNextZipEntry() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"));
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 assertNotNull(zipEntry);
 }

 @Test(expected = IOException.class)
 public void testGetNextZipEntryWhenReachedEndOfCentralDirectory() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"));
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 while (zipEntry != null) {
 zipEntry = zipInputStream.getNextZipEntry();
 }
 IOException ioException = assertThrows(IOException.class, () -> zipInputStream.getNextZipEntry());
 assertEquals("No more entries", ioException.getMessage());
 }

 @Test
 public void testGetNextEntry() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"));
 ArchiveEntry archiveEntry = zipInputStream.getNextEntry();
 assertNotNull(archiveEntry);
 assertEquals(ZipArchiveEntry.class, archiveEntry.getClass());
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryWhenReachedEndOfCentralDirectory() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"));
 ArchiveEntry archiveEntry = zipInputStream.getNextEntry();
 while (archiveEntry != null) {
 archiveEntry = zipInputStream.getNextEntry();
 }
 IOException ioException = assertThrows(IOException.class, () -> zipInputStream.getNextEntry());
 assertEquals("No more entries", ioException.getMessage());
 }

 @Test
 public void testReadWithStoredMethod() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 byte[] buffer = new byte[20];
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 int bytesRead = zipInputStream.read(buffer);
 assertNotEquals(-1, bytesRead);
 assertEquals(zipEntry.getSize(), zipInputStream.getBytesReadFromStream());
 }

 @Test
 public void testReadWithDeflatedMethod() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 byte[] buffer = new byte[20];
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 zipInputStream.closeEntry(); // close the current entry before reading
 zipInputStream.inf.reset(); // reset the inflater for each test case
 int bytesRead = zipInputStream.read(buffer);
 assertNotEquals(-1, bytesRead);
 assertEquals(zipEntry.getCompressedSize(), zipInputStream.getBytesReadFromStream());
 }

 @Test(expected = IOException.class)
 public void testReadWithNegativeLength() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 byte[] buffer = new byte[20];
 int bytesToRead = -10;
 int bytesRead = zipInputStream.read(buffer, 0, bytesToRead);
 fail("Expected IOException");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testReadWithInvalidBufferIndex() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 byte[] buffer = new byte[20];
 int bytesToRead = buffer.length;
 int bytesRead = zipInputStream.read(buffer, 20, bytesToRead);
 fail("Expected ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testClose() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 zipInputStream.close();
 assertEquals(0, zipInputStream.getBytesReadFromStream());
 }

 @Test
 public void testCloseEntry() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 zipInputStream.closeEntry();
 assertEquals(0, zipInputStream.getBytesReadFromStream());
 }

 @Test(expected = IOException.class)
 public void testCloseEntryWhenNotOpened() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 IOException ioException = assertThrows(IOException.class, () -> zipInputStream.closeEntry());
 assertEquals("Entry is null", ioException.getMessage());
 }

 @Test
 public void testGetBytesReadFromStream() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 zipInputStream.closeEntry();
 int bytesRead = zipInputStream.getBytesReadFromStream();
 assertEquals(0, bytesRead);
 }

 @Test
 public void testGetBytesReadFromStreamAfterReadingStoredEntry() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 int bytesRead = zipInputStream.read(new byte[10]);
 assertEquals(zipEntry.getSize(), zipInputStream.getBytesReadFromStream());
 }

 @Test
 public void testGetBytesReadFromStreamAfterReadingDeflatedEntry() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 ZipArchiveEntry zipEntry = zipInputStream.getNextZipEntry();
 zipInputStream.closeEntry();
 zipInputStream.inf.reset();
 int bytesRead = zipInputStream.read(new byte[10]);
 assertEquals(zipEntry.getCompressedSize(), zipInputStream.getBytesReadFromStream());
 }

 @Test
 public void testSkip() throws Exception {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(
 getClass().getResourceAsStream("/test_zip_file.zip"), "UTF-8", true);
 long value = Long.MAX_VALUE;
 long skipped = zipInputStream.skip(value);
 assertEquals(value, skipped);
 }

 @Test
 public void testMatchesWithValidSignature() {
 byte[] signature = new byte[] { 0x04, 0x03, 0x4b, 0x50, 0x0a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
 assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
 }

 @Test
 public void testMatchesWithInvalidSignatureLength() {
 byte[] signature = new byte[] { 0x04, 0x03, 0x4b, 0x50 };
 assertFalse(ZipArchiveInputStream.matches(signature, 5));
 }

 @Test
 public void testMatchesWithInvalidSignature() {
 byte[] signature = new byte[] { 0x04, 0x03, 0x4b, 0x51 }; // Invalid signature, different last byte
 assertFalse(ZipArchiveInputStream.matches(signature, 8));
 }

}