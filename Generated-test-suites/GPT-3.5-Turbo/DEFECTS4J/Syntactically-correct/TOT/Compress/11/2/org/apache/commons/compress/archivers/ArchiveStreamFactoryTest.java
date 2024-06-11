import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_validArchiverNameAndInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", in);
            assertNotNull(archiveInputStream);
        } catch (ArchiveException | IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("zip", null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_invalidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("rar", in);
    }
    
    @Test
    public void testCreateArchiveOutputStream_validArchiverNameAndOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", out);
            assertNotNull(archiveOutputStream);
        } catch (ArchiveException | IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("zip", null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_invalidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("rar", out);
    }
    
    @Test
    public void testCreateArchiveInputStream_autoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
            assertNotNull(archiveInputStream);
        } catch (ArchiveException | IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_invalidInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream("nonExistentFile.txt");
        factory.createArchiveInputStream(in);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_unsupportedArchive() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
    }
    
}