import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

 private TarArchiveOutputStream outStream;
 private ArchiveEntry entry;
 private File tempFile;

 @Before
 public void setUp() throws Exception {
 tempFile = File.createTempFile("test", null);
 outStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
 }

 @After
 public void tearDown() throws Exception {
 tempFile.delete();
 if (outStream != null) {
 outStream.close();
 }
 }

 @Test
 public void testConstructor() {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveOutputStream tarOutputStream2 = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")), 512);
 TarArchiveOutputStream tarOutputStream3 = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")), 512, 1024);
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test
 public void testFinish() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 tarOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testFinishAlreadyFinished() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 tarOutputStream.finish();
 tarOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testFinishWithoutPuttingAnEntry() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 tarOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testFinishWithUnclosedEntry() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testCloseWithoutFinishing() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.close();
 }

 @Test(expected = IOException.class)
 public void testCloseTwice() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.close();
 tarOutputStream.close();
 }

 @Test
 public void testGetRecordSize() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 int recordSize = tarOutputStream.getRecordSize();
 assertTrue(recordSize > 0);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithNullEntry() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveOutputStream.putArchiveEntry(tarOutputStream, null);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 ArchiveEntry archiveEntry = new File("test.txt").toPath().toFile().getAbsoluteFile();
 tarOutputStream.putArchiveEntry(archiveEntry);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithLongFileName() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 byte[] longFileNameBytes = "longFileName".getBytes();
 int fileNameLength = longFileNameBytes.length;
 if (tarOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_ERROR) {
 assertThrows(IOException.class,
 () -> tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.tar", longFileNameBytes, null, null, null, 0, fileNameLength)));
 } else if (tarOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_TRUNCATE) {
 TarArchiveEntry entry = new TarArchiveEntry("test.tar", longFileNameBytes, null, null, null, 0, fileNameLength);
 tarOutputStream.putArchiveEntry(entry);
 assertEquals(fileNameLength, outStream.getCount());
 } else if (tarOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_GNU) {
 TarArchiveEntry entry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME, longFileNameBytes, null, null, 0, fileNameLength + 1); // +1 for NUL
 tarOutputStream.putArchiveEntry(entry);
 TarArchiveEntry longLinkEntry = tarOutputStream.getNextEntry();
 assertNotNull(longLinkEntry);
 assertNull(longLinkEntry.getName());
 assertEquals(fileNameLength, longLinkEntry.getSize());
 }
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.write(TarConstants.MAGIC_LONGHEADER, 0, TarConstants.MAGIC_LONGHEADER.length);
 tarOutputStream.closeArchiveEntry();
 assertEquals(0, outStream.getCount());
 }

 @Test
 public void testWrite() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 int dataLength = 10;
 byte[] data = new byte[dataLength];
 for (int i = 0; i < dataLength; i++) {
 data[i] = (byte) i;
 }
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.write(data, 0, dataLength);
 tarOutputStream.closeArchiveEntry();
 assertEquals(dataLength + TarConstants.TAR_HEADER_SIZE, outStream.getCount());
 }

 @Test(expected = IOException.class)
 public void testWriteWithLongFileName() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 byte[] longFileNameBytes = "longFileName".getBytes();
 int fileNameLength = longFileNameBytes.length;
 TarArchiveEntry entry = new TarArchiveEntry("test.tar", longFileNameBytes, null, null, null, 0, fileNameLength);
 tarOutputStream.putArchiveEntry(entry);
 byte[] data = new byte[10];
 tarOutputStream.write(data, 0, data.length);
 tarOutputStream.closeArchiveEntry();
 if (tarOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_ERROR) {
 assertThrows(IOException.class, () -> tarOutputStream.write(data, 0, data.length));
 } else if (tarOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_TRUNCATE) {
 TarArchiveEntry entry2 = tarOutputStream.getNextEntry();
 assertNotNull(entry2);
 assertEquals(TarConstants.TAR_MAGIC, entry2.getHeader().getMagic());
 assertEquals(fileNameLength, entry2.getName().length());
 } else if (tarOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_GNU) {
 TarArchiveEntry entry2 = tarOutputStream.getNextEntry();
 assertNotNull(entry2);
 assertEquals(TarConstants.GNU_LONGLINK, entry2.getHeader().getMagic());
 assertEquals(fileNameLength + 1, entry2.getName().length());
 }
 }

 @Test(expected = IOException.class)
 public void testWriteWithUnclosedEntry() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.write(TarConstants.MAGIC_LONGHEADER, 0, TarConstants.MAGIC_LONGHEADER.length);
 tarOutputStream.write(new byte[]{"A", "B", "C"}, 0, 3);
 }

 @Test(expected = IOException.class)
 public void testWritePastSize() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 int dataLength = 10;
 byte[] data = new byte[dataLength];
 for (int i = 0; i < dataLength; i++) {
 data[i] = (byte) i;
 }
 tarOutputStream.write(data, 0, dataLength + 1);
 }

 @Test
 public void testWriteMultipleEntries() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("test.tar")));
 TarArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
 TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
 tarOutputStream.putArchiveEntry(entry1);
 tarOutputStream.write(TarConstants.MAGIC_LONGHEADER, 0, TarConstants.MAGIC_LONGHEADER.length);
 tarOutputStream.putArchiveEntry(entry2);
 tarOutputStream.write(TarConstants.MAGIC_LONGHEADER, 0, TarConstants.MAGIC_LONGHEADER.length);
 tarOutputStream.closeArchiveEntry();
 TarArchiveEntry entry3 = tarOutputStream.getNextEntry();
 assertEquals(entry1.getName(), entry3.getName());
 entry3 = tarOutputStream.getNextEntry();
 assertEquals(entry2.getName(), entry3.getName());
 assertNull(tarOutputStream.getNextEntry());
 }
}