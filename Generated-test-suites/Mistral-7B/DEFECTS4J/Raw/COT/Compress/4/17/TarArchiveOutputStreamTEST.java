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
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

 private OutputStream out;
 private TarArchiveOutputStream tarArchiveOutputStream;
 private ByteArrayOutputStream byteArrayOutputStream;

 @Before
 public void setUp() throws Exception {
 out = new ByteArrayOutputStream();
 byteArrayOutputStream = new ByteArrayOutputStream();
 tarArchiveOutputStream = new TarArchiveOutputStream(out);
 }

 @After
 public void tearDown() throws Exception {
 tarArchiveOutputStream.close();
 }

 @Test
 public void testConstructor() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("test.tar").getOutputStream());
 }

 @Test
 public void testConstructorWithBlockSizeAndRecordSize() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("test.tar").getOutputStream(), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithNullOutputStream() {
 new TarArchiveOutputStream(null);
 }

 @Test
 public void testPutArchiveEntry() throws IOException {
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test(expected = ClassCastException.class)
 public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
 ArchiveEntry archiveEntry = new File("test.txt").toPath().toFile();
 tarArchiveOutputStream.putArchiveEntry(archiveEntry);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithLongFileName() throws IOException {
 String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
 TarArchiveEntry entry = new TarArchiveEntry(longFileName);
 tarArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
 tarArchiveOutputStream.write(TarConstants.MAGIC, 0, TarConstants.MAGIC.length);
 tarArchiveOutputStream.closeArchiveEntry();
 byte[] expectedHeader = {
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
 };
 int headerLength = 512;
 byte[] header = new byte[headerLength];
 System.arraycopy(expectedHeader, 0, header, 0, headerLength);
 byte[] resultHeader = tarArchiveOutputStream.getBuffer().getRecord();
 assertEquals(header, resultHeader);
 }

 @Test
 public void testFinish() throws IOException {
 byte[] expectedEOF = new byte[512];
 for (int i = 0; i < expectedEOF.length; ++i) {
 expectedEOF[i] = 0;
 }
 tarArchiveOutputStream.finish();
 byte[] resultEOF = tarArchiveOutputStream.getBuffer().getRecord();
 assertEquals(expectedEOF, resultEOF);
 }

 @Test
 public void testSetLongFileMode() throws IOException {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @Test
 public void testClose() throws IOException {
 byte[] expectedResult = byteArrayOutputStream.toByteArray();
 tarArchiveOutputStream.close();
 byte[] result = out.toByteArray();
 assertArrayEquals(expectedResult, result);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 TarArchiveEntry entry = new TarArchiveEntry("test.txt");
 tarArchiveOutputStream.putArchiveEntry(entry);
 tarArchiveOutputStream.write(TarConstants.MAGIC, 0, TarConstants.MAGIC.length);
 }
}