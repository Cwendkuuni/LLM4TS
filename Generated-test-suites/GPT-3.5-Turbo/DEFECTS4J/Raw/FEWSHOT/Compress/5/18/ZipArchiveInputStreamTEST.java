import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    private ByteArrayInputStream testInput;

    @Before
    public void setup() {
        // setup test input
        // create a zip archive with a single file
        byte[] zipData = new byte[] {
            0x50, 0x4b, 0x03, 0x04, 0x0a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x0a, 0x00, 0x00, 0x00,
            0x2e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x06, 0x00, 0x00, 0x00, 0x66, 0x6f, 0x6f, 0x2e, 0x74, 0x78, 0x74, 0x00,
            (byte) 0xac, (byte) 0xe5, 0x0e, 0x92, 0x31, (byte) 0xa0, (byte) 0xeb, 0x46,
            0x2c, 0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00
        };
        testInput = new ByteArrayInputStream(zipData);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(testInput);
        ZipArchiveEntry entry = zipInput.getNextZipEntry();
        assertEquals("foo.txt", entry.getName());
        assertEquals(10, entry.getSize());
        assertEquals(10, entry.getCompressedSize());
        assertEquals(1, entry.getMethod());
        assertEquals(1621780116442L, entry.getTime());
        assertEquals(0, entry.getCrc());
        zipInput.close();
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(testInput);
        zipInput.getNextZipEntry();
        byte[] buffer = new byte[4];
        int bytesRead = zipInput.read(buffer);
        assertEquals(4, bytesRead);
        assertEquals('f', (char) buffer[0]);
        assertEquals('o', (char) buffer[1]);
        assertEquals('o', (char) buffer[2]);
        assertEquals('.', (char) buffer[3]);
        zipInput.close();
    }

    @Test
    public void testClose() throws IOException {
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(testInput);
        zipInput.getNextZipEntry();
        zipInput.close();
    }

    @Test
    public void testSkip() throws IOException {
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(testInput);
        zipInput.getNextZipEntry();
        long skipped = zipInput.skip(2);
        assertEquals(2, skipped);
        zipInput.close();
    }
}