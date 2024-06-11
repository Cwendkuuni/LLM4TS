import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test case 1: Valid archiver name and input stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream("zip", in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: Null archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(null, in);
            fail("No exception thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getName());
        }
        
        // Test case 3: Null input stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream ais = factory.createArchiveInputStream("zip", null);
            fail("No exception thrown for null input stream");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getName());
        }
        
        // Test case 4: Unknown archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream("unknown", in);
            fail("No exception thrown for unknown archiver name");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getName());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test case 1: Valid archiver name and output stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream("zip", out);
            assertNotNull(aos);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: Null archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(null, out);
            fail("No exception thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getName());
        }
        
        // Test case 3: Null output stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream aos = factory.createArchiveOutputStream("zip", null);
            fail("No exception thrown for null output stream");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getName());
        }
        
        // Test case 4: Unknown archiver name
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream("unknown", out);
            fail("No exception thrown for unknown archiver name");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getName());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() {
        // Test case 1: Valid input stream with ZIP archive
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] zipMagicBytes = new byte[] { 80, 75, 3, 4 };
            InputStream in = new ByteArrayInputStream(zipMagicBytes);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: Valid input stream with JAR archive
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] jarMagicBytes = new byte[] { 80, 75, 3, 4, 20, 0 };
            InputStream in = new ByteArrayInputStream(jarMagicBytes);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 3: Valid input stream with AR archive
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] arMagicBytes = new byte[] { 33, 60, 0, 62, 10 };
            InputStream in = new ByteArrayInputStream(arMagicBytes);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 4: Valid input stream with CPIO archive
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] cpioMagicBytes = new byte[] { 59, 73, 77, 67, 70, 66 };
            InputStream in = new ByteArrayInputStream(cpioMagicBytes);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 5: Valid input stream with dump archive
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] dumpMagicBytes = new byte[] { 127, 69, 76, 70 };
            InputStream in = new ByteArrayInputStream(dumpMagicBytes);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 6: Valid input stream with TAR archive
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] tarMagicBytes = new byte[] { 0, 120, 54, 48, 48, 48, 48, 48, 48, 48 };
            InputStream in = new ByteArrayInputStream(tarMagicBytes);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 7: Invalid input stream
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            fail("No exception thrown for invalid input stream");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getClass().getName());
        }
    }
}