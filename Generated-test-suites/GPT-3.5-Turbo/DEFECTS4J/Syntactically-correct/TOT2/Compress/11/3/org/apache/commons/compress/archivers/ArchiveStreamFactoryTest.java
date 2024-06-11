import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_archiverNameNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[10]));
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_inputStreamNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException for null input stream");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_archiverNameNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_outputStreamNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException for null output stream");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetectStreamNull() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException for null input stream");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_streamNotSupportMark() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(new ByteArrayInputStream(new byte[10]));
            fail("Expected IllegalArgumentException for input stream that does not support mark");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Additional test cases for various archiver names and input/output streams can be added here

}