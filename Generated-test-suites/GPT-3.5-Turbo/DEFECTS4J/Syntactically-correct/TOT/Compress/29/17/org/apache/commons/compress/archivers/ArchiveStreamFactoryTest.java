
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class ArchiveStreamFactoryTest {

    // Tests for ArchiveStreamFactory#createArchiveInputStream(String, InputStream)

    // Test for AR format
    @Test
    public void testCreateArchiveInputStreamAR() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = ArchiveStreamFactory.createArchiveInputStream("ar", in);
        assertTrue(ais instanceof ArArchiveInputStream);
    }

    // Test for ARJ format
    @Test
    public void testCreateArchiveInputStreamARJ() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = ArchiveStreamFactory.createArchiveInputStream("arj", in);
        assertTrue(ais instanceof ArjArchiveInputStream);
    }

    // Test for unsupported format
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnsupportedFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = ArchiveStreamFactory.createArchiveInputStream("fake_format", in);
    }

    // Tests for ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)

    // Test for AR format
    @Test
    public void testCreateArchiveOutputStreamAR() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = ArchiveStreamFactory.createArchiveOutputStream("ar", out);
        assertTrue(aos instanceof ArArchiveOutputStream);
    }

    // Test for ZIP format
    @Test
    public void testCreateArchiveOutputStreamZIP() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = ArchiveStreamFactory.createArchiveOutputStream("zip", out);
        assertTrue(aos instanceof ZipArchiveOutputStream);
    }

    // Test for unsupported format
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnsupportedFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = ArchiveStreamFactory.createArchiveOutputStream("fake_format", out);
    }

    // Tests for ArchiveStreamFactory#createArchiveInputStream(InputStream)

    // Test for ZIP format
    @Test
    public void testCreateArchiveInputStreamZIP() throws Exception {
        byte[] signature = new byte[]{0x50, 0x4b, 0x03, 0x04}; // ZIP signature
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream ais = ArchiveStreamFactory.createArchiveInputStream(in);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }

    // Test for TAR format
    @Test
    public void testCreateArchiveInputStreamTAR() throws Exception {
        byte[] signature = new byte[512]; // TAR signature
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream ais = ArchiveStreamFactory.createArchiveInputStream(in);
        assertTrue(ais instanceof TarArchiveInputStream);
    }

    // Test for unsupported format
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnsupportedSignature() throws Exception {
        byte[] signature = new byte[4]; // Invalid signature
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream ais = ArchiveStreamFactory.createArchiveInputStream(in);
    }

}

