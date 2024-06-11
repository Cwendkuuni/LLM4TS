import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiverNameAndInputStream_Success() {
        // Test basic functionality of createArchiveInputStream using a valid archiver name and input stream
        // Verify that the returned archive input stream is not null
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(ais);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ExceptionThrown() {
        // Test scenario where archiver name is null when calling createArchiveInputStream
        // Expect an IllegalArgumentException to be thrown
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (ArchiveException e) {
            fail("Unexpected exception");
        }
    }

    // Additional test cases for other methods and scenarios can be added here

}