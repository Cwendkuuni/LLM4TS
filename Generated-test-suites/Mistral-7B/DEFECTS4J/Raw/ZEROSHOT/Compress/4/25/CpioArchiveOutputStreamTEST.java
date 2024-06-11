import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

 private OutputStream outputStream;
 private CpioArchiveOutputStream cpioOutputStream;

 @Before
 public void setUp() throws Exception {
 outputStream = new MemoryOutputStream();
 cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
 }

 @Test
 public void testPutArchiveEntry() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 cpioOutputStream.putArchiveEntry(entry);
 assertEquals("testfile.txt", cpioOutputStream.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutArchiveEntryWithInvalidFormat() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 cpioOutputStream.putArchiveEntry(entry); // valid entry
 cpioOutputStream.putArchiveEntry(entry); // should throw exception with invalid format
 }

 @Test
 public void testWriteHeader() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 cpioOutputStream.putArchiveEntry(entry);
 assertEquals(CpioConstants.MAGIC_NEW, cpioOutputStream.readNBytes(8));
 }

 @Test
 public void testWriteNewEntryData() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 cpioOutputStream.putArchiveEntry(entry);
 byte[] header = new byte[entry.getHeaderSize()];
 cpioOutputStream.readFully(header);
 assertEquals(CpioConstants.C_ISREG, header[0] & 0xFF);
 assertEquals(CpioConstants.FORMAT_NEW, (header[1] & 0xFF) << 4 | (header[2] & 0xF));
 assertEquals(entry.getUID(), ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 3, 11)));
 assertEquals(entry.getGID(), ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 11, 15)));
 assertEquals(entry.getSize(), ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 15, 23)));
 assertEquals(entry.getTime(), ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 23, 31)));
 }

 @Test
 public void testWriteOldAsciiEntryData() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
 cpioOutputStream.putArchiveEntry(entry);
 byte[] header = new byte[entry.getHeaderSize()];
 cpioOutputStream.readFully(header);
 // 6-digit device number
 assertEquals(0x2, header[0] & 0xFF);
 assertEquals(0x0, header[1] & 0xFF);
 // 6-digit inode number
 assertEquals('t', header[2]);
 assertEquals('e', header[3]);
 assertEquals('s', header[4]);
 assertEquals('t', header[5]);
 assertEquals('f', header[6]);
 assertEquals('i', header[7]);
 assertEquals('l', header[8]);
 assertEquals('e', header[9]);
 assertEquals('.', header[10]);
 assertEquals('t', header[11]);
 assertEquals('x', header[12]);
 assertEquals('t', header[13]);
 // mode
 assertEquals(CpioConstants.C_ISREG, header[14] & 0xFF);
 // uid
 assertEquals('0', header[15]);
 assertEquals('1', header[16]);
 assertEquals('2', header[17]);
 assertEquals('3', header[18]);
 assertEquals('4', header[19]);
 assertEquals('5', header[20]);
 assertEquals(':', header[21]);
 // gid
 assertEquals('0', header[22]);
 assertEquals('1', header[23]);
 assertEquals('2', header[24]);
 assertEquals('3', header[25]);
 assertEquals(':', header[26]);
 // number of links
 assertEquals('1', header[27]);
 // size
 assertEquals('0', header[28]);
 assertEquals('1', header[29]);
 assertEquals('2', header[30]);
 assertEquals('3', header[31]);
 // time
 assertEquals(0, ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 32, 40)));
 // name length + 1
 assertEquals('t', header[40]);
 assertEquals('e', header[41]);
 assertEquals('s', header[42]);
 assertEquals('t', header[43]);
 assertEquals('f', header[44]);
 assertEquals('i', header[45]);
 assertEquals('l', header[46]);
 assertEquals('e', header[47]);
 assertEquals('.', header[48]);
 assertEquals('t', header[49]);
 assertEquals('x', header[50]);
 assertEquals('t', header[51]);
 // checksum
 assertEquals(0, header[52]);
 assertEquals(0, header[53]);
 assertEquals(0, header[54]);
 assertEquals(0, header[55]);
 }

 @Test
 public void testWriteOldBinaryEntryData() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
 cpioOutputStream.putArchiveEntry(entry);
 byte[] header = new byte[entry.getHeaderSize()];
 cpioOutputStream.readFully(header);
 // device number
 assertEquals(0x0200, ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 0, 2)));
 // inode number
 assertEquals(0x01234567, ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 2, 6)));
 // mode
 assertEquals(CpioConstants.C_ISREG, header[6] & 0xFF);
 // uid
 assertEquals(0x0101, ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 7, 11)));
 // gid
 assertEquals(0x0102, ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 11, 15)));
 // number of links
 assertEquals(0x01, header[15]);
 // size
 assertEquals(0x03040506, ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 16, 22)));
 // time
 assertEquals(0x0123456789ABCDEF, ArchiveUtils.byteArrayToLong(Arrays.copyOfRange(header, 22, 30)));
 // name length + 1
 assertEquals(0x07, header[30]);
 // name
 assertEquals('t', header[31]);
 assertEquals('e', header[32]);
 assertEquals('s', header[33]);
 assertEquals('t', header[34]);
 assertEquals('f', header[35]);
 assertEquals('i', header[36]);
 assertEquals('l', header[37]);
 assertEquals('e', header[38]);
 assertEquals('.', header[39]);
 assertEquals('t', header[40]);
 assertEquals('x', header[41]);
 assertEquals('t', header[42]);
 // checksum
 assertEquals(0, header[43]);
 assertEquals(0, header[44]);
 assertEquals(0, header[45]);
 assertEquals(0, header[46]);
 }

 @Test
 public void testWriteWithData() throws Exception {
 CpioArchiveEntry entry = new CpioArchiveEntry("testfile.txt");
 cpioOutputStream.putArchiveEntry(entry);
 byte[] data = "Hello World!".getBytes();
 cpioOutputStream.write(data, 0, data.length);
 cpioOutputStream.closeArchiveEntry();
 byte[] output = outputStream.toByteArray();
 assertArrayEquals(data, Arrays.copyOfRange(output, cpioOutputStream.getFilePointer() - data.length, cpioOutputStream.getFilePointer()));
 }

 @Test
 public void testFinish() throws Exception {
 cpioOutputStream.finish();
 byte[] trailer = new byte[CpioConstants.TRAILER_SIZE];
 cpioOutputStream.readFully(trailer);
 assertEquals(CpioConstants.MAGIC_TRAILER, Arrays.copyOfRange(trailer, 0, 8));
 }

 @Test
 public void testClose() throws Exception {
 cpioOutputStream.close();
 byte[] output = outputStream.toByteArray();
 assertEquals(CpioArchiveOutputStream.SIZE_UNKNOWN, cpioOutputStream.getSize());
 }

}