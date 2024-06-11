import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IllegalArgumentException {
        // Create a dummy InputStream
        InputStream in = new ByteArrayInputStream("test".getBytes());
        
        // Test with known archiver names
        ArchiveInputStream arIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(arIn);
        
        ArchiveInputStream zipIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(zipIn);
        
        ArchiveInputStream tarIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(tarIn);
        
        ArchiveInputStream jarIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(jarIn);
        
        ArchiveInputStream cpioIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(cpioIn);
        
        ArchiveInputStream dumpIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(dumpIn);
        
        // Test with unknown archiver name
        try {
            ArchiveInputStream unknownIn = new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
            fail("Expected ArchiveException not thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        
        // Test with null archiver name
        try {
            ArchiveInputStream nullIn = new ArchiveStreamFactory().createArchiveInputStream(null, in);;
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test with null input stream
        try {
            ArchiveInputStream nullIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IllegalArgumentException {
        // Create a dummy OutputStream
        OutputStream out = new FileOutputStream(new File("dummy.txt"));
        
        // Test with known archiver names
        ArchiveOutputStream arOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(arOut);
        
        ArchiveOutputStream zipOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(zipOut);
        
        ArchiveOutputStream tarOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(tarOut);
        
        ArchiveOutputStream jarOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(jarOut);
        
        ArchiveOutputStream cpioOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(cpioOut);
        
        // Test with unknown archiver name
        try {
            ArchiveOutputStream unknownOut = new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
            fail("Expected ArchiveException not thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        
        // Test with null archiver name
        try {
            ArchiveOutputStream nullOut = new ArchiveStreamFactory().createArchiveOutputStream(null, out);;
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test with null output stream
        try {
            ArchiveOutputStream nullOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() throws ArchiveException, IOException {
        // Create a test file
        File testFile = new File("test.txt");
        
        // Create a dummy input stream
        InputStream in = new FileInputStream(testFile);
        
        // Test with known archive formats
        ArchiveInputStream zipIn = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(zipIn);
        
        ArchiveInputStream jarIn = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(jarIn);
        
        ArchiveInputStream arIn = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(arIn);
        
        ArchiveInputStream cpioIn = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(cpioIn);
        
        // Test with unknown archive format
        try {
            ArchiveInputStream unknownIn = new ArchiveStreamFactory().createArchiveInputStream(in);
            fail("Expected ArchiveException not thrown");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
        
        // Test with null input stream
        try {
            ArchiveInputStream nullIn = new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
        
        // Test with input stream that does not support mark
        InputStream unsupportedIn = new ByteArrayInputStream("test".getBytes());
        try {
            ArchiveInputStream unsupportedMarkIn = new ArchiveStreamFactory().createArchiveInputStream(unsupportedIn);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }
}