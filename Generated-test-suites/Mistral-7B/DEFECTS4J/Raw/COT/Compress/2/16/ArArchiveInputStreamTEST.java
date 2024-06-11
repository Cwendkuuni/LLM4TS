import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 private InputStream input;
 private ArArchiveInputStream archiveInputStream;

 @Before
 public void setUp() throws Exception {
 input = new ByteArrayInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a, // Ar header
 (byte) 0x01, (byte) 'A', (byte) 'r', (byte) ' ', (byte) 'a', (byte) 'r', (byte) ' ', (byte) '1', (byte) '.', (byte) '0', (byte) '.', (byte) '1', (byte) '2', // Ar entry header
 (byte) 0x00, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x2e, (byte) 0x31, (byte) 0x32, (byte) 0x33, // file length
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // empty trailers
 (byte) 0x3c, (byte) 0x21, (byte) 0x72, (byte) 0x61, (byte) 0x68, (byte) 0x63, (byte) 0x0a, (byte) 0x3e, // Ar trailer
 (byte) -1}); // EOF
 archiveInputStream = new ArArchiveInputStream(input);
 }

 @Test
 public void testGetNextArEntry() throws Exception {
 ArchiveEntry entry = archiveInputStream.getNextArEntry();
 assertTrue(entry instanceof ArArchiveEntry);
 assertEquals("1.2.3", entry.getName());
 assertEquals(13, entry.getSize());
 entry = archiveInputStream.getNextArEntry();
 assertNull(entry);
 }

 @Test
 public void testGetNextEntry() throws Exception {
 ArchiveEntry entry = archiveInputStream.getNextEntry();
 assertTrue(entry instanceof ArArchiveEntry);
 assertEquals("1.2.3", entry.getName());
 assertEquals(13, entry.getSize());
 entry = archiveInputStream.getNextEntry();
 assertNull(entry);
 }

 @Test(expected = IOException.class)
 public void testGetNextEntryAfterClosed() throws Exception {
 archiveInputStream.close();
 archiveInputStream.getNextEntry();
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryBeforeHeader() throws Exception {
 input = new ByteArrayInputStream(new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, // not a valid Ar header
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // empty trailers
 (byte) 0x3c, (byte) 0x21, (byte) 0x72, (byte) 0x61, (byte) 0x68, (byte) 0x63, (byte) 0x0a, (byte) 0x3e, // Ar trailer
 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, // EOF with non-zero length
 (byte) 0x00});
 archiveInputStream = new ArArchiveInputStream(input);
 archiveInputStream.getNextArEntry();
 }

 @Test
 public void testMatches() throws Exception {
 byte[] signature = new byte[8];
 byte[] arHeader = {0x21, 0x3c, (byte) 'A', (byte) 'r', (byte) ' ', (byte) 'a', (byte) 'r', (byte) ' ', (byte) '1', (byte) '.', (byte) '0', (byte) '.', (byte) '1', (byte) '2', (byte) '3'};
 assertTrue(ArArchiveInputStream.matches(arHeader, arHeader.length));
 }

 @Test
 public void testMatchesWithInvalidLength() throws Exception {
 byte[] signature = new byte[8];
 byte[] arHeader = {0x21, 0x3c, (byte) 'A', (byte) 'r', (byte) ' ', (byte) 'a', (byte) 'r', (byte) ' ', (byte) '1', (byte) '.', (byte) '0', (byte) '.', (byte) '1', (byte) '2', (byte) '3'};
 assertFalse(ArArchiveInputStream.matches(signature, arHeader.length - 1));
 }
}