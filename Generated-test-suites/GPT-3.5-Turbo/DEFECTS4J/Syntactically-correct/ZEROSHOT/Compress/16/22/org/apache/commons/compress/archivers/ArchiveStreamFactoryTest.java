import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() {
        try {
            factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testCreateArchiveInputStream_UnknownArchiverName_ThrowsArchiveException() {
        try {
            factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // success
        }
    }

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName_ReturnsArchiveInputStream() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream output = factory.createArchiveInputStream("zip", input);
            assertNotNull(output);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        try {
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() {
        try {
            factory.createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownArchiverName_ThrowsArchiveException() {
        try {
            factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // success
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName_ReturnsArchiveOutputStream() {
        try {
            OutputStream output = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", output);
            assertNotNull(archiveOutputStream);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStream_FromInputStream_ReturnsArchiveInputStream() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream output = factory.createArchiveInputStream(input);
            assertNotNull(output);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_MarksNotSupported_ThrowsIllegalArgumentException() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[0]) {
                @Override
                public synchronized void mark(int readlimit) {
                    // does not support mark
                }
            };
            factory.createArchiveInputStream(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_InvalidSignature_ThrowsArchiveException() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(input);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // success
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_ValidSignature_ReturnsArchiveInputStream() {
        try {
            InputStream input = new ByteArrayInputStream("PK\003\004".getBytes()); // zip magic number
            ArchiveInputStream output = factory.createArchiveInputStream(input);
            assertNotNull(output);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}