import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

 @Test
 public void testGetNextZipEntry() throws Exception {
 ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
 new ByteArrayInputStream(
 "PK\u0003\u0004PK\u0007\u0001Local File Header\n" +
 "2 0 01-01-01 01:01:01.01 0000400000001054Namel.txt\u0000\u0000" +
 "PK\u0006\u0002Central Directory\n" +
 "12 0 Central Directory File Descriptor\n" +
 "18 0 End of Central Directory Record\n" +
 "0 0 Size of this disk\n" +
 "0 0 Number of disk with entries\n" +
 "0 0 Total number of entries\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory\n" +
 "PK\u0001\u0002Local File Header\n" +
 "2 0 01-01-01 01:01:01.01 0000400000001054Name2.txt\u0000\u0000" +
 "0 0 Name Length\n" +
 "0 0 Extra Field Length\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 File comment length\n" +
 "0 0 File comment length in extra field\n" +
 "0 0 Disc number starting file in this disk\n" +
 "0 0 Internal file attributes\n" +
 "0 0 External file attributes\n" +
 "0 0 Local header signature\n" +
 "PK\u0005Data Descriptor\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 Local header offset\n" +
 "0 0 Data descriptor signature\n" +
 "0 0 Compression method\n" +
 "0 0 Ways to compress the data\n" +
 "0 0 File comment length\n" +
 "Name2.txt\u0000\u0000\n" +
 "PK\u0005Data Descriptor\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 Local header offset\n" +
 "0 0 Data descriptor signature\n" +
 "0 0 Compression method\n" +
 "0 0 Ways to compress the data\n" +
 "0 0 File comment length\n" +
 "Name1.txt\u0000\u0000\n" +
 "0 0\u0000"), ZipEncodingHelper.UTF8, true);
 ZipArchiveEntry entry1 = zipArchiveInputStream.getNextZipEntry();
 assertEquals("Name1.txt", entry1.getName());
 ZipArchiveEntry entry2 = zipArchiveInputStream.getNextZipEntry();
 assertEquals("Name2.txt", entry2.getName());
 ZipArchiveEntry entry3 = zipArchiveInputStream.getNextZipEntry();
 assertNull(entry3);
 }

 @Test
 public void testRead() throws Exception {
 ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
 new ByteArrayInputStream(
 "PK\u0003\u0004PK\u0007\u0001Local File Header\n" +
 "2 0 01-01-01 01:01:01.01 0000400000001054Namel.txt\u0000\u0000" +
 "PK\u0006\u0002Central Directory\n" +
 "12 0 Central Directory File Descriptor\n" +
 "18 0 End of Central Directory Record\n" +
 "0 0 Size of this disk\n" +
 "0 0 Number of disk with entries\n" +
 "0 0 Total number of entries\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory\n" +
 "PK\u0001\u0002Local File Header\n" +
 "2 0 01-01-01 01:01:01.01 0000400000001054Name2.txt\u0000\u0000" +
 "0 0 Name Length\n" +
 "0 0 Extra Field Length\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 File comment length\n" +
 "0 0 File comment length in extra field\n" +
 "0 0 Disc number starting file in this disk\n" +
 "0 0 Internal file attributes\n" +
 "0 0 External file attributes\n" +
 "0 0 Local header signature\n" +
 "PK\u0005Data Descriptor\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 Local header offset\n" +
 "0 0 Data descriptor signature\n" +
 "0 0 Compression method\n" +
 "0 0 Ways to compress the data\n" +
 "0 0 File comment length\n" +
 "Name2.txt\u0000\u0000\n" +
 "PK\u0005Data Descriptor\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 Local header offset\n" +
 "0 0 Data descriptor signature\n" +
 "0 0 Compression method\n" +
 "0 0 Ways to compress the data\n" +
 "0 0 File comment length\n" +
 "Name1.txt\u0000\u0000\n" +
 "PK\u0003\u0005End of Central Directory Record\n" +
 "0 0 Size this disk\n" +
 "0 0 Number of disk with entries\n" +
 "0 0 Total number of entries in the central directory on this disk\n" +
 "0 0 Total number of entries in the central directory\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory\n" +
 "PK\u0007End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "PK\u0006End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "0 0 Size of the Central Directory\n" +
 "PK\u0005End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "0 0 Size of the Central Directory\n" +
 "PK\u0005End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature"));
 byte[] buffer = new byte[10];
 byte[] expected = "Name1.txt".getBytes();
 int len = expected.length;
 int read = 0;
 do {
 read = zipArchiveInputStream.read(buffer, 0, buffer.length);
 if (read > 0) {
 assertArrayEquals("Unexpected data", expected, Arrays.copyOfRange(buffer, 0, read));
 len -= read;
 expected = Arrays.copyOfRange(expected, len, expected.length);
 }
 } while (len > 0);
 zipArchiveInputStream.close();
 }

 @Test(expected = IOException.class)
 public void testReadAfterEndOfStream() throws Exception {
 ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(
 new ByteArrayInputStream(
 "PK\u0003\u0004PK\u0007\u0001Local File Header\n" +
 "2 0 01-01-01 01:01:01.01 0000400000001054Namel.txt\u0000\u0000" +
 "PK\u0006\u0002Central Directory\n" +
 "12 0 Central Directory File Descriptor\n" +
 "18 0 End of Central Directory Record\n" +
 "0 0 Size of this disk\n" +
 "0 0 Number of disk with entries\n" +
 "0 0 Total number of entries\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory\n" +
 "PK\u0001\u0002Local File Header\n" +
 "2 0 01-01-01 01:01:01.01 0000400000001054Name2.txt\u0000\u0000" +
 "0 0 Name Length\n" +
 "0 0 Extra Field Length\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 File comment length\n" +
 "0 0 File comment length in extra field\n" +
 "0 0 Disc number starting file in this disk\n" +
 "0 0 Internal file attributes\n" +
 "0 0 External file attributes\n" +
 "0 0 Local header signature\n" +
 "PK\u0005Data Descriptor\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 Local header offset\n" +
 "0 0 Data descriptor signature\n" +
 "0 0 Compression method\n" +
 "0 0 Ways to compress the data\n" +
 "0 0 File comment length\n" +
 "Name2.txt\u0000\u0000\n" +
 "PK\u0005Data Descriptor\n" +
 "0 0 CRC-32 of uncompressed data\n" +
 "0 0 Compressed size of local file header\n" +
 "0 0 Uncompressed size of local file header\n" +
 "0 0 Local header offset\n" +
 "0 0 Data descriptor signature\n" +
 "0 0 Compression method\n" +
 "0 0 Ways to compress the data\n" +
 "0 0 File comment length\n" +
 "Name1.txt\u0000\u0000\n" +
 "PK\u0003\u0005End of Central Directory Record\n" +
 "0 0 Size this disk\n" +
 "0 0 Number of disk with entries\n" +
 "0 0 Total number of entries in the central directory on this disk\n" +
 "0 0 Total number of entries in the central directory\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory\n" +
 "PK\u0007End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "PK\u0006End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "0 0 Size of the Central Directory\n" +
 "PK\u0005End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "0 0 Size of the Central Directory\n" +
 "PK\u0005End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "0 0 Size of the Central Directory\n" +
 "PK\u0005End of Central Directory Record\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 Offset of Start of Central Directory on this disk\n" +
 "0 0 Size of the Central Directory\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature\n" +
 "0 0 End of Central Directory Signature"));
 byte[] buffer = new byte[10];
 int read = zipArchiveInputStream.read(buffer, 0, buffer.length);
 fail();
 }
}