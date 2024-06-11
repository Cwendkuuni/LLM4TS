
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream inputStream = factory.createArchiveInputStream("zip", new ByteArrayInputStream(new byte[0]));
        assertNotNull(inputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("zip", new ByteArrayOutputStream());
        assertNotNull(outputStream);
    }
}