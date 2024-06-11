import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws Exception {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        String archiverName = "zip";
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnsupportedArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        String unsupportedArchiverName = "unsupported";
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(unsupportedArchiverName, inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: " + unsupportedArchiverName + " not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected ArchiveException
        } finally {
            inputStream.close();
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws Exception {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
        assertEquals(0, outputStream.size());
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        String archiverName = "zip";
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnsupportedArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        String unsupportedArchiverName = "unsupported";
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(unsupportedArchiverName, outputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: " + unsupportedArchiverName + " not found.", e.getMessage());
        }
    }
}