import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

 private OutputStream outputStream;
 private TarArchiveOutputStream tarArchiveOutputStream;

 @Before
 public void setUp() throws Exception {
 outputStream = new ByteArrayOutputStream();
 tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
 }

 @After
 public void tearDown() throws Exception {
 tarArchiveOutputStream.close();
 }

 /**
 * Test constructor with valid OutputStream.
 */
 @Test
 public void testConstructorWithValidOutputStream() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
 assertEquals(512, tarArchiveOutputStream.getRecordSize());
 }

 /**
 * Test constructor with null OutputStream.
 */
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullOutputStream() {
 new TarArchiveOutputStream(null, 512, 512);
 }

 /**
 * Test setLongFileMode with valid mode.
 */
 @Test
 public void testSetLongFileModeWithValidMode() {
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 }

 /**
 * Test setLongFileMode with invalid mode.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetLongFileModeWithInvalidMode() {
 tarArchiveOutputStream.setLongFileMode(-1);
 }

 /**
 * Test finish() method with no exceptions thrown.
 */
 @Test
 public void testFinishWithNoExceptions() throws IOException {
 tarArchiveOutputStream.finish();
 }

 /**
 * Test finish() method with IOException thrown.
 */
 @Test
 public void testFinishWithIOException() throws IOException {
 byte[] recordBuf = new byte[TarConstants.RECORD_SIZE];
 tarArchiveOutputStream.write(recordBuf);
 assertThrows(IOException.class, () -> tarArchiveOutputStream.finish());
 }

 /**
 * Test close() method with no exceptions thrown.
 */
 @Test
 public void testCloseWithNoExceptions() throws IOException {
 tarArchiveOutputStream.close();
 }

 /**
 * Test close() method with IOException thrown.
 */
 @Test
 public void testCloseWithIOException() throws IOException {
 byte[] recordBuf = new byte[TarConstants.RECORD_SIZE];
 tarArchiveOutputStream.write(recordBuf);
 assertThrows(IOException.class, () -> tarArchiveOutputStream.close());
 }

 /**
 * Test putArchiveEntry() method with valid ArchiveEntry.
 */
 @Test
 public void testPutArchiveEntryWithValidArchiveEntry() throws IOException {
 File testFile = new File("testFile.txt");
 String testFileName = "testFile.txt";
 TarArchiveEntry testArchiveEntry = new TarArchiveEntry(testFile, testFileName);
 tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
 }

 /**
 * Test putArchiveEntry() method with null ArchiveEntry.
 */
 @Test(expected = NullPointerException.class)
 public void testPutArchiveEntryWithNullArchiveEntry() {
 TarArchiveEntry testArchiveEntry = null;
 assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.putArchiveEntry(testArchiveEntry));
 }

 /**
 * Test putArchiveEntry() method with ArchiveEntry that exceeds TarConstants.NAMELEN.
 */
 @Test
 public void testPutArchiveEntryWithLongFileName() throws IOException {
 String longFileName = "longFileNameThatExceedsTarConstants.NAMELEN.length()";
 TarArchiveEntry testArchiveEntry = new TarArchiveEntry(new File("testFile.txt"), longFileName);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU); // set long file mode to write long file names
 tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
 }

 /**
 * Test putArchiveEntry() method with ArchiveEntry that has size > TarConstants.NAMELEN.
 */
 @Test
 public void testPutArchiveEntryWithLargeFileSize() throws IOException {
 File largeFile = new File("largeFile.txt");
 long largeFileSize = 1024 * 1024; // 1 MB
 TarArchiveEntry testArchiveEntry = new TarArchiveEntry(largeFile, "largeFile.txt");
 tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
 }

 /**
 * Test closeArchiveEntry() method with no exceptions thrown.
 */
 @Test
 public void testCloseArchiveEntryWithNoExceptions() throws IOException {
 TarArchiveEntry testArchiveEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 /**
 * Test closeArchiveEntry() method with IOException thrown.
 */
 @Test
 public void testCloseArchiveEntryWithIOException() throws IOException {
 TarArchiveEntry testArchiveEntry = new TarArchiveEntry(new File("testFile.txt"), "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(testArchiveEntry);
 assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
 }

 /**
 * Test write() method with valid byte array and offset and length.
 */
 @Test
 public void testWriteWithValidByteArrayOffsetLength() throws IOException {
 byte[] testByteArray = {0x01, 0x02, 0x03};
 int testOffset = 0;
 int testLength = 3;
 tarArchiveOutputStream.write(testByteArray, testOffset, testLength);
 }

 /**
 * Test write() method with null byte array.
 */
 @Test(expected = NullPointerException.class)
 public void testWriteWithNullByteArray() {
 byte[] testByteArray = null;
 tarArchiveOutputStream.write(testByteArray, 0, 0);
 }

 /**
 * Test write() method with byte array offset greater than length.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testWriteWithByteArrayOffsetGreaterThanLength() {
 byte[] testByteArray = {0x01, 0x02};
 int testOffset = 2;
 tarArchiveOutputStream.write(testByteArray, testOffset, 1);
 }

 /**
 * Test write() method with byte array length greater than available bytes to write.
 */
 @Test
 public void testWriteWithByteArrayLengthGreaterThanAvailableBytes() throws IOException {
 byte[] testByteArray = {0x01, 0x02, 0x03, 0x04};
 int testOffset = 0;
 int testLength = 5;
 tarArchiveOutputStream.write(testByteArray, testOffset, testLength);
 }

 /**
 * Test write() method with byte array offset less than 0.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testWriteWithByteArrayOffsetLessThanZero() {
 byte[] testByteArray = {0x01, 0x02};
 int testOffset = -1;
 tarArchiveOutputStream.write(testByteArray, testOffset, 2);
 }

 /**
 * Test write() method with byte array length less than 0.
 */
 @Test(expected = NegativeArraySizeException.class)
 public void testWriteWithByteArrayLengthLessThanZero() {
 byte[] testByteArray = {0x01, 0x02};
 int testLength = -1;
 tarArchiveOutputStream.write(testByteArray, 0, testLength);
 }
}