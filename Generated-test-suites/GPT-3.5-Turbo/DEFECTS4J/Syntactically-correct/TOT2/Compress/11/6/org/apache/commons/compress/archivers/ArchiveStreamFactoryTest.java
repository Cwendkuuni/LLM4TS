import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() {
        // Test the creation of ArchiveInputStream for AR format
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, in));
        } catch (Exception e) {
            fail("Exception thrown for AR format: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test the creation of ArchiveOutputStream for ZIP format
        try {
            OutputStream out = new ByteArrayOutputStream();
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out));
        } catch (Exception e) {
            fail("Exception thrown for ZIP format: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        // Test auto-detection of ArchiveInputStream for ZIP format
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(in));
        } catch (Exception e) {
            fail("Exception thrown for ZIP format autodetection: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() {
        // Test creation of ArchiveInputStream with null archiver name
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() {
        // Test creation of ArchiveInputStream with null input stream
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() {
        // Test creation of ArchiveOutputStream with null archiver name
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() {
        // Test creation of ArchiveOutputStream with null output stream
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiver() {
        // Test creation of ArchiveInputStream with unknown archiver name
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiver() {
        // Test creation of ArchiveOutputStream with unknown archiver name
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNoMarkSupport() {
        // Test creation of ArchiveInputStream with input stream not supporting mark
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            in.reset();
        } catch (Exception e) {
            fail("InputStream does not support mark: " + e.getMessage());
        }
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownSignature() {
        // Test auto-detection of ArchiveInputStream with unknown stream signature
        byte[] signature = new byte[12];
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            in.mark(signature.length);
            factory.createArchiveInputStream(in);
        } catch (Exception e) {
            fail("Unknown signature not detected: " + e.getMessage());
        }
    }
}