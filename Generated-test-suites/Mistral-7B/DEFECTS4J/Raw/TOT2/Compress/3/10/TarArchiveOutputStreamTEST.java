import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

 @Test
 public void testPutArchiveEntryAndClose() throws Exception {
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"));;
 TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
 tarOS.putArchiveEntry(entry);
 tarOS.write("test content".getBytes());
 tarOS.closeArchiveEntry();
 tarOS.finish();
 tarOS.close();
 }

 @Test
 public void testPutArchiveEntryWithLongFileName() throws Exception {
 String longFileName = "a/long/path/for/testing/test.txt";
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
 tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 TarArchiveEntry entry = new TarArchiveEntry(longFileName, "test content".getBytes());
 tarOS.putArchiveEntry(entry);
 tarOS.write(longFileName.getBytes());
 tarOS.write(0); // NUL terminator
 tarOS.closeArchiveEntry();
 tarOS.finish();
 tarOS.close();
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws Exception {
 String longFileName = "a/long/path/for/testing/test.txt";
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
 TarArchiveEntry entry = new TarArchiveEntry(longFileName, "test content".getBytes());
 tarOS.putArchiveEntry(entry);
 tarOS.finish();
 tarOS.close();
 }

 @Test(expected = IOException.class)
 public void testPutArchiveEntryWithLongFileNameAndTruncateMode() throws Exception {
 String longFileName = "a/long/path/for/testing/test.txt";
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
 TarArchiveEntry entry = new TarArchiveEntry(longFileName, "test content".getBytes());
 tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 tarOS.putArchiveEntry(entry);
 tarOS.write(entry.getName().substring(0, TarConstants.NAMELEN).getBytes());
 tarOS.write(0); // NUL terminator
 tarOS.finish();
 tarOS.close();
 }

 @Test
 public void testWrite() throws Exception {
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
 TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
 tarOS.putArchiveEntry(entry);
 tarOS.write("test content".getBytes());
 tarOS.closeArchiveEntry();
 tarOS.finish();
 tarOS.close();
 }

 @Test
 public void testWriteWithAssemble() throws Exception {
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
 byte[] wBuf = "test content".getBytes();
 int wOffset = 0;
 int numToWrite = wBuf.length;
 tarOS.write(wBuf, wOffset, numToWrite);
 tarOS.write(wBuf, wOffset, numToWrite);
 tarOS.finish();
 tarOS.close();
 }

 @Test(expected = IOException.class)
 public void testWriteWithOutEntry() throws Exception {
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
 byte[] wBuf = "test content".getBytes();
 int wOffset = 0;
 int numToWrite = wBuf.length;
 tarOS.write(wBuf, wOffset, numToWrite);
 tarOS.finish();
 tarOS.close();
 }

 @Test(expected = IOException.class)
 public void testWriteWithOutEntryAndAssemble() throws Exception {
 TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new FileOutputStream("test.tar"), TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
 byte[] wBuf = "test content".getBytes();
 int wOffset = 0;
 int numToWrite = wBuf.length;
 tarOS.write(wBuf, wOffset, numToWrite);
 tarOS.write(wBuf, wOffset, numToWrite);
 tarOS.finish();
 tarOS.close();
 }

 @Test
 public void testCreateArchiveEntry() throws Exception {
 File inputFile = new File("test.txt");
 TarArchiveEntry entry = new TarArchiveOutputStream(new FileOutputStream("test.tar")).createArchiveEntry(inputFile, "test.txt");
 }
}