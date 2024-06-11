import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

 private OutputStream out;
 private TarArchiveOutputStream tarArchiveOutputStream;

 @Before
 public void setUp() {
 out = new ByteArrayOutputStream();
 tarArchiveOutputStream = new TarArchiveOutputStream(out);
 }

 @After
 public void tearDown() throws IOException {
 tarArchiveOutputStream.close();
 }

 @Test
 public void testConstructorWithOutputStream() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
 }

 @Test
 public void testConstructorWithOutputStreamBlockSizeAndRecordSize() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out, 1024, 512);
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test(expected = RuntimeException.class)
 public void testPutArchiveEntryWithTooLongFileName() throws IOException {
 String longFileName = "".repeat(TarConstants.NAMELEN + 1);
 TarArchiveEntry entry = new TarArchiveEntry(longFileName);
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testPutArchiveEntryWithValidFileName() throws IOException {
 String validFileName = "testFileName.txt";
 TarArchiveEntry entry = new TarArchiveEntry(validFileName);
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testPutArchiveEntryWithLongFileNameAndLongFileModeGNU() throws IOException {
 String longFileName = "testFileNameWithLongName.txt";
 TarArchiveEntry entry = new TarArchiveEntry(longFileName);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test(expected = RuntimeException.class)
 public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
 TarArchiveEntry entry = new TarArchiveEntry("testFileName.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 String fileName = "testFileName.txt";
 TarArchiveEntry entry = new TarArchiveEntry(fileName);
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.closeArchiveEntry();
 }

 @Test(expected = IOException.class)
 public void testWriteWithBytesBeyondSizeSpecifiedInHeader() throws IOException {
 byte[] bytesToWrite = new byte[1024 * 2];
 tarArchiveOutputStream.write(bytesToWrite, 0, bytesToWrite.length);
 }

 @Test
 public void testWrite() throws IOException {
 byte[] bytesToWrite = new byte[1024];
 tarArchiveOutputStream.write(bytesToWrite, 0, bytesToWrite.length);
 }

 @Test
 public void testWriteWithSmallBuffers() throws IOException {
 byte[] bytesToWrite = new byte[1024];
 tarArchiveOutputStream.write(bytesToWrite, 0, 512);
 tarArchiveOutputStream.write(bytesToWrite, 512, 512);
 }

 @Test
 public void testFinish() throws IOException {
 tarArchiveOutputStream.finish();
 }

 @Test
 public void testClose() throws IOException {
 tarArchiveOutputStream.close();
 }

 @Test
 public void testGetRecordSize() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
 assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
 }
}