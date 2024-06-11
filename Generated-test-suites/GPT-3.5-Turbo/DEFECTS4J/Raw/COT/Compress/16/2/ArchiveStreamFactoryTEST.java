import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(archiveIn);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(archiveOut);
    }

    @Test
    public void testCreateArchiveInputStream_withNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_withNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_withNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_withNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_withNullStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_noArchiverFound() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(in);
            fail("Expected ArchiveException was not thrown.");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testCreateArchiveInputStream_autodetect_invalidSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[ArchiveInputStream.SIGNATURE_LENGTH]);
        try {
            factory.createArchiveInputStream(in);
            fail("Expected ArchiveException was not thrown.");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_dumpArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { '\037', 'D', 'U', 'M', 'P' });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        // Verify that the correct archive type is detected
        assertTrue(archiveIn instanceof org.apache.commons.compress.archivers.dump.DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_tarArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { '\000', 'u', 's', 't', 'a', 'r' });
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        // Verify that the correct archive type is detected
        assertTrue(archiveIn instanceof org.apache.commons.compress.archivers.tar.TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_tarArchive_with512ByteSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[512]);
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
        assertNotNull(archiveIn);
        // Verify that the correct archive type is detected
        assertTrue(archiveIn instanceof org.apache.commons.compress.archivers.tar.TarArchiveInputStream);
    }
}