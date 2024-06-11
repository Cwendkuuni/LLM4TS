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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

 private TarArchiveOutputStream tarOutputStream;
 private ByteArrayOutputStream outputStream;
 private byte[] expectedData = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r'};

 @Before
 public void setUp() throws IOException {
 outputStream = new ByteArrayOutputStream();
 tarOutputStream = new TarArchiveOutputStream(outputStream);
 }

 @After
 public void tearDown() throws IOException {
 tarOutputStream.close();
 }

 @Test
 public void constructorTest() {
 OutputStream outputStream = new ByteArrayOutputStream();
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 }

 @Test
 public void constructorWithBlockSizeTest() {
 OutputStream outputStream = new ByteArrayOutputStream();
 int blockSize = 5;
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, blockSize);
 assertEquals(blockSize, tarOutputStream.getBlockSize());
 }

 @Test
 public void constructorWithRecordSizeTest() {
 OutputStream outputStream = new ByteArrayOutputStream();
 int blockSize = 5;
 int recordSize = 10;
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, blockSize, recordSize);
 assertEquals(blockSize, tarOutputStream.getBlockSize());
 assertEquals(recordSize, tarOutputStream.getRecordSize());
 }

 @Test
 public void setLongFileModeTest() {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test
 public void putArchiveEntryTest() throws IOException {
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.finish();
 assertEquals(TarConstants.MAGIC_1000, outputStream.toByteArray()[0]);
 assertEquals(TarConstants.MAGIC_2000, outputStream.toByteArray()[1]);
 assertEquals(expectedData.length, outputStream.toByteArray().length);
 }

 @Test
 public void putArchiveEntryWithLongFileNameTest() throws IOException {
 String fileName = "longFileNameThatIsLongerThanNameLen.txt";
 TarArchiveEntry entry = new TarArchiveEntry(fileName);
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.finish();
 }

 @Test
 public void closeTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 tarOutputStream.write(expectedData, 0, expectedData.length);
 tarOutputStream.close();
 }

 @Test
 public void closeWithDataTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 tarOutputStream.write(expectedData, 0, expectedData.length);
 tarOutputStream.close();
 assertEquals(expectedData.length, outputStream.size());
 }

 @Test
 public void closeWithUnclosedEntryTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 tarOutputStream.write(expectedData, 0, expectedData.length);
 assertThrows(IOException.class, () -> tarOutputStream.close());
 }

 @Test
 public void closeTwiceTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 tarOutputStream.write(expectedData, 0, expectedData.length);
 tarOutputStream.close();
 tarOutputStream.close();
 }

 @Test
 public void writeTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 byte[] data = "test".getBytes();
 tarOutputStream.write(data, 0, data.length);
 tarOutputStream.finish();
 assertEquals("test".getBytes().length, outputStream.size());
 }

 @Test
 public void writeWithUnclosedEntryTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarOutputStream.putArchiveEntry(entry);
 byte[] data = "test".getBytes();
 tarOutputStream.write(data, 0, data.length);
 assertFalse(tarOutputStream.haveUnclosedEntry());
 tarOutputStream.close();
 }

 @Test
 public void writeEOFTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 tarOutputStream.writeEOFRecord();
 tarOutputStream.finish();
 assertEquals(2 * TarConstants.RECORD_LENGTH, outputStream.size());
 }

 @Test
 public void flushTest() throws IOException {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 byte[] data = "test".getBytes();
 tarOutputStream.write(data, 0, data.length);
 tarOutputStream.flush();
 assertEquals(data.length, outputStream.size());
 }

 @Test
 public void createArchiveEntryTest() throws IOException {
 File file = new File("test.txt");
 TarArchiveEntry entry = tarOutputStream.createArchiveEntry(file, "test.txt");
 assertNull(entry.getName());
 assertEquals(file.getAbsolutePath(), entry.getPathname());
 }

}