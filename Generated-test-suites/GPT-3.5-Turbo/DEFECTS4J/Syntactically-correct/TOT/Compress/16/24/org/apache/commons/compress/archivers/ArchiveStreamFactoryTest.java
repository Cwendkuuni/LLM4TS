import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
        inputStream = new ByteArrayInputStream("test string".getBytes());
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateArchiveInputStream_validArchiverName() {
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
            assertNotNull(archiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() throws Exception {
        factory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws Exception {
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiverName() throws Exception {
        factory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_validArchiverName() {
        try {
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
            assertNotNull(archiveOutputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() throws Exception {
        factory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() throws Exception {
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_unknownArchiverName() throws Exception {
        factory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect_validSignature() {
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertNotNull(archiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_autoDetect_nullInputStream() throws Exception {
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_autoDetect_inputStreamWithoutMarkSupport() throws Exception {
        InputStream inputStreamWithoutMarkSupport = new ByteArrayInputStream("test string".getBytes());
        factory.createArchiveInputStream(inputStreamWithoutMarkSupport);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_autoDetect_invalidSignature() throws Exception {
        InputStream invalidInputStream = new ByteArrayInputStream("invalid signature".getBytes());
        factory.createArchiveInputStream(invalidInputStream);
    }

    // Additional test cases can be added here to cover edge cases and error scenarios

}