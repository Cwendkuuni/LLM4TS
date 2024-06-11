import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 private InputStream inputStream;
 private ArchiveInputStream archiveInputStream;
 private ArArchiveInputStream arArchiveInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = IOUtils.toInputStream("testData/test.tar.gz");
 archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(inputStream);
 arArchiveInputStream = (ArArchiveInputStream) archiveInputStream;
 }

 @After
 public void tearDown() throws Exception {
 if (arArchiveInputStream != null) {
 arArchiveInputStream.close();
 }
 if (archiveInputStream != null) {
 archiveInputStream.close();
 }
 if (inputStream != null) {
 inputStream.close();
 }
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
 assertNotNull(entry);
 assertEquals(1, arArchiveInputStream.getNextArEntry().getNumber(), 0);
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntry_throwsIOException_whenInputStreamHasNoData() throws IOException {
 arArchiveInputStream.close();
 ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
 fail("Should have thrown an IOException");
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntry_throwsIOException_whenInvalidHeader() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{(byte)0x21, (byte)0x3c, (byte)0x61, (byte)0x72, (byte)0x63, (byte)0x68, (byte)0x3e, (byte)0x0a, (byte)0x21, (byte)0x3c, (byte)0x61, (byte)0x72, (byte)0x63, (byte)0x68, (byte)0x3e, (byte)0x0a});
 ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
 ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
 fail("Should have thrown an IOException");
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArchiveEntry entry = arArchiveInputStream.getNextEntry();
 assertEquals(ArArchiveEntry.class, entry.getClass());
 assertEquals(1, ((ArArchiveEntry) entry).getNumber(), 0);
 }

 @Test(expected = IOException.class)
 public void testGetNextEntry_throwsIOException_whenInputStreamHasNoData() throws IOException {
 arArchiveInputStream.close();
 ArchiveEntry entry = arArchiveInputStream.getNextEntry();
 fail("Should have thrown an IOException");
 }

 @Test
 public void testClose() throws IOException {
 arArchiveInputStream.close();
 assertFalse(arArchiveInputStream.isOpen());
 }

 @Test
 public void testRead() throws IOException {
 int read = arArchiveInputStream.read();
 assertNotEquals(-1, read);
 }

 @Test
 public void testRead_returnZero_whenInputStreamHasNoData() throws IOException {
 arArchiveInputStream.close();
 int read = arArchiveInputStream.read();
 assertEquals(-1, read);
 }

 @Test
 public void testRead_returnExpectedData_whenInputStreamHasData() throws IOException {
 byte[] buffer = new byte[10];
 int length = arArchiveInputStream.read(buffer);
 assertNotEquals(-1, length);
 byte[] expected = "expectedData".getBytes();
 assertArrayEquals(expected, buffer, 0, expected.length);
 }

 @Test(expected = IOException.class)
 public void testRead_throwsIOException_whenInputStreamHasNoData() throws IOException {
 arArchiveInputStream.close();
 byte[] buffer = new byte[10];
 int length = arArchiveInputStream.read(buffer);
 fail("Should have thrown an IOException");
 }

 @Test
 public void testMatches() throws Exception {
 byte[] signature = new byte[16];
 byte[] data = "testArArchive signature".getBytes();
 System.arraycopy(data, 0, signature, 0, data.length);
 assertTrue(ArArchiveInputStream.matches(signature, data.length + 16));
 }

 @Test
 public void testMatches_returnsFalse_whenDataIsNotARArchiveSignature() throws Exception {
 byte[] signature = new byte[16];
 byte[] data = "testData".getBytes();
 System.arraycopy(data, 0, signature, 0, data.length);
 assertFalse(ArArchiveInputStream.matches(signature, data.length + 16));
 }
}