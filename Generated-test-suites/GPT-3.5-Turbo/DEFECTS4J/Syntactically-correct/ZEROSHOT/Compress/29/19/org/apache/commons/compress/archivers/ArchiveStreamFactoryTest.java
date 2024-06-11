import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null, null);
            fail("An IllegalArgumentException should have been thrown for null archiverName and null input stream.");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("An IllegalArgumentException should have been thrown for null archiverName and null input stream.");
        }

        try {
            factory.createArchiveInputStream("ar", null);
            fail("An IllegalArgumentException should have been thrown for null input stream.");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("An IllegalArgumentException should have been thrown for null input stream.");
        }

        try {
            factory.createArchiveInputStream("test", null);
            fail("An ArchiveException should have been thrown for unknown archiver name.");
        } catch (ArchiveException e) {
            assertEquals("Archiver: test not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, null);
            fail("An IllegalArgumentException should have been thrown for null archiverName and null output stream.");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("An IllegalArgumentException should have been thrown for null archiverName and null output stream.");
        }

        try {
            factory.createArchiveOutputStream("ar", null);
            fail("An IllegalArgumentException should have been thrown for null output stream.");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("An IllegalArgumentException should have been thrown for null output stream.");
        }

        try {
            factory.createArchiveOutputStream("test", null);
            fail("An ArchiveException should have been thrown for unknown archiver name.");
        } catch (ArchiveException e) {
            assertEquals("Archiver: test not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null);
            fail("An IllegalArgumentException should have been thrown for null input stream.");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("An IllegalArgumentException should have been thrown for null input stream.");
        }

        try {
            byte[] data = new byte[]{80, 75, 3, 4}; // ZIP signature
            InputStream in = new ByteArrayInputStream(data);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail("No exception should have been thrown for ZIP signature.");
        }

        try {
            byte[] data = new byte[]{80, 75, 5, 6}; // Unknown signature
            InputStream in = new ByteArrayInputStream(data);
            factory.createArchiveInputStream(in);
            fail("An ArchiveException should have been thrown for unknown signature.");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

}