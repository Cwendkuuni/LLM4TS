import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

 @Test
 public void testGetNextZipEntry_normalCase() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 assertEquals("test.txt", entry.getName());
 }

 @Test
 public void testGetNextZipEntry_nullEntry() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream());
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 assertNull(entry);
 }

 @Test
 public void testGetNextZipEntry_closedStream() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 zipInputStream.close();
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 assertNull(entry);
 }

 @Test
 public void testGetNextZipEntry_readPastEndOfCentralDirectory() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream());
 for (int i = 0; i < 100; i++) {
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 assertNotNull(entry);
 }
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 assertNull(entry);
 }

 @Test
 public void testRead_normalCase() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 byte[] buffer = new byte[1024];
 int length = zipInputStream.read(buffer, 0, buffer.length);
 assertGreaterThan(length, 0);
 }

 @Test
 public void testRead_endOfStream() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 byte[] buffer = new byte[1024];
 int length = zipInputStream.read(buffer, 0, buffer.length);
 assertEquals(-1, length);
 }

 @Test
 public void testRead_invalidBufferSize() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 byte[] buffer = new byte[0];
 int length = zipInputStream.read(buffer, 0, buffer.length);
 assertEquals(-1, length);
 }

 @Test(expected = IOException.class)
 public void testRead_closedStream() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 zipInputStream.close();
 byte[] buffer = new byte[1024];
 int length = zipInputStream.read(buffer, 0, buffer.length);
 }

 @Test
 public void testCloseEntry_normalCase() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 zipInputStream.closeEntry();
 assertNull(zipInputStream.getNextZipEntry());
 }

 @Test
 public void testCloseEntry_closedStream() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 zipInputStream.close();
 zipInputStream.closeEntry();
 assertNull(zipInputStream.getNextZipEntry());
 }

 @Test
 public void testCloseEntry_noEntry() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream());
 zipInputStream.closeEntry();
 assertNull(zipInputStream.getNextZipEntry());
 }

 @Test
 public void testFill_normalCase() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream());
 zipInputStream.fill();
 }

 @Test(expected = IOException.class)
 public void testFill_closedStream() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream());
 zipInputStream.close();
 zipInputStream.fill();
 }

 @Test
 public void testReadFully() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 byte[] buffer = new byte[1024];
 zipInputStream.readFully(buffer);
 }

 @Test(expected = IOException.class)
 public void testReadFully_closedStream() throws Exception {
 ZipFile zipFile = new ZipFile("test.zip");
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream("test.txt"));
 zipInputStream.close();
 zipInputStream.readFully(new byte[1024]);
 }
}