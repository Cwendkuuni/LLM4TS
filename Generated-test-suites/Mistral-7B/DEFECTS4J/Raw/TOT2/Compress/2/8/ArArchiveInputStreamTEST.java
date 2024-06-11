import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 private InputStream inputStream;
 private ArArchiveInputStream arStream;

 @Before
 public void setUp() throws Exception {
 inputStream = ArchiveStreamFactory.createArchiveInputStream("testdata/test.tar.gz");
 arStream = new ArArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArArchiveEntry entry = arStream.getNextArEntry();
 assertNotNull(entry);
 assertEquals("testfile1.txt", entry.getName());
 long expectedSize = 12;
 assertEquals(expectedSize, entry.getSize());

 ArArchiveEntry nextEntry = arStream.getNextArEntry();
 assertNull(nextEntry);
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArArchiveEntry entry = arStream.getNextEntry();
 assertSame(entry, arStream.getNextArEntry());
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryAfterEOF() throws IOException {
 ArArchiveEntry entry = arStream.getNextEntry();
 arStream.getNextArEntry(); // consume next entry
 arStream.getNextArEntry(); // consume next entry
 assertNull(arStream.getNextEntry());
 }

 @Test
 public void testClose() throws IOException {
 ArArchiveEntry entry = arStream.getNextArEntry();
 assertTrue(arStream.isOpen());
 arStream.close();
 assertFalse(arStream.isOpen());
 }

 @Test(expected = IOException.class)
 public void testReadAfterClose() throws IOException {
 arStream.close();
 arStream.read();
 }

 @Test(expected = IOException.class)
 public void testReadBeforeOpen() throws IOException {
 arStream.read();
 }

 @Test
 public void testRead() throws IOException {
 byte[] buffer = new byte[10];
 int actual = arStream.read(buffer);
 assertNotNull(buffer);
 assertNotNull(buffer[0]);
 assertEquals(4, actual);
 IOUtils.closeQuietly(arStream);
 }

 @Test
 public void testReadLong() throws IOException {
 byte[] buffer = new byte[12];
 int actual = arStream.read(buffer, 0, buffer.length);
 assertNotNull(buffer);
 assertNotNull(buffer[0]);
 assertEquals(12, actual);
 IOUtils.closeQuietly(arStream);
 }

 @Test(expected = IOException.class)
 public void testReadNegativeLength() throws IOException {
 arStream.read(new byte[10], -1, 10);
 }

 @Test(expected = IOException.class)
 public void testReadNullBuffer() throws IOException {
 arStream.read(null, 0, 10);
 }

 @Test(expected = IOException.class)
 public void testReadNullOffset() throws IOException {
 arStream.read(new byte[10], -1, (short) -1);
 }

 @Test(expected = IOException.class)
 public void testReadNullLength() throws IOException {
 arStream.read(new byte[10], -1, (short) 0);
 }

 @Test
 public void testMatchesSignature() {
 byte[] signature = new byte[16];
 System.arraycopy(ArArchiveInputStream.class.getName().getBytes(), 0, signature, 0, 16);
 assertTrue(ArArchiveInputStream.matches(signature, signature.length));
 }
}