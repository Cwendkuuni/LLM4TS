import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

 private InputStream inputStream;
 private ArArchiveInputStream archiveInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = new ByteArrayInputStream(new byte[]{"\u001f\u003cAR\u0021\u0061 adm\u003e\u0000\u0001".getBytes()});
 archiveInputStream = new ArArchiveInputStream(inputStream);
 }

 @Test
 public void testGetNextArEntry() throws IOException {
 ArArchiveEntry entry = archiveInputStream.getNextArEntry();
 assertEquals("adm", entry.getName());
 assertEquals(1L, entry.getSize());
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWhenEndOfStreamReached() throws IOException {
 archiveInputStream.getNextArEntry();
 archiveInputStream.getNextArEntry(); // consume next entry
 assertNull(archiveInputStream.getNextArEntry());
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWhenInvalidEntryHeader() throws IOException {
 inputStream = new ByteArrayInputStream(new byte[]{"\u001f\u003cAR\u0021\u0061 a".getBytes()});
 archiveInputStream = new ArArchiveInputStream(inputStream);
 assertNull(archiveInputStream.getNextArEntry());
 }

 @Test(expected = IOException.class)
 public void testGetNextArEntryWhenInvalidSignature() throws IOException {
 inputStream = new ByteArrayInputStream(new byte[]{"\u001f\u003cAB\u0021\u0061 a".getBytes()});
 archiveInputStream = new ArArchiveInputStream(inputStream);
 assertNull(archiveInputStream.getNextArEntry());
 }

 @Test
 public void testGetNextEntry() throws IOException {
 ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
 assertEquals("adm", nextEntry.getName());
 assertEquals(1L, nextEntry.getSize());
 }

 @Test
 public void testClose() throws IOException {
 archiveInputStream.close();
 assertFalse(archiveInputStream.isOpen());
 }

 @Test
 public void testMatches() {
 byte[] signature = new byte[]{"\u001f", "\u003c", "AR", "\u0021", "\u0061", "ad", "\u003e", "\u0000", "\u0001"};
 assertTrue(ArArchiveInputStream.matches(signature, signature.length));
 }

 @Test
 public void testMatchesWhenLengthIsLessThanExpected() {
 byte[] signature = new byte[]{"\u001f", "\u003c", "AR", "\u0021", "\u0061", "a"};
 assertFalse(ArArchiveInputStream.matches(signature, signature.length));
 }

 @Test
 public void testMatchesWhenSignatureIsInvalid() {
 byte[] signature = new byte[]{"\u001f", "\u003c", "AB", "\u0021", "\u0061", "a"};
 assertFalse(ArArchiveInputStream.matches(signature, signature.length));
 }
}