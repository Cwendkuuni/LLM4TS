import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Typical use case
        // Testing AR archive format
        ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertNotNull(arInputStream);

        // Edge case
        // Testing unknown archiver name
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", null);
            fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            // Expected
        }

        // Exception scenario
        // Testing null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Typical use case
        // Testing ZIP archive format
        ArchiveOutputStream zipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        assertNotNull(zipOutputStream);

        // Edge case
        // Testing unsupported archiver name
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, null);
            fail("Expected StreamingNotSupportedException was not thrown");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Exception scenario
        // Testing null output stream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        // Typical use case
        // Testing auto-detecting ZIP archive format
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(zipArchiveInputStream);

        // Edge case
        // Testing unsupported archive format
        try {
            new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            // Expected
        }

        // Exception scenario
        // Testing null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}