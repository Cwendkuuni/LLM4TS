import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test creating archive input stream with known archiver name "AR"
        // Edge case: archiverName is null
        // Expected: IllegalArgumentException is thrown
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        // TODO: Add more test cases for createArchiveInputStream method
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test creating archive output stream with known archiver name "TAR"
        // Typical case: archiverName is "TAR"
        // Expected: TarArchiveOutputStream is created
        try {
            assertNotNull(new ArchiveStreamFactory().createArchiveOutputStream("TAR", System.out));
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException: " + e.getMessage());
        }

        // TODO: Add more test cases for createArchiveOutputStream method
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() {
        // Test creating archive input stream with autodetection
        // Edge case: InputStream does not support mark
        // Expected: IllegalArgumentException is thrown
        try {
            new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        // TODO: Add more test cases for createArchiveInputStream method with autodetection
    }

    // Merge all individual test cases into one comprehensive JUnit test file

}