import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        try {
            factory.createArchiveInputStream(null, inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        try {
            factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            factory.createArchiveOutputStream(null, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        try {
            factory.createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_ArchiveException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        try {
            factory.createArchiveInputStream("nonexistent", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_IOException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[8]);

        try {
            factory.createArchiveInputStream("zip", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertNotNull(archiveInputStream);
        } catch (ArchiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}