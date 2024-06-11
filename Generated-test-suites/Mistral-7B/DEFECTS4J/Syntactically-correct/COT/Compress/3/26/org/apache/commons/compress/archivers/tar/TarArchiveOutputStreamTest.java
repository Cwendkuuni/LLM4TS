import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

 private TarArchiveOutputStream tarOutputStream;
 private final String TEST_FILE_NAME = "testFile.txt";
 private final String TEST_LONG_FILE_NAME = "testLongFileName.txt";
 private final String TEST_DIRECTORY_NAME = "testDirectory";
 private final File testFile = new File(System.getProperty("java.io.tmpdir"), TEST_FILE_NAME);
 private final File testDirectory = new File(System.getProperty("java.io.tmpdir"), TEST_DIRECTORY_NAME);

 @Before
 public void setUp() throws Exception {
 if (!testDirectory.exists()) {
 testDirectory.mkdir();
 }
 }

 @After
 public void tearDown() throws Exception {
 if (testDirectory.exists()) {
 testDirectory.delete();
 }
 }

 @Test
 public void testConstructor() {
 OutputStream outputStream = new byte[0].opensystemoutputstream();
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
 assertNotNull(tarOutputStream);
 }

 @Test
 public void testSetLongFileMode() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLongFileModeWithInvalidMode() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.setLongFileMode(3);
 }

 @Test
 public void testFinish() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testFinishWithPreviousEntry() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.putArchiveEntry(new TarArchiveEntry(testFile));
 tarOutputStream.finish();
 }

 @Test
 public void testClose() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.close();
 }

 @Test(expected = IOException.class)
 public void testCloseWithPreviousEntry() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.putArchiveEntry(new TarArchiveEntry(testFile));
 tarOutputStream.close();
 }

 @Test
 public void testGetRecordSize() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
 }

 @Test
 public void testPutArchiveEntry() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(testFile);
 tarOutputStream.putArchiveEntry(tarEntry);
 }

 @Test(expected = NullPointerException.class)
 public void testPutArchiveEntryWithNullEntry() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarOutputStream.putArchiveEntry(null);
 }

 @Test(expected = ClassCastException.class)
 public void testPutArchiveEntryWithNonTarArchiveEntry() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 File file = new File("testFile.txt");
 tarOutputStream.putArchiveEntry(file);
 }

 @Test
 public void testCloseArchiveEntry() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(testFile);
 tarOutputStream.putArchiveEntry(tarEntry);
 tarOutputStream.closeArchiveEntry();
 }

 @Test(expected = IOException.class)
 public void testCloseArchiveEntryWithPreviousData() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(testFile);
 tarOutputStream.putArchiveEntry(tarEntry);
 tarOutputStream.write(new byte[]{0x01, 0x02, 0x03});
 tarOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWrite() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(testFile);
 tarOutputStream.putArchiveEntry(tarEntry);
 byte[] data = "Data to write".getBytes();
 tarOutputStream.write(data);
 tarOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteWithPreviousEntryData() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(testFile);
 tarOutputStream.putArchiveEntry(tarEntry);
 tarOutputStream.write(new byte[]{0x01, 0x02, 0x03});
 tarOutputStream.write(new byte[]{0x04, 0x05, 0x06});
 tarOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteWithLongFileName() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(new File(testDirectory, TEST_LONG_FILE_NAME));
 tarOutputStream.putArchiveEntry(tarEntry);
 String longFileName = new String(new char[TarConstants.NAMELEN + 1]).replace('\0', 'a');
 byte[] longFileNameBytes = longFileName.getBytes();
 tarOutputStream.write(longFileNameBytes);
 tarOutputStream.write(0); // NUL terminator
 tarOutputStream.closeArchiveEntry();
 }

 @Test(expected = IOException.class)
 public void testWriteWithLongFileNameTruncated() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(new File(testDirectory, TEST_LONG_FILE_NAME));
 tarOutputStream.putArchiveEntry(tarEntry);
 String longFileName = new String(new char[TarConstants.NAMELEN + 1]).replace('\0', 'a');
 byte[] longFileNameBytes = longFileName.getBytes();
 tarOutputStream.write(longFileNameBytes, 0, TarConstants.NAMELEN);
 tarOutputStream.write(0); // NUL terminator
 tarOutputStream.closeArchiveEntry();
 }

 @Test
 public void testWriteWithLongFileNameGNU() throws Exception {
 TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry tarEntry = new TarArchiveEntry(new File(testDirectory, TEST_LONG_FILE_NAME), TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
 tarOutputStream.putArchiveEntry(tarEntry);
 String longFileName = new String(new char[TarConstants.NAMELEN + 1]).replace('\0', 'a');
 byte[] longFileNameBytes = longFileName.getBytes();
 tarOutputStream.write(longFileNameBytes);
 tarOutputStream.write(0); // NUL terminator
 tarOutputStream.write(longFileNameBytes);
 tarOutputStream.closeArchiveEntry();
 }
}