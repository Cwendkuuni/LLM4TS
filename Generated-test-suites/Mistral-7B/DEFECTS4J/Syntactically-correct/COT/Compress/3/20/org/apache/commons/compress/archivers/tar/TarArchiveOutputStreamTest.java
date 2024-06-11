import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

 private OutputStream outputStream;
 private TarArchiveOutputStream tarArchiveOutputStream;

 @Before
 public void setUp() throws Exception {
 outputStream = new ByteArrayOutputStream();
 tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
 }

 @After
 public void tearDown() throws Exception {
 tarArchiveOutputStream.close();
 }

 @Test
 public void testConstructorWithOutputStream() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
 }

 @Test
 public void testConstructorWithOutputStreamBlockSize() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE);
 assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
 }

 @Test
 public void testConstructorWithOutputStreamBlockSizeRecordSize() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
 assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBlockSize() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, -1);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRecordSize() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, -1);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSetLongFileMode() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
 }

 @Test
 public void testFinish() throws Exception {
 TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write("Hello World!".getBytes());
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();

 byte[] result = outputStream.toByteArray();
 byte[] expectedEOF = new byte[TarBuffer.RECORD_SIZE];
 Arrays.fill(expectedEOF, (byte) 0);
 int expectedRecordSize = TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE * 2;

 assertEquals(expectedRecordSize + entry.getSize(), result.length);

 for (int i = 0; i < result.length; i++) {
 if (i < TarConstants.HEADER_SIZE) {
 assertEquals(entry.getHeaderBytes()[i], result[i]);
 } else if (i >= result.length - TarConstants.RECORD_SIZE && i < result.length) {
 assertEquals(expectedEOF[i % TarConstants.RECORD_SIZE], result[i]);
 } else {
 assertNotSame(entry.getHeaderBytes()[i % TarConstants.HEADER_SIZE], result[i]);
 }
 }
 }

 @Test(expected = IOException.class)
 public void testFinishBeforePutArchiveEntry() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 tarArchiveOutputStream.finish();
 fail("Should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testCloseWhileWriting() throws Exception {
 TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write("Hello World!".getBytes());
 tarArchiveOutputStream.close();
 fail("Should throw IOException");
 }

 @Test
 public void testClose() throws Exception {
 TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write("Hello World!".getBytes());
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.close();
 }

 @Test
 public void testGetRecordSize() throws Exception {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
 assertEquals(tarArchiveOutputStream.getRecordSize(), TarBuffer.RECORD_SIZE);
 }

 @Test
 public void testPutArchiveEntryWithLongFileName() throws Exception {
 String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/z";
 TarArchiveEntry entry = new TarArchiveEntry(longFileName);
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write("Hello World!".getBytes());
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.finish();

 byte[] result = outputStream.toByteArray();
 byte[] longFileNameBytes = longFileName.getBytes();
 int longFileNameLength = longFileNameBytes.length;
 int expectedRecordSize = TarConstants.HEADER_SIZE + TarConstants.RECORD_SIZE * 3;

 assertEquals(expectedRecordSize + entry.getSize(), result.length);

 for (int i = 0; i < result.length; i++) {
 if (i < TarConstants.HEADER_SIZE) {
 assertEquals(entry.getHeaderBytes()[i], result[i]);
 } else if (i >= result.length - TarConstants.RECORD_SIZE && i < result.length) {
 byte recordHeaderByte = i % TarConstants.RECORD_SIZE == 0 ? (byte) ('H' + (i / TarConstants.RECORD_SIZE)) : result[i - 1];
 assertEquals(recordHeaderByte, result[i]);
 } else if (i >= result.length - longFileNameLength - TarConstants.RECORD_SIZE && i < result.length) {
 for (int j = 0; j < longFileNameLength; j++) {
 assertEquals(longFileNameBytes[j], result[i + j]);
 }
 } else {
 assertNotSame(entry.getHeaderBytes()[i % TarConstants.HEADER_SIZE], result[i]);
 }
 }
 }
}