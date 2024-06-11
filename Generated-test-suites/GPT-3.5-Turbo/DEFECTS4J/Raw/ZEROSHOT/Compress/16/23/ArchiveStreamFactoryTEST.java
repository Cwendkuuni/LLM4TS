import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        InputStream in = new ByteArrayInputStream(new byte[10]);
        
        ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);
        
        assertNotNull(ais);
        assertEquals(ArArchiveInputStream.class, ais.getClass());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[10]);
        
        try {
            factory.createArchiveInputStream(archiverName, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        InputStream in = null;
        
        try {
            factory.createArchiveInputStream(archiverName, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithUnknownArchiver() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "unknown";
        InputStream in = new ByteArrayInputStream(new byte[10]);
        
        try {
            factory.createArchiveInputStream(archiverName, in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        OutputStream out = new ByteArrayOutputStream();
        
        ArchiveOutputStream aos = factory.createArchiveOutputStream(archiverName, out);
        
        assertNotNull(aos);
        assertEquals(ArArchiveOutputStream.class, aos.getClass());
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();
        
        try {
            factory.createArchiveOutputStream(archiverName, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        OutputStream out = null;
        
        try {
            factory.createArchiveOutputStream(archiverName, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithUnknownArchiver() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "unknown";
        OutputStream out = new ByteArrayOutputStream();
        
        try {
            factory.createArchiveOutputStream(archiverName, out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertNotNull(ais);
        assertEquals(ZipArchiveInputStream.class, ais.getClass());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = null;
        
        try {
            factory.createArchiveInputStream(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNonMarkableInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            public int read() {
                return 0;
            }
        };
        
        try {
            factory.createArchiveInputStream(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithUnknownSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        
        try {
            factory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
    
}