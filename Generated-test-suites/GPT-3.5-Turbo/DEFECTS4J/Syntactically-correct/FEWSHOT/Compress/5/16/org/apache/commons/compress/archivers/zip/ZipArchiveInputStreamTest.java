import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setUp() {
        byte[] data = createZipData();
        zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(data));
    }

    @After
    public void tearDown() throws IOException {
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ArchiveEntry entry1 = zipArchiveInputStream.getNextZipEntry();
        assertZipEntry(entry1, "file1.txt", 10, 10);

        ArchiveEntry entry2 = zipArchiveInputStream.getNextZipEntry();
        assertZipEntry(entry2, "file2.txt", 20, 20);

        ArchiveEntry entry3 = zipArchiveInputStream.getNextZipEntry();
        assertZipEntry(entry3, "file3.txt", 30, 30);

        ArchiveEntry entry4 = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry4);
    }

    @Test
    public void testRead() throws IOException {
        zipArchiveInputStream.getNextZipEntry();

        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
        assertArrayEquals("File1 data".getBytes(), buffer);

        bytesRead = zipArchiveInputStream.read(buffer, 0, 10);
        assertEquals(0, bytesRead);
    }

    @Test
    public void testSkip() throws IOException {
        zipArchiveInputStream.getNextZipEntry();

        long skipped = zipArchiveInputStream.skip(5);
        assertEquals(5, skipped);

        byte[] buffer = new byte[5];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertArrayEquals("data".getBytes(), buffer);

        skipped = zipArchiveInputStream.skip(5);
        assertEquals(0, skipped);

        bytesRead = zipArchiveInputStream.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertArrayEquals("File1".getBytes(), buffer);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    private byte[] createZipData() {
        // TODO: Create a byte array containing a valid ZIP file data

        return new byte[0];
    }

    private void assertZipEntry(ArchiveEntry entry, String name, long size, int compressedSize) {
        assertEquals(name, entry.getName());
        assertEquals(size, entry.getSize());
        assertEquals(compressedSize, entry.getCompressedSize());

        if (entry instanceof ZipArchiveEntry) {
            ZipArchiveEntry zipEntry = (ZipArchiveEntry) entry;
            // TODO: Add assertions for additional properties of ZipArchiveEntry
        }
    }
}