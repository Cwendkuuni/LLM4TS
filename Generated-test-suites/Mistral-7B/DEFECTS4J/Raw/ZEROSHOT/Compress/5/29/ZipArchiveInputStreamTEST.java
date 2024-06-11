import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.ZipLong;
import org.apache.commons.compress.utils.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;
    private ZipFile zipFile;

    @BeforeEach
    public void setUp() throws IOException {
        byte[] testData = new byte[] {
                0x04, 0x03, 0x4b, 0x50, // Local File Header signature
                0x02, 0x01, 0x08, 0x00, 0x00, // Version made by, platform
                0x04, 0x05, 0x06, 0x07, // General Purpose Bit Flag
                0x00, 0x02, // Compression Method
                0x4c, 0x49, 0x4e, 0x42, 0x53, 0x53, 0x45, 0x54, // Last Mod File Time
                0x00, 0x00, 0x00, 0x01, // Last Mod File Date
                0x3c, 0x2d, 0x3e, 0x34, // CRC-32
                0x00, 0x00, 0x00, 0x03, 0x04, 0x05, 0x06, 0x07, // Compressed Size
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, // Uncompressed Size
                0x06, 0x00, // File Name Length
                0x04, 0x00, // Extra Field Length
                0x54, 0x45, 0x53, 0x53, 0x55, 0x4e, 0x44, 0x41, // File Name
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // Extra Field
                0x4d, 0x54, 0x68, 0x65, 0x78, 0x2d, 0x63, 0x72, // Signature for this test
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x00, 0x00, // Signature for this test (continued)
                0x01 // End of Central Directory signature
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(testData, 0, testData.length);
        byte[] zipFileData = baos.toByteArray();

        zipFile = new ZipFile(new ByteArrayInputStream(zipFileData));
        zipArchiveInputStream = new ZipArchiveInputStream(zipFile.getInputStream(new ZipEntry("test.zip")));
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assertions.assertNotNull(entry);
        Assertions.assertEquals("test.zip", entry.getName());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ZipEntry entry = zipArchiveInputStream.getNextEntry();
        Assertions.assertNotNull(entry);
        Assertions.assertEquals("test.zip", entry.getName());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assertions.assertEquals(bytesRead, 6);
        Assertions.assertArrayEquals(new byte[]{0x4d, 0x54, 0x68, 0x65, 0x78}, buffer, 0, 5);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        Assertions.assertThrows(IOException.class, zipArchiveInputStream::getNextZipEntry);
    }

    @Test
    public void testMatchesSignature() throws IOException {
        Assertions.assertTrue(ZipArchiveInputStream.matches(new byte[]{0x04, 0x03, 0x4b, 0x50}, ZipArchiveOutputStream.LFH_SIG.length));
        Assertions.assertTrue(ZipArchiveInputStream.matches(new byte[]{0x04, 0x03, 0x4b, 0x50, 0x01}, ZipArchiveOutputStream.EOCD_SIG.length + ZipArchiveOutputStream.LFH_SIG.length));
        Assertions.assertFalse(ZipArchiveInputStream.matches(new byte[]{0x04, 0x03, 0x4b, 0x51}, ZipArchiveOutputStream.LFH_SIG.length));
    }
}