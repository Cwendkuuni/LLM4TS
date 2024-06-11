import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamValid() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);

            assertNotNull(inputStream);
        } catch (ArchiveException e) {
            fail("Exception thrown unexpectedly: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamValid() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);

            assertNotNull(outputStream);
        } catch (ArchiveException e) {
            fail("Exception thrown unexpectedly: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        try {
            factory.createArchiveInputStream(null, null);
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        try {
            factory.createArchiveOutputStream(null, null);
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    // Add more test cases for edge cases and error scenarios

}