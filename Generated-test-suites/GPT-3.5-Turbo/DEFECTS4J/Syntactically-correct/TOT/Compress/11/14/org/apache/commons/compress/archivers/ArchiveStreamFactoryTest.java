import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    // Test cases for createArchiveInputStream method
    
    @Test
    public void testCreateArchiveInputStreamValidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream stream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(stream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream stream = factory.createArchiveInputStream(archiverName, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;
        ArchiveInputStream stream = factory.createArchiveInputStream(archiverName, in);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "invalid";
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream stream = factory.createArchiveInputStream(archiverName, in);
    }
    
    // Test cases for createArchiveOutputStream method
    
    @Test
    public void testCreateArchiveOutputStreamValidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream stream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(stream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream stream = factory.createArchiveOutputStream(archiverName, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;
        ArchiveOutputStream stream = factory.createArchiveOutputStream(archiverName, out);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalidArchiverName() throws ArchiveException {
        ArchivesStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "invalid";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream stream = factory.createArchiveOutputStream(archiverName, out);
    }
    
    // Test cases for createArchiveInputStream method with auto-detect
    
    @Test
    public void testCreateArchiveInputStreamValidInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream stream = factory.createArchiveInputStream(in);
        assertNotNull(stream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStreamAutoDetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = null;
        ArchiveInputStream stream = factory.createArchiveInputStream(in);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidInputStreamAutoDetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream("invalid");
        ArchiveInputStream stream = factory.createArchiveInputStream(in);
    }

}