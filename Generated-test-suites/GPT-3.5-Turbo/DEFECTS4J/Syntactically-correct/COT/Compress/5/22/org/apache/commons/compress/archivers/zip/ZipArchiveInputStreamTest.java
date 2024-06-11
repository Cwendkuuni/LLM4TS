
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private final byte[] zipData = {
        0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0xB7, 0x1C,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x1C, 0x00, 0x00, 0x00, 0x74, 0x65, 0x73, 0x74,
        0x2E, 0x74, 0x78, 0x74, 0x15, 0x24, 0x5B, 0xFE, 0x06, 0x00, 0x26, 0x85,
        0x36, 0x1B, 0x85, 0x36, 0x1B, 0x26, 0x85, 0x36, 0x1B, 0x85, 0x36, 0x1B,
        0x26, 0x85, 0x36, 0x1B, 0x85, 0x36, 0x1B, 0x26, 0x85, 0x36, 0x1B, 0x85,
        0x36, 0x1B, 0x26, 0x85, 0x36, 0x1B, 0x85, 0x36, 0x1B, 0x26, 0x85, 0x36,
        0x1B, 0x85, 0x36, 0x1B, 0x0B, 0x00, 0x01, 0x04, 0xE8, 0x03, 0xB8, 0x39,
        0x49, 0x2C, 0x00, 0x00, 0x00, 0x00
    };

    private ByteArrayInputStream inputStream;

    @Before
    public void setup() {
        inputStream = new ByteArrayInputStream(zipData);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Assert that the first entry is correct
        ZipEntry entry1 = zipInputStream.getNextZipEntry();
        assertEquals("test.txt", entry1.getName());
        assertFalse(entry1.isDirectory());

        // Assert that there are no more entries
        ZipEntry entry2 = zipInputStream.getNextZipEntry();
        assertNull(entry2);
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Read the entry's content
        byte[] content = new byte[30];
        int bytesRead = zipInputStream.read(content);

        // Assert that the content is correct
        assertEquals(30, bytesRead);
        assertEquals("This is a test file.", new String(content));
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);
        zipInputStream.close();

        // Try to read from closed stream
        zipInputStream.read();
    }

    @Test
    public void testSkip() throws IOException {
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Skip the entry's content
        long skipped = zipInputStream.skip(30);

        // Assert that the correct number of bytes was skipped
        assertEquals(30, skipped);
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x04};
        assertTrue(ZipArchiveInputStream.matches(signature, 4));

        byte[] invalidSignature = {0x00, 0x00, 0x00, 0x00};
        assertFalse(ZipArchiveInputStream.matches(invalidSignature, 4));
    }
}
