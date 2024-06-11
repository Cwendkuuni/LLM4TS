import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidInput() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);
            assertNotNull(ais);
        } catch (ArchiveException e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(null, in);
            fail("Exception should be thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        try {
            factory.createArchiveInputStream(archiverName, null);
            fail("Exception should be thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }

    // Additional test cases for other methods should be added here

}