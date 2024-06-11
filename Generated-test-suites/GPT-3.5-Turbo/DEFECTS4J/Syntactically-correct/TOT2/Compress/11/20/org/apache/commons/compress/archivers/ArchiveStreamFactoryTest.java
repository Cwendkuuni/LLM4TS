import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_validArchiverName() {
        // Test valid archiver name "zip"
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        ArchiveInputStream ais = factory.createArchiveInputStream("zip", in);
        
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveOutputStream_validArchiverName() {
        // Test valid archiver name "tar"
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        ArchiveOutputStream aos = factory.createArchiveOutputStream("tar", out);
        
        assertNotNull(aos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() {
        // Test null archiver name
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() {
        // Test null output stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveOutputStream("zip", null);
    }

    @Test
    public void testCreateArchiveInputStream_autodetectArchiveType() {
        // Test autodetecting archive type from input stream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04}; // ZIP signature
        InputStream in = new ByteArrayInputStream(data);
        
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertNotNull(ais);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_unsupportedMark() {
        // Test input stream without supporting mark
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream("test.txt");
        
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiverName() {
        // Test unknown archiver name
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        factory.createArchiveInputStream("unknown", in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_invalidStreamSignature() {
        // Test invalid stream signature for autodetection
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[4]; // Invalid signature
        InputStream in = new ByteArrayInputStream(data);
        
        factory.createArchiveInputStream(in);
    }

}