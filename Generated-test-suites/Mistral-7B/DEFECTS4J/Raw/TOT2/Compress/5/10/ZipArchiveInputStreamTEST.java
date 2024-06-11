import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

 @Test
 public void testGetNextZipEntry_whenCalledWithValidInputStream_returnsExpectedEntry() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
 assertNotNull(entry);
 }

 @Test(expected = IOException.class)
 public void testGetNextZipEntry_whenCalledWithClosedInputStream_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 zipInputStream.close();
 zipInputStream.getNextZipEntry();
 }

 @Test(expected = IOException.class)
 public void testGetNextZipEntry_whenCalledWithEmptyInputStream_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockEmptyInputStream());
 zipInputStream.getNextZipEntry();
 }

 @Test
 public void testGetNextZipEntry_whenCalledMultipleTimesWithValidInputStream_returnsExpectedEntries() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockMultiEntryInputStream());
 ZipArchiveEntry entry1 = zipInputStream.getNextZipEntry();
 ZipArchiveEntry entry2 = zipInputStream.getNextZipEntry();
 assertNotNull(entry1);
 assertNotNull(entry2);
 assertEquals("entry2.getName()", "entry2.getName()", entry2.getName());
 }

 @Test(expected = IOException.class)
 public void testGetNextZipEntry_whenCalledWithInvalidCentralDirectorySignature_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInvalidCentralDirectorySignatureInputStream());
 zipInputStream.getNextZipEntry();
 }

 @Test
 public void testRead_whenCalledWithValidData_readsExpectedData() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockReadDataInputStream());
 byte[] buffer = new byte[10];
 int bytesRead = zipInputStream.read(buffer);
 assertNotNull(bytesRead);
 assertEquals(5, bytesRead);
 }

 @Test(expected = IOException.class)
 public void testRead_whenCalledWithClosedInputStream_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 zipInputStream.close();
 zipInputStream.read(new byte[10]);
 }

 @Test(expected = IOException.class)
 public void testRead_whenCalledWithNullBuffer_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 zipInputStream.read(null);
 }

 @Test(expected = IOException.class)
 public void testRead_whenCalledWithNegativeCount_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 zipInputStream.read(new byte[10], -1, 10);
 }

 @Test(expected = IOException.class)
 public void testRead_whenCalledWithBufferSizeGreaterThanAvailableBytes_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockReadDataInputStream());
 byte[] buffer = new byte[20];
 int bytesRead = zipInputStream.read(buffer);
 assertEquals(10, bytesRead);
 }

 @Test(expected = EOFException.class)
 public void testRead_whenCalledWithEndOfStream_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockReadDataInputStream());
 byte[] buffer = new byte[10];
 int bytesRead = zipInputStream.read(buffer);
 assertEquals(10, bytesRead);
 bytesRead = zipInputStream.read(buffer);
 assertEquals(-1, bytesRead);
 }

 @Test(expected = IOException.class)
 public void testClose_whenCalledTwice_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 zipInputStream.close();
 zipInputStream.close();
 }

 @Test
 public void testSkip_whenCalledWithValidData_skipsExpectedData() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockSkipDataInputStream());
 zipInputStream.skip(10);
 }

 @Test(expected = IOException.class)
 public void testSkip_whenCalledWithNegativeValue_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 zipInputStream.skip(-1);
 }

 @Test(expected = IOException.class)
 public void testSkip_whenCalledWithLargeValueGreaterThanAvailableData_throwsException() throws IOException {
 ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new MockInputStream());
 zipInputStream.skip(Long.MAX_VALUE);
 }

 @Test(expected = IOException.class)
 public void testMatches_whenCalledWithInvalidSignature_throwsException() throws IOException {
 byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length + 1];
 assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
 }
}