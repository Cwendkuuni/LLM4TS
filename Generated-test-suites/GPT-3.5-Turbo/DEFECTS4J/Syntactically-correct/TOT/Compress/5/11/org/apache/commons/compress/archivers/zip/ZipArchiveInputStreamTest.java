import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setup() {
        byte[] zipData = generateZipFile();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipData);
        zipInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNotNull(entry);

        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());

        ArchiveEntry directoryEntry = zipInputStream.getNextZipEntry();
        assertNotNull(directoryEntry);
        assertEquals("documents/", directoryEntry.getName());
        assertTrue(directoryEntry.isDirectory());

        ArchiveEntry emptyDirectoryEntry = zipInputStream.getNextZipEntry();
        assertNotNull(emptyDirectoryEntry);
        assertEquals("emptyDirectory/", emptyDirectoryEntry.getName());
        assertTrue(emptyDirectoryEntry.isDirectory());

        ArchiveEntry nullEntry = zipInputStream.getNextZipEntry();
        assertNull(nullEntry);
    }

    @Test
    public void testRead() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[1024];
        int bytes = zipInputStream.read(buffer);
        String content = new String(buffer, 0, bytes);

        assertEquals("This is a test file.", content);

        zipInputStream.closeEntry();

        byte[] buffer2 = new byte[1024];
        int bytes2 = zipInputStream.read(buffer2);
        assertEquals(-1, bytes2);
    }

    @Test
    public void testClose() throws IOException {
        zipInputStream.close();

        // Try to read after closing the stream should throw IOException
        try {
            zipInputStream.getNextZipEntry();
            fail("IOException not thrown after closing the stream");
        } catch (IOException e) {
            assertEquals("The stream is closed", e.getMessage());
        }
    }

    private byte[] generateZipFile() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            ZipOutputStream zos = new ZipOutputStream(baos)) {

            // Create a test file
            ZipEntry entry = new ZipEntry("test.txt");
            zos.putNextEntry(entry);
            zos.write("This is a test file.".getBytes());
            zos.closeEntry();

            // Create a test directory
            ZipEntry directoryEntry = new ZipEntry("documents/");
            zos.putNextEntry(directoryEntry);
            zos.closeEntry();

            // Create an empty directory
            ZipEntry emptyDirectoryEntry = new ZipEntry("emptyDirectory/");
            zos.putNextEntry(emptyDirectoryEntry);
            zos.closeEntry();

            zos.finish();
            zos.close();
            return baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}