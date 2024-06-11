import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

 private OutputStream out;
 private TarArchiveOutputStream tarArchiveOutputStream;

 @Before
 public void setUp() throws Exception {
 out = new ByteArrayOutputStream();
 tarArchiveOutputStream = new TarArchiveOutputStream(out);
 }

 @After
 public void tearDown() throws Exception {
 tarArchiveOutputStream.close();
 }

 @Test
 public void testConstructor1() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 }

 @Test
 public void testConstructor2() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE);
 }

 @Test
 public void testConstructor3() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
 assertNotNull(tarArchiveOutputStream);
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
 }

 @Test
 public void testFinish() throws IOException {
 tarArchiveOutputStream.finish();
 assertNotNull(tarArchiveOutputStream.getRecordSize());
 assertEquals(2 * TarConstants.TAR_REC_SIZE, tarArchiveOutputStream.getRecordSize());
 }

 @Test
 public void testClose() throws IOException {
 tarArchiveOutputStream.close();
 assertFalse(tarArchiveOutputStream.isOpen());
 }

 @Test
 public void testWriteEOFRecords() throws IOException {
 byte[] expected = new byte[2 * TarConstants.TAR_REC_SIZE];
 Arrays.fill(expected, (byte) 0);
 tarArchiveOutputStream.finish();
 assertEquals(expected, out.toByteArray());
 }

 @Test
 public void testPutArchiveEntry() throws IOException {
 byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
 File testFile = new File("testFile.txt");
 TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(testFile, "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] expectedHeader = new byte[TarConstants.TAR_HDR_SIZE + testData.length];
 TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", testData.length, testData);
 testEntry.writeEntryHeader(expectedHeader);
 tarArchiveOutputStream.write(testData);
tarArchiveOutputStream.closeArchiveEntry();
 assertArrayEquals(expectedHeader, Arrays.copyOfRange(out.toByteArray(), 0, TarConstants.TAR_HDR_SIZE + testData.length));
 }

 @Test
 public void testCloseArchiveEntryBeforePut() throws IOException {
 TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.putArchiveEntry(entry);
 assertThrows(IOException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
 }

 @Test
 public void testCloseArchiveEntryTwice() throws IOException {
 byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
 File testFile = new File("testFile.txt");
 TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(testFile, "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write(testData);
 tarArchiveOutputStream.closeArchiveEntry();
 tarArchiveOutputStream.closeArchiveEntry();
 byte[] expected = new byte[TarConstants.TAR_HDR_SIZE + testData.length];
 TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", testData.length, testData);
 testEntry.writeEntryHeader(expected);
 assertArrayEquals(expected, Arrays.copyOfRange(out.toByteArray(), 0, TarConstants.TAR_HDR_SIZE + testData.length));
 }

 @Test
 public void testWriteBytes() throws IOException {
 byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
 byte[] expected = new byte[testData.length + TarConstants.TAR_HDR_SIZE];
 TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write(testData);
 tarArchiveOutputStream.closeArchiveEntry();
 testEntry.writeEntryHeader(Arrays.copyOfRange(expected, 0, TarConstants.TAR_HDR_SIZE));
 assertEquals(testData, Arrays.copyOfRange(out.toByteArray(), TarConstants.TAR_HDR_SIZE, TarConstants.TAR_HDR_SIZE + testData.length));
 }

 @Test
 public void testWriteSmallBuffer() throws IOException {
 byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
 byte[] smallBuffer = new byte[3];
 byte[] expected = new byte[testData.length + TarConstants.TAR_HDR_SIZE];
 TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 for (int i = 0; i < testData.length; i += smallBuffer.length) {
 tarArchiveOutputStream.write(smallBuffer, 0, smallBuffer.length);
 System.arraycopy(testData, i, smallBuffer, 0, smallBuffer.length);
 }
 tarArchiveOutputStream.closeArchiveEntry();
 testEntry.writeEntryHeader(Arrays.copyOfRange(expected, 0, TarConstants.TAR_HDR_SIZE));
 assertEquals(testData, Arrays.copyOfRange(out.toByteArray(), TarConstants.TAR_HDR_SIZE, TarConstants.TAR_HDR_SIZE + testData.length));
 }

 @Test
 public void testWriteSmallBufferLastChunkSmaller() throws IOException {
 byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
 byte[] smallBuffer = new byte[4];
 byte[] expected = new byte[testData.length + TarConstants.TAR_HDR_SIZE];
 TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 for (int i = 0; i < testData.length; i += smallBuffer.length) {
 tarArchiveOutputStream.write(smallBuffer, 0, Math.min(smallBuffer.length, testData.length - i));
 }
 tarArchiveOutputStream.write(Arrays.copyOfRange(testData, TarConstants.TAR_HDR_SIZE + TarConstants.TAR_HDR_SIZE, testData.length));
 tarArchiveOutputStream.closeArchiveEntry();
 testEntry.writeEntryHeader(Arrays.copyOfRange(expected, 0, TarConstants.TAR_HDR_SIZE));
 assertEquals(testData, Arrays.copyOfRange(out.toByteArray(), TarConstants.TAR_HDR_SIZE, TarConstants.TAR_HDR_SIZE + testData.length));
 }

 @Test
 public void testWriteOneByte() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("testFile.txt"));
 tarArchiveOutputStream.write(1);
 tarArchiveOutputStream.closeArchiveEntry();
 byte[] expected = new byte[TarConstants.TAR_HDR_SIZE + 1];
 TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt");
 testEntry.writeEntryHeader(expected);
 assertEquals(expected[TarConstants.TAR_HDR_SIZE], (byte) 1);
 }

 @Test
 public void testWriteEOFAfterPutNextEntry() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("testFile1.txt"));
 tarArchiveOutputStream.write(new byte[] {1, 2, 3});
 tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("testFile2.txt"));
 tarArchiveOutputStream.write(new byte[] {4, 5, 6});
 tarArchiveOutputStream.write(new byte[0]);
 tarArchiveOutputStream.close();
 byte[] expected = new byte[TarConstants.TAR_HDR_SIZE * 2 + 9];
 TarArchiveEntry testEntry1 = new TarArchiveEntry("testFile1.txt");
 testEntry1.writeEntryHeader(Arrays.copyOfRange(expected, 0, TarConstants.TAR_HDR_SIZE));
 TarArchiveEntry testEntry2 = new TarArchiveEntry("testFile2.txt");
 testEntry2.writeEntryHeader(Arrays.copyOfRange(expected, TarConstants.TAR_HDR_SIZE, TarConstants.TAR_HDR_SIZE * 2));
 assertEquals(expected, out.toByteArray());
 }

 @Test
 public void testWriteEOFAfterPutNextEntryTruncated() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
 TarArchiveEntry entry1 = tarArchiveOutputStream.createArchiveEntry(new File("testFile1.txt"), "testFile1.txt");
 tarArchiveOutputStream.putArchiveEntry(entry1);
 tarArchiveOutputStream.write(new byte[] {1, 2, 3});
 TarArchiveEntry entry2 = tarArchiveOutputStream.createArchiveEntry(new File("testFile2.txt"), "testFile2.txt");
 tarArchiveOutputStream.putArchiveEntry(entry2);
 tarArchiveOutputStream.write(new byte[2]);
 tarArchiveOutputStream.finish();
 byte[] expected = new byte[TarConstants.TAR_HDR_SIZE * 2 + 5];
 TarArchiveEntry testEntry1 = new TarArchiveEntry("testFile1.txt", 3, new byte[] {1, 2, 3});
 testEntry1.writeEntryHeader(Arrays.copyOfRange(expected, 0, TarConstants.TAR_HDR_SIZE));
 TarArchiveEntry testEntry2 = new TarArchiveEntry("testFile2.txt", 2, new byte[2]);
 testEntry2.writeEntryHeader(Arrays.copyOfRange(expected, TarConstants.TAR_HDR_SIZE, TarConstants.TAR_HDR_SIZE * 2));
 assertEquals(expected, out.toByteArray());
 }

 @Test
 public void testWriteLargeFile() throws IOException {
 long fileSize = 1024 * 1024; // 1 MB
 byte[] testData = new byte[fileSize];
 Path testFile = Files.createTempFile("test", "tar");
 try (FileOutputStream fos = new FileOutputStream(testFile.toFile());
 TarOutputStream tos = new TarOutputStream(fos)) {
 TarArchiveEntry entry = tos.createArchiveEntry(testFile.toFile(), "testFile.tar");
 tos.putNextEntry(entry);
 tos.write(testData, 0, (int) fileSize);
 tos.finish();
 }
 File inputFile = new File(testFile.toString());
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(inputFile));
 TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "testFile.tar");
 tarArchiveOutputStream.putArchiveEntry(entry);
 byte[] expected = new byte[fileSize + TarConstants.TAR_HDR_SIZE];
 Arrays.fill(expected, (byte) 0);
 TarArchiveEntry testEntry = new TarArchiveEntry("testFile.tar", fileSize, testData);
 testEntry.writeEntryHeader(expected);
 assertEquals(testData, Files.readAllBytes(inputFile.toPath()));
 assertArrayEquals(expected, tarArchiveOutputStream.getBuffer().getBuffer());
 tarArchiveOutputStream.close();
 }
}