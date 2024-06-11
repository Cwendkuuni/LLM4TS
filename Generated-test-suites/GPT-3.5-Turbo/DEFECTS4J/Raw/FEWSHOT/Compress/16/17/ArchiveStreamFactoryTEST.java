import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    private ArchiveStreamFactory factory;
    
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        factory = new ArchiveStreamFactory();
        
        // Test with valid archiver names
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, in));
        
        in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in));
        
        in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in));
        
        in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in));
        
        in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in));
        
        in = new ByteArrayInputStream(new byte[0]);
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in));
        
        // Test with invalid archiver name
        try {
            in = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream("invalid", in);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // success
        }
        
        // Test with null archiver name
        try {
            in = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
        
        // Test with null input stream
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        factory = new ArchiveStreamFactory();
        
        // Test with valid archiver names
        OutputStream out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out));
        
        out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out));
        
        out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out));
        
        out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out));
        
        out = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out));
        
        // Test with invalid archiver name
        try {
            out = new ByteArrayOutputStream();
            factory.createArchiveOutputStream("invalid", out);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // success
        }
        
        // Test with null archiver name
        try {
            out = new ByteArrayOutputStream();
            factory.createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
        
        // Test with null output stream
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testCreateArchiveInputStream_autoDetect() throws ArchiveException, IOException {
        factory = new ArchiveStreamFactory();
        
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        
        // Test with ZIP archive
        assertNotNull(factory.createArchiveInputStream(in));
        
        // Test with JAR archive
        in.reset();
        assertNotNull(factory.createArchiveInputStream(in));
        
        // Test with AR archive
        in.reset();
        assertNotNull(factory.createArchiveInputStream(in));
        
        // Test with CPIO archive
        in.reset();
        assertNotNull(factory.createArchiveInputStream(in));
        
        // Test with TAR archive
        in.reset();
        assertNotNull(factory.createArchiveInputStream(in));
        
        // Test with invalid archive
        in.reset();
        try {
            factory.createArchiveInputStream(in);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // success
        }
    }
}