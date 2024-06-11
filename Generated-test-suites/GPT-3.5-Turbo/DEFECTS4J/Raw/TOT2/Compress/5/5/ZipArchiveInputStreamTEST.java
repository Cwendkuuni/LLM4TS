import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setup() {
        // Create a sample zip file to use in tests
        byte[] zipFileData = createSampleZipFile();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipFileData);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    private byte[] createSampleZipFile() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream out = new ZipOutputStream(bos);

            // Add a file to the zip
            ZipEntry entry = new ZipEntry("testFile.txt");
            byte[] fileContent = "This is a test file content".getBytes();
            entry.setSize(fileContent.length);
            out.putNextEntry(entry);
            out.write(fileContent);
            out.closeEntry();

            out.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(23, entry.getSize());
        assertEquals(0, entry.getMethod());
    }

    @Test
    public void testRead() throws IOException {
        zipArchiveInputStream.getNextZipEntry(); // Read the first entry
        byte[] buffer = new byte[100];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 100);
        assertEquals(23, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        assertTrue(zipArchiveInputStream.isClosed());
    }

    @Test
    public void testSkip() throws IOException {
        zipArchiveInputStream.getNextZipEntry(); // Read the first entry

        long skipped = zipArchiveInputStream.skip(10);
        assertEquals(10, skipped);

        byte[] buffer = new byte[100];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 100);
        assertEquals(13, bytesRead); // Remaining bytes after skipping 10 bytes
    }

    @Test
    public void testMatches() {
        byte[] signature = {80, 75, 3, 4}; // LFH_SIG
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));

        byte[] eocdSignature = {80, 75, 5, 6}; // EOCD_SIG
        assertFalse(ZipArchiveInputStream.matches(eocdSignature, eocdSignature.length));
    }

}