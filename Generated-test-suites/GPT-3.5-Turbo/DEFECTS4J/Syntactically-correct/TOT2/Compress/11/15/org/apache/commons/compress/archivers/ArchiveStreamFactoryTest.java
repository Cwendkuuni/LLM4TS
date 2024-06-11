import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_ValidParameters() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in));
        } catch (Exception e) {
            fail("Exception thrown when creating ArchiveInputStream with valid parameters");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (Exception e) {
            fail("Wrong exception thrown for null archiver name");
        }
    }

    // Add more test cases for edge cases and error scenarios

    @Test
    public void testCreateArchiveOutputStream_ValidParameters() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out));
        } catch (Exception e) {
            fail("Exception thrown when creating ArchiveOutputStream with valid parameters");
        }
    }

    // Add more test cases for other public methods

}