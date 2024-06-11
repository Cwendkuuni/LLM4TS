import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

 private byte[] getTestData() {
 return new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7 };
 }

 @Test
 public void testConstructor() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 });
 assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullOutputStream() {
 new TarArchiveOutputStream(null);
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 });
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test(expected = IllegalArgumentArgumentException.class)
 public void testSetLongFileModeInvalidMode() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 });
 tarArchiveOutputStream.setLongFileMode(4);
 }

 @Test
 public void testFinish() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.finish();
 }

 @Test(expected = IllegalStateException.class)
 public void testFinishAlreadyFinished() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.finish();
 }

 @Test(expected = IllegalStateException.class)
 public void testFinishNoEntries() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testClose() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.close();
 }

 @Test
 public void testCloseTwice() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.close();
 tarArchiveOutputStream.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testCloseAlreadyClosed() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.close();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testGetRecordSize() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 assertEquals(512, tarArchiveOutputStream.getRecordSize());
 }

 @Test
 public void testPutArchiveEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutArchiveEntryNullEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.putArchiveEntry(null);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryInvalidEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.putArchiveEntry(new Object());
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryInvalidEntryNameLength() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("".getBytes(0, 1024));
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testPutArchiveEntryValidEntryName() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test(expected = IllegalStateException.class)
 public void testCloseArchiveEntryNotOpened() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWrite() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] testData = getTestData();
 tarArchiveOutputStream.write(testData, 0, testData.length);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test(expected = IllegalStateException.class)
 public void testWriteBeforeEntryClosed() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 byte[] testData = getTestData();
 tarArchiveOutputStream.write(testData, 0, testData.length);
 }

 @Test
 public void testWriteEmpty() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(0);
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write(new byte[0], 0, 0);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWritePartial() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] testData = getTestData();
 int numToWrite = testData.length / 2;
 tarArchiveOutputStream.write(testData, 0, numToWrite);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteFull() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] testData = getTestData();
 tarArchiveOutputStream.write(testData, 0, testData.length);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteTwoEntries() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry1 = new TarArchiveEntry("test1.txt");
 entry1.setSize(5);
 TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
 entry2.setSize(5);
 tarArchiveOutputStream.putArchiveEntry(entry1);
 tarArchiveOutputStream.write(new byte[5], 0, 5);
 tarArchiveOutputStream.putArchiveEntry(entry2);
 tarArchiveOutputStream.write(new byte[5], 0, 5);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test
 public void testFinishAndClose() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] testData = getTestData();
 tarArchiveOutputStream.write(testData, 0, testData.length);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testFinishAndCloseTwice() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new OutputStream() {
 @Override
 public void write(int b) throws IOException {}

 @Override
 public void close() throws IOException {}
 }, 1024, 512);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 entry.setSize(10);
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] testData = getTestData();
 tarArchiveOutputStream.write(testData, 0, testData.length);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 }
}