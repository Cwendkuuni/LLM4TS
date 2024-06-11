import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("test", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream("test", new ByteArrayOutputStream());
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectWithNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutodetectWithUnsupportedMark() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

}