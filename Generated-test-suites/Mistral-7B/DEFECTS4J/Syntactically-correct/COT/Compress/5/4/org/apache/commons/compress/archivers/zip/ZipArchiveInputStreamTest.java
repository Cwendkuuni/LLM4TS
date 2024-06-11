import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntryNormalCase() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryClosedStream() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryNullEntry() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testGetNextZipEntryEndOfCentralDirectory() throws IOException {
        String testFile = "test.zip";
        InputStream inputStream = ZipFileTestUtil.getInputStreamForTest(testFile);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = null;
        while ((entry = zipArchiveInputStream.getNextZipEntry()) != null) {
            // do something with the entry
        }
        Assert.assertNull(entry);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryInvalidLocalFileHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x05, (byte) 0x06 // Local File Header Signature
                , (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Version Made By
                , (byte) 0x00, (byte) 0x02, (byte) 0x08, (byte) 0x00 // General Purpose Bit Flag
                , (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00 // Compression Method
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Last Mod File Time
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Last Mod File Date
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // CRC-32
                , (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF // Compressed Size
                , (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF // Uncompressed Size
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // File Name Length
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Extra Field Length
                });
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryInvalidCentralDirectory() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x05, (byte) 0x06 // Central Directory Signature
                , (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Central Dir. CDisk Number
                , (byte) 0x00, (byte) 0x02, (byte) 0x08, (byte) 0x00 // Central Dir. CDisk Number of Entries
                , (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00 // Central Dir. CTotal Number of Disks
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Central Dir. CDisk Location of Start of Central Dir.
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Central Dir. CDisk Location of Start of Central Dir. File Entry
                });
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test(expected = DataFormatException.class)
    public void testReadBadData() throws IOException, DataFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x01}); // Single byte read
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        byte[] buffer = new byte[1];
        zipArchiveInputStream.read(buffer, 0, 1);
    }

    @Test(expected = ZipException.class)
    public void testReadDataDescriptors() throws IOException, DataFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x05, (byte) 0x06 // Local File Header Signature
                , (byte) 0x08, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Version Made By
                , (byte) 0x00, (byte) 0x02, (byte) 0x08, (byte) 0x00 // General Purpose Bit Flag
                , (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00 // Compression Method
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Last Mod File Time
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Last Mod File Date
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // CRC-32
                , (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF // Compressed Size
                , (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF // Uncompressed Size
                , (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00 // File Name Length
                , (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Extra Field Length
                , (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Comment Length
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // File Name
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Extra Field
                , (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Comment
                , (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00 // Data Descriptor Signature
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // CRC-32 of Central Dir. File Header
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // CRC-32 of Local File Header
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Compressed Size of Local File Header
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Uncompressed Size of Local File Header
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Local File Header offset in Central Dir.
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Comment Length
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 // Data Size
                });
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();

        byte[] buffer = new byte[1];
        zipArchiveInputStream.read(buffer, 0, 1);
    }
}