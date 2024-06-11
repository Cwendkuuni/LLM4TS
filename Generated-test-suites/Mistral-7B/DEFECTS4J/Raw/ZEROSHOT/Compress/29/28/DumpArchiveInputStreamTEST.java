import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Test;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

 private static final String TEST_FILE = "testDumpArchive.bin";

 @Test
 public void testInit() {
 try (DumpArchiveInputStream is = new DumpArchiveInputStream(
 new java.io.FileInputStream(TEST_FILE), "UTF-8")) {
 // test encoding
 ZipEncoding encoding = is.getZipEncoding();
 assertEquals("UTF-8", encoding.getName());

 // test summary
 DumpArchiveSummary summary = is.getSummary();
 assertEquals(3, summary.getBlockSize());
 assertEquals(1, summary.getHeaderBlockCount());
 assertEquals(1, summary.getFileBlockCount());
 } catch (IOException e) {
 fail("Failed to open test file: " + e.getMessage());
 }
 }

 @Test(expected = ArchiveException.class)
 public void testBadInit() {
 try (DumpArchiveInputStream is = new DumpArchiveInputStream(
 new java.io.FileInputStream(TEST_FILE), null)) {
 fail("Should throw ArchiveException for null encoding");
 } catch (ArchiveException e) {
 // expected
 } catch (IOException e) {
 fail("Failed to open test file: " + e.getMessage());
 }
 }

 @Test
 public void testReadEntries() throws IOException {
 try (DumpArchiveInputStream is = new DumpArchiveInputStream(
 new java.io.FileInputStream(TEST_FILE), "UTF-8")) {
 DumpArchiveEntry entry = null;
 int idx = 0;

 // read all entries
 while ((entry = is.getNextEntry()) != null) {
 assertEquals(idx++, entry.getIndex());
 }

 // verify EOF
 assertEquals(0, is.getNextEntry().getIndex());
 }
 }

 @Test
 public void testReadEntriesTwice() throws IOException {
 try (DumpArchiveInputStream is = new DumpArchiveInputStream(
 new java.io.FileInputStream(TEST_FILE), "UTF-8")) {
 DumpArchiveEntry entry = null;
 int idx = 0;

 // read all entries
 while ((entry = is.getNextEntry()) != null) {
 assertEquals(idx++, entry.getIndex());
 }

 // read all entries again
 idx = 0;
 while ((entry = is.getNextEntry()) != null) {
 assertEquals(idx++, entry.getIndex());
 }
 }
 }

 @Test(expected = IOException.class)
 public void testNoMoreEntries() throws IOException {
 try (DumpArchiveInputStream is = new DumpArchiveInputStream(
 new java.io.FileInputStream(TEST_FILE), "UTF-8")) {
 DumpArchiveEntry entry = null;

 // read all entries
 while ((entry = is.getNextEntry()) != null) {
 // do nothing
 }

 // read next entry
 entry = is.getNextEntry();
 fail("Should throw IOException for no more entries");
 }
 }

 @Test
 public void testReadBytes() throws IOException {
 try (DumpArchiveInputStream is = new DumpArchiveInputStream(
 new java.io.FileInputStream(TEST_FILE), "UTF-8")) {
 DumpArchiveEntry entry = is.getNextEntry();
 assertTrue(entry.isDirectory());

 byte[] buf = new byte[1024];
 int read = is.read(buf, 0, buf.length);
 assertEquals(read, buf.length);
 }
 }

 @Test
 public void testReadBytesEOF() throws IOException {
 try (DumpArchiveInputStream is = new DumpArchiveInputStream(
 new java.io.FileInputStream(TEST_FILE), "UTF-8")) {
 DumpArchiveEntry entry = is.getNextEntry();
 assertTrue(entry.isDirectory());

 byte[] buf = new byte[1024];
 int read = is.read(buf, 0, buf.length);
 assertEquals(read, buf.length);

 is.close();
 entry = is.getNextEntry();
 assertEquals(0, is.read(buf, 0, buf.length));
 }
 }
}