import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new FileInputStream("test.zip");
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        // Test case for valid entry
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("file1.txt", entry.getName());

        // Test case for no more entries
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testRead() throws IOException {
        // Test case for reading bytes from entry
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        byte[] expectedData = "This is file1.txt".getBytes();
        byte[] actualData = new byte[expectedData.length];
        int bytesRead = zipArchiveInputStream.read(actualData, 0, actualData.length);
        assertEquals(expectedData.length, bytesRead);
        assertArrayEquals(expectedData, actualData);

        // Test case for no more data to read
        assertEquals(-1, zipArchiveInputStream.read(actualData, 0, actualData.length));

        // Test case for reading bytes from compressed entry
        entry = zipArchiveInputStream.getNextZipEntry();
        expectedData = "This is a compressed file".getBytes();
        actualData = new byte[expectedData.length];
        bytesRead = zipArchiveInputStream.read(actualData, 0, actualData.length);
        assertEquals(expectedData.length, bytesRead);
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        try {
            // Attempt to read after closing should throw IOException
            zipArchiveInputStream.getNextZipEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected behavior
        }
    }

    @Test
    public void testSkip() throws IOException {
        // Skip past the first entry
        zipArchiveInputStream.skip(16);

        // Check if next entry is correct
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertEquals("file2.txt", entry.getNmae());
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] {0x50, 0x4B, 0x03, 0x04};

        // Test matching signature for normal file
        assertTrue(ZipArchiveInputStream.matches(signature, 4));

        // Test matching signature for empty zip
        signature = new byte[] {0x50, 0x4B, 0x05, 0x06};
        assertTrue(ZipArchiveInputStream.matches(signature, 4));

        // Test non-matching signature
        signature = new byte[] {0x00, 0x00, 0x00, 0x00};
        assertFalse(ZipArchiveInputStream.matches(signature, 4));
    }

}