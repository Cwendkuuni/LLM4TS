import org.junit.*;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry() throws Exception {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry entry = zipStream.getNextZipEntry();

        assertNotNull("First entry should not be null", entry);
        assertEquals("First entry name should be test1.txt", "test1.txt", entry.getName());
        assertEquals("First entry method should be STORED", 0, entry.getMethod());

        entry = zipStream.getNextZipEntry();

        assertNotNull("Second entry should not be null", entry);
        assertEquals("Second entry name should be test2.txt", "test2.txt", entry.getName());
        assertEquals("Second entry method should be STORED", 0, entry.getMethod());

        entry = zipStream.getNextZipEntry();

        assertNull("There should be no more entries", entry);
    }

    @Test
    public void testRead() throws Exception {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry entry = zipStream.getNextZipEntry();

        assertNotNull("First entry should not be null", entry);
        byte[] buffer = new byte[1024];
        int read = zipStream.read(buffer);
        assertEquals("Read data should be equal to entry size", entry.getSize(), read);

        entry = zipStream.getNextZipEntry();

        assertNotNull("Second entry should not be null", entry);
        read = zipStream.read(buffer);
        assertEquals("Read data should be equal to entry size", entry.getSize(), read);

        entry = zipStream.getNextZipEntry();

        assertNull("There should be no more entries", entry);
    }

    @Test
    public void testClose() throws Exception {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(inputStream);

        zipStream.close();

        try {
            ZipArchiveEntry entry = zipStream.getNextZipEntry();
            fail("Calling getNextZipEntry() after closing the stream should throw an IOException");
        } catch (IOException e) {
            assertEquals("Exception message should be The stream is closed", "The stream is closed", e.getMessage());
        }

        try {
            byte[] buffer = new byte[1024];
            int read = zipStream.read(buffer);
            fail("Calling read() after closing the stream should throw an IOException");
        } catch (IOException e) {
            assertEquals("Exception message should be The stream is closed", "The stream is closed", e.getMessage());
        }
    }

    @Test
    public void testSkip() throws Exception {
        InputStream inputStream = new FileInputStream("test.zip");
        ZipArchiveInputStream zipStream = new ZipArchiveInputStream(inputStream);

        ZipArchiveEntry entry = zipStream.getNextZipEntry();

        assertNotNull("First entry should not be null", entry);
        long skipped = zipStream.skip(10);
        assertEquals("Skipped bytes should be equal to the specified value", 10, skipped);

        entry = zipStream.getNextZipEntry();

        assertNotNull("Second entry should not be null", entry);
        skipped = zipStream.skip(20);
        assertEquals("Skipped bytes should be equal to the specified value", 20, skipped);

        entry = zipStream.getNextZipEntry();

        assertNull("There should be no more entries", entry);
    }

    @Test
    public void testMatches() throws Exception {
        byte[] signature = new byte[]{0x50, 0x4B, 0x03, 0x04};

        assertTrue("Signature should match", ZipArchiveInputStream.matches(signature, 4));

        signature = new byte[]{0x50, 0x4B, 0x01, 0x02};

        assertFalse("Signature should not match", ZipArchiveInputStream.matches(signature, 4));
    }
}