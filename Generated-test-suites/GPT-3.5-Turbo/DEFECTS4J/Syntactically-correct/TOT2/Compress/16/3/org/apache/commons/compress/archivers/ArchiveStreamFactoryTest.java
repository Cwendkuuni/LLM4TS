import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidInput() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", inputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        try {
            archiveStreamFactory.createArchiveInputStream(null, inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidInput() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("ar", outputStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        try {
            archiveStreamFactory.createArchiveOutputStream("cpio", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    // Test cases for createArchiveInputStream(InputStream in) can be added similarly

    @Test
    public void testCreateArchiveInputStream_AutoDetectZip() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        byte[] zipHeader = new byte[]{0x50, 0x4B, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipHeader);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectUnknown() throws IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        try {
            archiveStreamFactory.createArchiveInputStream(inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

    // Add more test cases for edge cases and exception handling scenarios

}