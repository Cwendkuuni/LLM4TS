import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

 @Test
 public void testConstructor() {
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out));
 }

 @Test
 public void testConstructorWithBlockSizeAndRecordSize() {
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 }

 @Test
 public void testSetLongFileMode() throws IOException {
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out));
 tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test
 public void testFinish() throws IOException {
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out));
 tarOutput.finish();
 assertTrue(tarOutput.isFinished());
 }

 @Test
 public void testClose() throws IOException {
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out));
 tarOutput.close();
 assertTrue(tarOutput.isClosed());
 }

 @Test
 public void testPutArchiveEntryWithLongFileName() throws IOException {
 byte[] longFileNameBytes = "longFileName".getBytes();
 TarArchiveEntry entry = new TarArchiveEntry("longFileName");
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 tarOutput.putArchiveEntry(entry);
 byte[] expectedLongFileNameBytes = new byte[longFileNameBytes.length + 1]; // +1 for NUL
 System.arraycopy(longFileNameBytes, 0, expectedLongFileNameBytes, 0, longFileNameBytes.length);
 tarOutput.write(expectedLongFileNameBytes);
 tarOutput.write((byte) 0); // NUL terminator
 tarOutput.closeArchiveEntry();
 }

 @Test
 public void testPutArchiveEntryWithShortFileName() throws IOException {
 TarArchiveEntry entry = new TarArchiveEntry("shortFileName");
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 tarOutput.putArchiveEntry(entry);
 }

 @Test
 public void testPutArchiveEntryWithLongerFileNameThanNameLength() {
 TarArchiveEntry entry = new TarArchiveEntry("longerFileNameThanNameLength");
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 assertThrows(RuntimeException.class, () -> tarOutput.putArchiveEntry(entry));
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 byte[] testData = "testData".getBytes();
 TarArchiveEntry entry = new TarArchiveEntry("testFileName");
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 tarOutput.putArchiveEntry(entry);
 tarOutput.write(testData);
 tarOutput.closeArchiveEntry();
 }

 @Test
 public void testWrite() throws IOException {
 byte[] testData = "testData".getBytes();
 byte[] expectedTestData = new byte[testData.length];
 System.arraycopy(testData, 0, expectedTestData, 0, testData.length);
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 TarArchiveEntry entry = new TarArchiveEntry("testFileName");
 tarOutput.putArchiveEntry(entry);
 tarOutput.write(testData);
 byte[] actualTestData = tarOutput.buf.getBuffer();
 System.arraycopy(actualTestData, tarOutput.pos, expectedTestData, 0, testData.length);
 assertArrayEquals(expectedTestData, actualTestData);
 }

 @Test
 public void testFinishAndClose() throws IOException {
 byte[] testData = "testData".getBytes();
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 TarArchiveEntry entry = new TarArchiveEntry("testFileName");
 tarOutput.putArchiveEntry(entry);
 tarOutput.write(testData);
 tarOutput.finish();
 tarOutput.close();
 }

 @Test
 public void testFinishTwice() {
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out));
 tarOutput.finish();
 tarOutput.finish();
 assertThrows(IOException.class, () -> tarOutput.finish());
 }

 @Test
 public void testCloseTwice() {
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out));
 tarOutput.close();
 tarOutput.close();
 assertThrows(IOException.class, () -> tarOutput.close());
 }

 @Test
 public void testCreateArchiveEntryWithNullFile() {
 assertNull(new TarArchiveOutputStream(new CountingOutputStream(System.out)).createArchiveEntry(null, ""));
 }

 @Test
 public void testCreateArchiveEntryWithNullEntryName() {
 TarArchiveEntry entry = new TarArchiveEntry(new File("inputFile.txt"));
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 assertThrows(IOException.class, () -> tarOutput.putArchiveEntry(null));
 }

 @Test
 public void testCreateArchiveEntryWithNullFileName() {
 TarArchiveEntry entry = new TarArchiveEntry("entryName");
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 assertThrows(IOException.class, () -> tarOutput.putArchiveEntry(entry, null));
 }

 @Test
 public void testCloseWithUnclosedEntry() throws IOException {
 TarArchiveEntry entry = new TarArchiveEntry("testFileName");
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 tarOutput.putArchiveEntry(entry);
 tarOutput.finish();
 assertFalse(tarOutput.haveUnclosedEntry);
 }

 @Test
 public void testCloseWithoutFinish() throws IOException {
 TarArchiveEntry entry = new TarArchiveEntry("testFileName");
 TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new CountingOutputStream(System.out), 512, 512);
 tarOutput.putArchiveEntry(entry);
 assertThrows(IOException.class, () -> tarOutput.close());
 }
}