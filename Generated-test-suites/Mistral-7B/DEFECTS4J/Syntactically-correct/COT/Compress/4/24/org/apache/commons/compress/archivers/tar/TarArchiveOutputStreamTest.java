import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

 @Test
 public void testConstructor() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 }

 @Test
 public void testSetLongFileMode() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLongFileModeWithInvalidMode() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 tarArchiveOutputStream.setLongFileMode(-1);
 }

 @Test
 public void testFinish() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 tarArchiveOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testFinishWithUnclosedEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testClose() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }

 @Test(expected = IOException.class)
 public void testCloseTwice() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testGetRecordSize() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 assertEquals(TarConstants.RECORD_SIZE, tarArchiveOutputStream.getRecordSize());
 }

 @Test
 public void testPutArchiveEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test(expected = ClassCastException.class)
 public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 ArchiveEntry entry = new MyArchiveEntry();
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test(expected = IOException.class)
 public void testCloseArchiveEntryWithUnwrittenData() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testWrite() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] data = new byte[1024];
 tarArchiveOutputStream.write(data, 0, data.length);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteWithUnclosedEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] data = new byte[1024];
 tarArchiveOutputStream.write(data, 0, data.length);
 }

 @Test(expected = IOException.class)
 public void testWriteWithUnwrittenDataAndUnclosedEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] data = new byte[1024];
 tarArchiveOutputStream.write(data, 0, data.length);
 tarArchiveOutputStream.write(data, 0, data.length);
 }

 @Test(expected = IOException.class)
 public void testWriteWithTooMuchData() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] data = new byte[TarConstants.MAX_NAME_LENGTH + 1];
 tarArchiveOutputStream.write(data, 0, data.length);
 }

 @Test
 public void testWriteEOFRecord() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testFlush() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 tarArchiveOutputStream.flush();
 }

 @Test
 public void testCreateArchiveEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new MockOutputStream());
 File file = new File("test.txt");
 ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
 }
}

class MockOutputStream extends OutputStream {
 @Override
 public void write(int b) throws IOException {
 // empty implementation
 }

 @Override
 public void write(byte[] b, int off, int len) throws IOException {
 // empty implementation
 }

 @Override
 public void flush() throws IOException {
 // empty implementation
 }

 @Override
 public void close() throws IOException {
 // empty implementation
 }
}

class MyArchiveEntry implements ArchiveEntry {
 @Override
 public String getName() {
 return "";
 }

 @Override
 public long getSize() {
 return 0;
 }

 @Override
 public long getTimeCreated() {
 return 0;
 }

 @Override
 public long getTimeModified() {
 return 0;
 }

 @Override
 public void writeEntryHeader(byte[] header) throws IOException {
 // empty implementation
 }
}