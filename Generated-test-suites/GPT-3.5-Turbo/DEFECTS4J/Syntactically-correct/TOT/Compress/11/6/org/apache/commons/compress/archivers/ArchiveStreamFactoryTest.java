import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    // Test createArchiveInputStream with valid archiver name and input stream
    @Test
    public void testCreateArchiveInputStream_ValidInput() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        
        ArchiveInputStream result = factory.createArchiveInputStream("zip", in);

        assertNotNull(result);
    }

    // Test createArchiveInputStream with null archiver name
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        
        factory.createArchiveInputStream(null, in);
    }

    // Test createArchiveInputStream with null input stream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveInputStream("zip", null);
    }

    // Test createArchiveInputStream with unknown archiver name
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        
        factory.createArchiveInputStream("unknown", in);
    }

    // Test createArchiveOutputStream with valid archiver name and output stream
    @Test
    public void testCreateArchiveOutputStream_ValidInput() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        ArchiveOutputStream result = factory.createArchiveOutputStream("zip", out);

        assertNotNull(result);
    }

    // Test createArchiveOutputStream with null archiver name
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        factory.createArchiveOutputStream(null, out);
    }

    // Test createArchiveOutputStream with null output stream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveOutputStream("zip", null);
    }

    // Test createArchiveOutputStream with unknown archiver name
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        factory.createArchiveOutputStream("unknown", out);
    }

    // Test createArchiveInputStream with valid input stream containing ZIP archive
    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZIP() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("PK\u0003\u0004".getBytes());
        
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        assertNotNull(result);
    }

    // Test createArchiveInputStream with valid input stream containing JAR archive
    @Test
    public void testCreateArchiveInputStream_AutoDetect_JAR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("PK\u0003\u0004".getBytes());
        
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        assertNotNull(result);
    }

    // Test createArchiveInputStream with valid input stream containing AR archive
    @Test
    public void testCreateArchiveInputStream_AutoDetect_AR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("!<arch>\n".getBytes());
        
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        assertNotNull(result);
    }

    // Test createArchiveInputStream with valid input stream containing CPIO archive
    @Test
    public void testCreateArchiveInputStream_AutoDetect_CPIO() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("07070\u0010".getBytes());
        
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        assertNotNull(result);
    }

    // Test createArchiveInputStream with invalid input stream
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("invalid".getBytes());
        
        factory.createArchiveInputStream(in);
    }

    // Test createArchiveInputStream with null input stream for auto-detect
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream_AutoDetect() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveInputStream(null);
    }

}