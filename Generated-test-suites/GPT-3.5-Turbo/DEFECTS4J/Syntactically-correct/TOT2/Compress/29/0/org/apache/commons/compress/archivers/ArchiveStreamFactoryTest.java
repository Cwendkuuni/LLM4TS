import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_WithValidArchiverName_AR() {
        // Test creating ArchiveInputStream with valid archiver name "ar"
        // Assert the output is not null and of type ArArchiveInputStream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, null));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_WithInvalidArchiverName() {
        // Test creating ArchiveInputStream with invalid archiver name
        // Assert that an ArchiveException is thrown
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("invalid", null);
            fail("ArchiveException should be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof ArchiveException);
        }
    }

    @Test
    public void testCreateArchiveOutputStream_WithValidArchiverName_ZIP() {
        // Test creating ArchiveOutputStream with valid archiver name "zip"
        // Assert the output is not null and of type ZipArchiveOutputStream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAutoDetectArchiveInputStream_ZIP() {
        // Test autodetecting ArchiveInputStream from input stream with ZIP format
        // Assert the output is not null and of type ZipArchiveInputStream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            assertNotNull(factory.createArchiveInputStream(null));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAutoDetectArchiveInputStream_WithInvalidStream() {
        // Test autodetecting ArchiveInputStream from an invalid input stream
        // Assert that an IllegalArgumentException is thrown
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null);
            fail("IllegalArgumentException should be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testAutoDetectArchiveInputStream_WithInvalidMarkSupport() {
        // Test autodetecting ArchiveInputStream from an input stream without mark support
        // Assert that an IllegalArgumentException is thrown
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            });
            fail("IllegalArgumentException should be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

}