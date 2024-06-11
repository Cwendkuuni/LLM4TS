
import org.junit.Test;
import java.io.ByteArrayInputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR_NullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(null, inputStream);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStream_UnsupportedArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream("invalid", inputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    // Additional test cases can be added for other methods and scenarios

}

