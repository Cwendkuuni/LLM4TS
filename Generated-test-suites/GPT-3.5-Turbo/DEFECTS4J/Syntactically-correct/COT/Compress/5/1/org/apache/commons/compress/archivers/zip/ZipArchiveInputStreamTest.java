import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setup() throws IOException {
        byte[] zipData = generateZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void getNextZipEntry_ReturnsNextEntry() throws IOException {
        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals("file1.txt", entry.getName());
    }

    @Test
    public void getNextZipEntry_ReturnsNullAfterLastEntry() throws IOException {
        // Read all entries
        while (zipArchiveInputStream.getNextZipEntry() != null) { }

        ArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNull(entry);
    }

    @Test
    public void read_ReadsCorrectNumberOfBytes() throws IOException {
        zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
    }

    @Test
    public void read_ReturnsNegativeOneAtEndOfEntry() throws IOException {
        zipArchiveInputStream.getNextZipEntry();
        byte[] buffer = new byte[10];
        // Read all bytes in the entry
        while (zipArchiveInputStream.read(buffer) != -1) { }

        int bytesRead = zipArchiveInputStream.read(buffer, 0, 10);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void close_ClosesInputStream() throws IOException {
        zipArchiveInputStream.close();
        try {
            zipArchiveInputStream.getNextZipEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            // expected exception
        }
    }

    private byte[] generateZipData() throws IOException {
        // Generate a simple zip file with a single entry
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(baos);
        ZipArchiveEntry entry = new ZipArchiveEntry("file1.txt");
        zipOutput.putArchiveEntry(entry);
        zipOutput.write(buffer);
        zipOutput.closeArchiveEntry();
        zipOutput.close();
        return baos.toByteArray();
    }

}