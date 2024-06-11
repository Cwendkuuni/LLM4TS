import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class ZipArchiveInputStreamJUnitTest {

    private ZipArchiveInputStream zipInputStream;

    private static final String FILE_NAME = "test.zip";
    private static final String ENTRY_NAME = "test.txt";
    private static final byte[] ENTRY_CONTENT = "Hello, World!".getBytes();

    @Before
    public void setup() throws IOException {
        // Create a zip file with a single entry
        FileOutputStream fos = new FileOutputStream(FILE_NAME);
        ZipOutputStream zos = new ZipOutputStream(fos);
        ZipEntry entry = new ZipEntry(ENTRY_NAME);
        zos.putNextEntry(entry);
        zos.write(ENTRY_CONTENT);
        zos.closeEntry();
        zos.close();

        // Create the ZipArchiveInputStream for testing
        FileInputStream fis = new FileInputStream(FILE_NAME);
        zipInputStream = new ZipArchiveInputStream(fis);
    }

    @After
    public void cleanup() throws IOException {
        // Delete the zip file
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }

        // Close the ZipArchiveInputStream
        zipInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertEquals(ENTRY_NAME, entry.getName());
        assertEquals(ENTRY_CONTENT.length, entry.getSize());
    }

    @Test
    public void testRead() throws IOException {
        zipInputStream.getNextZipEntry();
        byte[] buffer = new byte[ENTRY_CONTENT.length];
        int bytes = zipInputStream.read(buffer);
        assertEquals(ENTRY_CONTENT.length, bytes);
        assertEquals(new String(ENTRY_CONTENT), new String(buffer));
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        zipInputStream.getNextZipEntry();
        byte[] buffer = new byte[ENTRY_CONTENT.length];
        int bytes = zipInputStream.read(buffer, 0, 5);
        assertEquals(5, bytes);
        assertEquals(new String(ENTRY_CONTENT, 0, 5), new String(buffer, 0, 5));
    }

    @Test
    public void testClose() throws IOException {
        zipInputStream.close();
        zipInputStream.getNextZipEntry();
        assertNull(zipInputStream.getNextZipEntry());
    }

    @Test
    public void testSkip() throws IOException {
        zipInputStream.getNextZipEntry();
        long skipped = zipInputStream.skip(5);
        assertEquals(5, skipped);
        byte[] buffer = new byte[1];
        int bytesRead = zipInputStream.read(buffer);
        assertEquals(1, bytesRead);
        assertEquals(ENTRY_CONTENT[5], buffer[0]);
    }
}