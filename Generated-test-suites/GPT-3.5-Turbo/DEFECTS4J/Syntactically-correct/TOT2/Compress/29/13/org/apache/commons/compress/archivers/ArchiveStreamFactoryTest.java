import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_validAr() {
        // Create an array of bytes
        byte[] data = "test data for AR format".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);

            assertNotNull(archiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_validZip() {
        // Create an array of bytes
        byte[] data = "test data for ZIP format".getBytes();
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);

            assertNotNull(archiveOutputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_invalidArchiverName() {
        // Create an array of bytes
        byte[] data = "test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);

        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("invalid", inputStream);

            fail("Exception not thrown for invalid archiver name");
        } catch (Exception e) {
            assertTrue(e instanceof ArchiveException);
        }
    }

    @Test
    public void testCreateArchiveInputStream_nullInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);

            fail("Exception not thrown for null input stream");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    // Add more test cases for edge cases and exceptional scenarios

}