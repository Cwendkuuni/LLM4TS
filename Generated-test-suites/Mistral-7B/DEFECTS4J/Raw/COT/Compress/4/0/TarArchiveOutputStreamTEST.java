import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

 private OutputStream outputStream;
 private TarArchiveOutputStream tarArchiveOutputStream;

 @Before
 public void setUp() throws Exception {
 outputStream = new MemoryOutputStream();
 tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 }

 @After
 public void tearDown() throws Exception {
 tarArchiveOutputStream.close();
 }

 @Test
 public void testConstructorWithOutputStream() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 }

 @Test
 public void testConstructorWithOutputStreamAndBlockSize() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 256);
 }

 @Test
 public void testConstructorWithOutputStreamAndBlockSizeAndRecordSize() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 256, 512);
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 }

 @Test
 public void testFinishWithoutCloseArchiveEntryThrowsException() throws IOException {
 tarArchiveOutputStream.finish();
 assertThrows(IOException.class, () -> tarArchiveOutputStream.finish());
 }

 @Test
 public void testFinishWithCloseArchiveEntry() throws IOException {
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testCloseWithOpenEntryThrowsException() throws IOException {
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 assertThrows(IOException.class, () -> tarArchiveOutputStream.close());
 }

 @Test
 public void testCloseWithClosedEntry() throws IOException {
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testGetRecordSize() throws IOException {
 assertEquals(512, tarArchiveOutputStream.getRecordSize());
 }

 @Test
 public void testPutArchiveEntryWithValidEntry() throws IOException {
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 }

 @Test(expected = ClassCastException.class)
 public void testPutArchiveEntryWithInvalidEntry() throws IOException {
 ArchiveEntry archiveEntry = new File("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 }

 @Test
 public void testCloseArchiveEntryWithUnclosedEntryThrowsException() throws IOException {
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
 }

 @Test
 public void testCloseArchiveEntryWithClosedEntry() throws IOException {
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteWithValidData() throws IOException {
 byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
 tarArchiveOutputStream.write(data, 0, data.length);
 }

 @Test
 public void testWriteWithInvalidOffset() throws IOException {
 byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
 assertThrows(IndexOutOfBoundsException.class, () -> tarArchiveOutputStream.write(data, 1, data.length));
 }

 @Test
 public void testWriteWithInvalidLength() throws IOException {
 byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
 assertThrows(IndexOutOfBoundsException.class, () -> tarArchiveOutputStream.write(data, 0, data.length + 1));
 }

 @Test
 public void testWriteWithValidDataLengthLessThanRecordSize() throws IOException {
 byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
 tarArchiveOutputStream.write(data, 0, data.length);
 }

 @Test
 public void testWriteWithValidDataLengthEqualToRecordSize() throws IOException {
 byte[] data = new byte[512];
 tarArchiveOutputStream.write(data, 0, data.length);
 }

 @Test
 public void testWriteWithValidDataLengthGreaterThanRecordSize() throws IOException {
 byte[] data = new byte[1024];
 tarArchiveOutputStream.write(data, 0, data.length);
 }

 @Test
 public void testWriteWithValidDataAndCloseArchiveEntry() throws IOException {
 byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 tarArchiveOutputStream.write(data, 0, data.length);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteWithValidDataAndFinish() throws IOException {
 byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
 ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 tarArchiveOutputStream.write(data, 0, data.length);
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testWriteEOFRecord() throws IOException {
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testFlush() throws IOException {
 tarArchiveOutputStream.flush();
 }

 @Test
 public void testCreateArchiveEntryWithValidFile() {
 File file = new File("test.txt");
 ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
 }

 @Test(expected = NullPointerException.class)
 public void testCreateArchiveEntryWithNullFile() {
 ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(null, "test.txt");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveEntryWithNullEntryName() {
 File file = new File("test.txt");
 ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, null);
 }
}

class MemoryOutputStream extends OutputStream {
 private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

 @Override
 public void write(int b) throws IOException {
 byteArrayOutputStream.write(b);
 }

 @Override
 public void write(byte[] b, int off, int len) throws IOException {
 byteArrayOutputStream.write(b, off, len);
 }

 @Override
 public void flush() throws IOException {
 byteArrayOutputStream.flush();
 }

 @Override
 public void close() throws IOException {
 byteArrayOutputStream.close();
 }
}