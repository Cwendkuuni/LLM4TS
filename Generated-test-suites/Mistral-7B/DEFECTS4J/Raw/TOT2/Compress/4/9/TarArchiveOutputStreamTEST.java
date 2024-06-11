import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit. After;
import org.junit. Before;
import org.junit. Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {

 private OutputStream outputStream;
 private TarArchiveOutputStream tarArchiveOutputStream;
 private ByteArrayOutputStream byteArrayOutputStream;
 private ByteArrayInputStream byteArrayInputStream;

 @Before
 public void setUp() throws Exception {
 outputStream = new ByteArrayOutputStream();
 tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 }

 @After
 public void tearDown() throws Exception {
 tarArchiveOutputStream.close();
 }

 /**
 * Test default constructor.
 */
 @Test
 public void testDefaultConstructor() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStreamDefault = new TarArchiveOutputStream(new ByteArrayOutputStream());
 assertEquals(TarArchiveOutputStream.DEFAULT_BLKSIZE, tarArchiveOutputStreamDefault.getBlockSize());
 assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarArchiveOutputStreamDefault.getRecordSize());
 }

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStreamConstructor = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
 assertEquals(1024, tarArchiveOutputStreamConstructor.getBlockSize());
 assertEquals(512, tarArchiveOutputStreamConstructor.getRecordSize());
 }

 /**
 * Test setLongFileMode method.
 */
 @Test
 public void testSetLongFileMode() throws IOException {
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 /**
 * Test finish method.
 */
 @Test
 public void testFinish() throws IOException {
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE, outputStream.size());
 }

 /**
 * Test close method.
 */
 @Test
 public void testClose() throws IOException {
 tarArchiveOutputStream.finish();
 tarArchiveOutputStream.close();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + outputStream.size(), byteArrayInputStream.available());
 }

 /**
 * Test putArchiveEntry method with null archiveEntry.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testPutArchiveEntryNullArchiveEntry() throws IOException {
 tarArchiveOutputStream.putArchiveEntry(null);
 }

 /**
 * Test putArchiveEntry method with empty archiveName.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testPutArchiveEntryEmptyArchiveName() throws IOException {
 String emptyArchiveName = "";
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("testFile.txt"), emptyArchiveName);
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 }

 /**
 * Test putArchiveEntry method with empty file.
 */
 @Test
 public void testPutArchiveEntryEmptyFile() throws IOException {
 File emptyFile = new File("testFile.txt");
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(emptyFile, "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH
         + 1, outputStream.size());
 }

 /**
 * Test putArchiveEntry method with file.
 */
 @Test
 public void testPutArchiveEntryFile() throws IOException {
 File file = new File("testFile.txt");
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(file, "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.write("Test file content".getBytes());
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + TarConstants.HEADER_SIZE
         + 1 + TarConstants.NAME_LENGTH + "Test file content".length(), outputStream.size());
 }

 /**
 * Test closeArchiveEntry method without write.
 */
 @Test
 public void testCloseArchiveEntryWithoutWrite() throws IOException {
 // Empty entry
 String emptyArchiveName = "";
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(new File("testFile.txt"), emptyArchiveName);
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH, outputStream.size());

 // Non-empty entry
 File file = new File("testFile.txt");
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(file, "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.write("Test file content".getBytes());
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH
         + "Test file content".length(), outputStream.size());
 }

 /**
 * Test closeArchiveEntry method with write.
 */
 @Test
 public void testCloseArchiveEntryWithWrite() throws IOException {
 File file = new File("testFile.txt");
 TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(file, "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
 tarArchiveOutputStream.write("Test file content".getBytes());
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH
         + "Test file content".length(), outputStream.size());
 }

 /**
 * Test write method with empty wBuf.
 */
 @Test
 public void testWriteEmptywBuf() throws IOException {
 byte[] wBuf = new byte[0];
 tarArchiveOutputStream.write(wBuf, 0, 0);
 }

 /**
 * Test write method with small wBuf.
 */
 @Test
 public void testWriteSmallwBuf() throws IOException {
 byte[] wBuf = new byte[4];
 tarArchiveOutputStream.write(wBuf, 0, 4);
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + TarConstants.HEADER_SIZE + 1 + TarConstants.NAME_LENGTH
         + 4, outputStream.size());
 }

 /**
 * Test write method with big wBuf.
 */
 @Test
 public void testWriteBigwBuf() throws IOException {
 byte[] wBuf = new byte[1024];
 tarArchiveOutputStream.write(wBuf, 0, 1024);
 tarArchiveOutputStream.finish();
 byteArrayOutputStream.writeTo(outputStream);
 assertEquals(2 * TarConstants.STANDARD_MAGIC_SIZE + TarConstants.HEADER_SIZE + 1 + TarConstants.NAME_LENGTH
         + 1024, outputStream.size());
 }

 /**
 * Test write method with wBuf longer than remaining space in the archive.
 */
 @Test(expected = IOException.class)
 public void testWritewBufLongerThanRemainingSpace() throws IOException {
 byte[] wBuf = new byte[1024 + 1];
 tarArchiveOutputStream.write(wBuf, 0, 1024 + 1);
 }
}