import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, in));
        
        in = new ByteArrayInputStream(new byte[10]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, in));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test
    public void testCreateZipArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in));
        
        in = new ByteArrayInputStream(new byte[10]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateZipArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateZipArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    // Add test cases for other archiver names and archive input streams
    
    @Test
    public void testCreateArArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }
    
    @Test
    public void testCreateZipArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateZipArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateZipArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    // Add test cases for other archiver names and archive output streams
    
    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(in));
        
        in = new ByteArrayInputStream(new byte[10]);
        assertNotNull(factory.createArchiveInputStream(in));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }
    
    // Add test cases to verify autodetection of archive type
    
}