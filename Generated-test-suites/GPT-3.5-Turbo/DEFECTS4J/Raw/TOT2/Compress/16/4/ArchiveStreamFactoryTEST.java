import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName_ValidInputStream() {
        // Test creating an ArchiveInputStream with valid archiver name and input stream
        // Check that the created ArchiveInputStream is not null

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream("test data".getBytes());
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        // Test creating an ArchiveInputStream with null archiver name
        // Check that IllegalArgumentException is thrown

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream("test data".getBytes());
            factory.createArchiveInputStream(null, in);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() {
        // Test creating an ArchiveInputStream with null input stream
        // Check that IllegalArgumentException is thrown

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName_ValidOutputStream() {
        // Test creating an ArchiveOutputStream with valid archiver name and output stream
        // Check that the created ArchiveOutputStream is not null

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        // Test creating an ArchiveOutputStream with null archiver name
        // Check that IllegalArgumentException is thrown

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            factory.createArchiveOutputStream(null, out);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() {
        // Test creating an ArchiveOutputStream with null output stream
        // Check that IllegalArgumentException is thrown

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getMessage());
        }
    }

    // additional tests for edge cases and exception handling scenarios can be added here

}