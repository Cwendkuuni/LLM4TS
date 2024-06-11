import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException for null archiverName");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (ArchiveException e) {
            fail("Expected IllegalArgumentException for null archiverName");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException for null InputStream");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (ArchiveException e) {
            fail("Expected IllegalArgumentException for null InputStream");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException for null archiverName");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (ArchiveException e) {
            fail("Expected IllegalArgumentException for null archiverName");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException for null OutputStream");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (ArchiveException e) {
            fail("Expected IllegalArgumentException for null OutputStream");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithNullInputStream() {
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected IllegalArgumentException for null InputStream in autodetect");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        } catch (ArchiveException e) {
            fail("Expected IllegalArgumentException for null InputStream in autodetect");
        }
    }
  
}