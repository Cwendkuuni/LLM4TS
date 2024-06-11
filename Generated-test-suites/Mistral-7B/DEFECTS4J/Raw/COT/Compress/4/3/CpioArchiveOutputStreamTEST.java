import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

 private OutputStream outputStream;
 private CpioArchiveOutputStream cpioArchiveOutputStream;

 @Before
 public void setUp() throws Exception {
 outputStream = new MemoryOutputStream();
 cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
 }

 @Test
 public void testPutArchiveEntry() throws Exception {
 // Testing putting a new entry with valid parameters
 CpioArchiveEntry entry = new CpioArchiveEntry();
 entry.setName("testfile");
 entry.setFileSize(5);
 entry.setMode(CpioConstants.C_ISREG);
 cpioArchiveOutputStream.putArchiveEntry(entry);
 // Checking if the entry was written correctly
 byte[] bytes = outputStream.toByteArray();
 assertTrue(ArchiveUtils.isCpioMagicNumber(bytes, 0));
 // Checking if the header of the entry was written correctly
 CpioArchiveEntry readEntry = new CpioArchiveEntry();
 readEntry.parseFrom(new ByteArrayInputStream(bytes));
 assertEquals("testfile", readEntry.getName());
 assertEquals(5, readEntry.getSize());
 // Testing putting the same entry again
 cpioArchiveOutputStream.putArchiveEntry(entry);
 // Checking if an IOException is thrown when trying to put an entry with the same name again
 try {
 cpioArchiveOutputStream.putArchiveEntry(entry);
 fail();
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutArchiveEntryWithInvalidFormat() throws Exception {
 cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 }

 @Test
 public void testWrite() throws Exception {
 // Testing writing data to the entry
 byte[] data = "testdata".getBytes();
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.write(data, 0, data.length);
 byte[] bytes = outputStream.toByteArray();
 byte[] expected = ArchiveUtils.addDataToStream(bytes, data);
 // Checking if the data was written correctly
 assertArrayEquals(expected, bytes);
 }

 @Test(expected = IOException.class)
 public void testWriteAfterClose() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.close();
 cpioArchiveOutputStream.write(new byte[5], 0, 5);
 }

 @Test(expected = IOException.class)
 public void testWriteAfterFinish() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.finish();
 cpioArchiveOutputStream.write(new byte[5], 0, 5);
 }

 @Test(expected = IOException.class)
 public void testWriteWithClosedStream() throws Exception {
 outputStream = new MemoryOutputStream();
 cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
 cpioArchiveOutputStream.close();
 cpioArchiveOutputStream.write(new byte[5], 0, 5);
 }

 @Test(expected = IOException.class)
 public void testWriteWithWrongDataSize() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.write(new byte[5], 0, 6);
 }

 @Test
 public void testFinish() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.finish();
 byte[] bytes = outputStream.toByteArray();
 assertTrue(ArchiveUtils.isCpioTrailerMagicNumber(bytes));
 }

 @Test(expected = IOException.class)
 public void testFinishWithEntries() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testFinishWithData() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.write(new byte[5], 0, 5);
 cpioArchiveOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testCloseWithEntries() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.close();
 }

 @Test(expected = IOException.class)
 public void testCloseWithData() throws Exception {
 cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry());
 cpioArchiveOutputStream.write(new byte[5], 0, 5);
 cpioArchiveOutputStream.close();
 }

 @After
 public void tearDown() throws Exception {
 outputStream.close();
 }

}