import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamValid() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream ais = factory.createArchiveInputStream("zip", in);
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveOutputStreamValid() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream aos = factory.createArchiveOutputStream("zip", out);
        assertNotNull(aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("invalid", in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("zip", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("zip", null);
    }

    // Add more test cases for edge cases and exception handling scenarios if required

}