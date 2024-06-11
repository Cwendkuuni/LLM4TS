import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry() {
        byte[] zipEntryData = {
            0x50, 0x4b, 0x03, 0x04, // local file header signature
            0x0A, 0x00, // version needed to extract
            0x08, 0x00, // general purpose bit flag
            0x08, 0x00, // compression method
            0x00, 0x00, // last mod file time
            0x00, 0x00, // last mod file date
            0x00, 0x00, 0x00, 0x00, // crc-32
            0x00, 0x00, 0x00, 0x00, // compressed size
            0x00, 0x00, 0x00, 0x00, // uncompressed size
            0x00, 0x04, // file name length
            0x00, 0x00, // extra field length
            't', 'e', 's', 't' // file name
        };
        ByteArrayInputStream zipEntryStream = new ByteArrayInputStream(zipEntryData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipEntryStream);

        try {
            ZipArchiveEntry zipEntry = zipArchiveInputStream.getNextZipEntry();
            assertNotNull(zipEntry);
            assertEquals("test", zipEntry.getName());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testRead() {
        byte[] zipEntryData = {
            0x50, 0x4b, 0x03, 0x04, // local file header signature
            0x08, 0x00, // version needed to extract
            0x08, 0x00, // general purpose bit flag
            0x08, 0x00, // compression method
            0x00, 0x00, // last mod file time
            0x00, 0x00, // last mod file date
            0x00, 0x00, 0x00, 0x00, // crc-32
            0x00, 0x00, 0x00, 0x00, // compressed size
            0x00, 0x00, 0x00, 0x00, // uncompressed size
            0x00, 0x04, // file name length
            0x00, 0x00, // extra field length
            't', 'e', 's', 't' // file name
        };
        ByteArrayInputStream zipEntryStream = new ByteArrayInputStream(zipEntryData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipEntryStream);

        byte[] buffer = new byte[16]; // Buffer size is 16
        try {
            int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
            assertEquals(4, bytesRead);
            assertEquals("test", new String(buffer, 0, bytesRead));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testClose() {
        byte[] zipEntryData = {
            0x50, 0x4b, 0x03, 0x04, // local file header signature
            0x08, 0x00, // version needed to extract
            0x08, 0x00, // general purpose bit flag
            0x08, 0x00, // compression method
            0x00, 0x00, // last mod file time
            0x00, 0x00, // last mod file date
            0x00, 0x00, 0x00, 0x00, // crc-32
            0x00, 0x00, 0x00, 0x00, // compressed size
            0x00, 0x00, 0x00, 0x00, // uncompressed size
            0x00, 0x04, // file name length
            0x00, 0x00, // extra field length
            't', 'e', 's', 't' // file name
        };
        ByteArrayInputStream zipEntryStream = new ByteArrayInputStream(zipEntryData);
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(zipEntryStream);

        try {
            zipArchiveInputStream.close();
            zipArchiveInputStream.getNextZipEntry(); // Attempt to get next zip entry after closing
            fail("IOException should be thrown after closing the stream");
        } catch (IOException e) {
            // Expected IOException
        }
    }
}