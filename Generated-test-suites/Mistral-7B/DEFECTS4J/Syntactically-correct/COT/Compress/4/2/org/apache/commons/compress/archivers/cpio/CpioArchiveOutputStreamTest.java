import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

 private OutputStream out;
 private CpioArchiveOutputStream cpioArchiveOutputStream;

 @Before
 public void setUp() throws Exception {
 out = new MemoryOutputStream();
 cpioArchiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
 }

 @Test
 public void testPutArchiveEntry() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 entry.setFileSize(10);
 entry.setMode(CpioConstants.C_ISREG);
 cpioArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryDuplicateName() throws Exception {
 CpioArchiveEntry entry1 = new CpioArchiveEntry("testfile.txt");
 CpioArchiveEntry entry2 = new CpioArchiveEntry("testfile.txt");
 cpioArchiveOutputStream.putArchiveEntry(entry1);
 cpioArchiveOutputStream.putArchiveEntry(entry2);
 }

 @Test
 public void testPutArchiveEntryWrongFormat() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
 cpioArchiveOutputStream.putArchiveEntry(entry);
 }

 @Test(expected = IOException.class)
 public void testWriteWithClosedStream() throws Exception {
 cpioArchiveOutputStream.close();
 cpioArchiveOutputStream.write(new byte[10], 0, 10);
 }

 @Test
 public void testFinish() throws Exception {
 cpioArchiveOutputStream.finish();
 }

 @Test(expected = IOException.class)
 public void testFinishWithEntries() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 cpioArchiveOutputStream.putArchiveEntry(entry);
 cpioArchiveOutputStream.finish();
 }

 @Test
 public void testClose() throws Exception {
 cpioArchiveOutputStream.close();
 }

 @Test
 public void testCreateArchiveEntry() throws Exception {
 File file = new File("testfile.txt");
 CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(file, "testfile.txt");
 assertEquals("testfile.txt", entry.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveEntryNullFile() throws Exception {
 cpioArchiveOutputStream.createArchiveEntry(null, "testfile.txt");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateArchiveEntryEmptyName() throws Exception {
 cpioArchiveOutputStream.createArchiveEntry(new File("testfile.txt"), "");
 }

}