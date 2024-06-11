import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Typical use case - ZIP
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        // Edge case - Empty archiver name
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("", in);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }

        // Edge case - Null input stream
        try {
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", null);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Error scenario - Unknown archiver name
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
            fail("Should throw exception");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Typical use case - TAR
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
            assertNotNull(aos);
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        // Edge case - Empty archiver name
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("", out);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }

        // Edge case - Null output stream
        try {
            ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("tar", null);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }

        // Error scenario - Unknown archiver name
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
            fail("Should throw exception");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        // Typical use case - ZIP
        try {
            InputStream in = new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04});
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        // Edge case - Null input stream
        try {
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }

        // Error scenario - No Archiver found for the stream signature
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
            fail("Should throw exception");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

}