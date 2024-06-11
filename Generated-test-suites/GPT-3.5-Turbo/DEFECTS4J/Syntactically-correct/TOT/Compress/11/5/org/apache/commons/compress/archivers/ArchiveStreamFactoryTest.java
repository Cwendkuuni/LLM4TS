import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null, InputStream.nullInputStream());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_UnknownArchiverName_ThrowsArchiveException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream("tar.gz", InputStream.nullInputStream());
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: tar.gz not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName_ReturnsArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream result = factory.createArchiveInputStream("zip", InputStream.nullInputStream());
            assertNotNull(result);
        } catch (Exception e) {
            fail("Expected no exception to be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream(null, OutputStream.nullOutputStream());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownArchiverName_ThrowsArchiveException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream("tar.gz", OutputStream.nullOutputStream());
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: tar.gz not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName_ReturnsArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream result = factory.createArchiveOutputStream("zip", OutputStream.nullOutputStream());
            assertNotNull(result);
        } catch (Exception e) {
            fail("Expected no exception to be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_InputStreamIsNull_ThrowsIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_InputDoesNotSupportMark_ThrowsIllegalArgumentException() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream input = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            factory.createArchiveInputStream(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_InvalidInput_ReturnsArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream input = new ByteArrayInputStream(new byte[10]);
            ArchiveInputStream result = factory.createArchiveInputStream(input);
            assertNull(result); // This is a place holder test case since the behavior for invalid input needs to be defined
        } catch (Exception e) {
            fail("Expected no exception to be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_ValidInput_ReturnsArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            // Provide a valid input stream
            // ...
            ArchiveInputStream result = factory.createArchiveInputStream(InputStream.nullInputStream());
            assertNotNull(result);
        } catch (Exception e) {
            fail("Expected no exception to be thrown");
        }
    }
}