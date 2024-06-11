import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class ZipArchiveInputStreamTest {

    @Test
    public void testGetNextZipEntry() throws Exception {
        try (ZipArchiveInputStream zipInput = new ZipArchiveInputStream(ZipArchiveInputStreamTest.class.getResourceAsStream("/test.zip"))) {
            ZipArchiveEntry entry1 = zipInput.getNextZipEntry();
            Assert.assertNotNull(entry1);
            Assert.assertEquals("file1.txt", entry1.getName());

            ZipArchiveEntry entry2 = zipInput.getNextZipEntry();
            Assert.assertNotNull(entry2);
            Assert.assertEquals("file2.txt", entry2.getName());

            // Edge case: No more entries
            ZipArchiveEntry entry3 = zipInput.getNextZipEntry();
            Assert.assertNull(entry3);
        }
    }

    @Test
    public void testRead() throws Exception {
        try (ZipArchiveInputStream zipInput = new ZipArchiveInputStream(ZipArchiveInputStreamTest.class.getResourceAsStream("/test.zip"))) {
            zipInput.getNextZipEntry();
            byte[] buffer = new byte[10];
            int bytesRead = zipInput.read(buffer, 0, buffer.length);
            Assert.assertEquals(10, bytesRead);
            // Validate read data

            // Edge case: Buffer is larger than entry size
            zipInput.getNextZipEntry();
            byte[] largeBuffer = new byte[1000];
            bytesRead = zipInput.read(largeBuffer, 0, largeBuffer.length);
            Assert.assertEquals(20, bytesRead);
            // Validate read data

            // Edge case: Entry size is larger than buffer
            zipInput.getNextZipEntry();
            byte[] smallBuffer = new byte[5];
            bytesRead = zipInput.read(smallBuffer, 0, smallBuffer.length);
            Assert.assertEquals(5, bytesRead);
            // Validate read data

            // Exception handling: Reading after end of entry
            int extraBytesRead = zipInput.read(buffer, 0, buffer.length);
            Assert.assertEquals(-1, extraBytesRead);
        }
    }

    @Test
    public void testClose() throws Exception {
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(ZipArchiveInputStreamTest.class.getResourceAsStream("/test.zip"));
        zipInput.close();
        // Exception handling: Reading after close
        int bytesRead = zipInput.read(new byte[10], 0, 10);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testSkip() throws Exception {
        try (ZipArchiveInputStream zipInput = new ZipArchiveInputStream(ZipArchiveInputStreamTest.class.getResourceAsStream("/test.zip"))) {
            zipInput.getNextZipEntry();

            long skipped = zipInput.skip(10);
            Assert.assertEquals(10, skipped);

            // Edge case: Skipping more than entry size
            skipped = zipInput.skip(1000);
            Assert.assertEquals(20, skipped);
        }
    }

    @Test
    public void testMatches() throws Exception {
        byte[] signature1 = new byte[] { 0x50, 0x4B, 0x03, 0x04 };
        boolean matches1 = ZipArchiveInputStream.matches(signature1, signature1.length);
        Assert.assertTrue(matches1);

        byte[] signature2 = new byte[] { 0x50, 0x4B, 0x01, 0x02 };
        boolean matches2 = ZipArchiveInputStream.matches(signature2, signature2.length);
        Assert.assertFalse(matches2);
    }
    
}