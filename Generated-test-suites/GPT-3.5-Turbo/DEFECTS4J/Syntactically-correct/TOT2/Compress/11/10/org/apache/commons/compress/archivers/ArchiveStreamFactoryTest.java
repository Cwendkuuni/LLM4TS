import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidInput() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Should not throw an exception for valid input");
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(null, in);
            fail("Should throw IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Should throw IllegalArgumentException for null archiver name");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidInput() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
        } catch (Exception e) {
            fail("Should not throw an exception for valid input");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Should throw IllegalArgumentException for null output stream");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Should throw IllegalArgumentException for null output stream");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Should not throw an exception for valid input");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_NoMarkSupport() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            factory.createArchiveInputStream(in);
            fail("Should throw IllegalArgumentException for stream without mark support");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Should throw IllegalArgumentException for stream without mark support");
        }
    }

    // Additional test cases for edge cases and error scenarios can be added here

}