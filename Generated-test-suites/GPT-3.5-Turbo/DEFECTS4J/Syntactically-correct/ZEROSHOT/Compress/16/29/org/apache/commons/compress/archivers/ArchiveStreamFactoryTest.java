import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            assertNull("Returned input stream should be null.", in);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test ZIP archive format
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            assertNull("Returned input stream should be null.", in);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test TAR archive format
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            assertNull("Returned input stream should be null.", in);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test JAR archive format
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            assertNull("Returned input stream should be null.", in);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test CPIO archive format
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            assertNull("Returned input stream should be null.", in);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test DUMP archive format
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            assertNull("Returned input stream should be null.", in);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test unknown archiver name
        try {
            ArchiveInputStream in = factory.createArchiveInputStream("unknown", null);
            assertNull("Returned input stream should be null.", in);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        try {
            ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            assertNull("Returned output stream should be null.", out);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test ZIP archive format
        try {
            ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            assertNull("Returned output stream should be null.", out);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test TAR archive format
        try {
            ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            assertNull("Returned output stream should be null.", out);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test JAR archive format
        try {
            ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            assertNull("Returned output stream should be null.", out);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test CPIO archive format
        try {
            ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            assertNull("Returned output stream should be null.", out);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        // Test unknown archiver name
        try {
            ArchiveOutputStream out = factory.createArchiveOutputStream("unknown", null);
            assertNull("Returned output stream should be null.", out);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test null input stream
        try {
            ArchiveInputStream in = factory.createArchiveInputStream(null);
            assertNull("Returned input stream should be null.", in);
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
        
        // Test input stream without mark support
        try {
            InputStream in = new ByteArrayInputStream(new byte[10]);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNull("Returned input stream should be null.", archiveIn);
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
        
        // Test ZIP archive format
        try {
            byte[] signature = {80, 75, 3, 4};
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNotNull("Returned input stream should not be null.", archiveIn);
            assertTrue("Returned input stream should be of type ZipArchiveInputStream.", archiveIn instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail("An exception occurred while creating archive input stream for ZIP format: " + e.getMessage());
        }
        
        // Test JAR archive format
        try {
            byte[] signature = {80, 75, 3, 4};
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNotNull("Returned input stream should not be null.", archiveIn);
            assertTrue("Returned input stream should be of type JarArchiveInputStream.", archiveIn instanceof JarArchiveInputStream);
        } catch (ArchiveException e) {
            fail("An exception occurred while creating archive input stream for JAR format: " + e.getMessage());
        }
        
        // Test AR archive format
        try {
            byte[] signature = {33, 60, 48, 62};
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNotNull("Returned input stream should not be null.", archiveIn);
            assertTrue("Returned input stream should be of type ArArchiveInputStream.", archiveIn instanceof ArArchiveInputStream);
        } catch (ArchiveException e) {
            fail("An exception occurred while creating archive input stream for AR format: " + e.getMessage());
        }
        
        // Test CPIO archive format
        try {
            byte[] signature = {48, 60, 83, 69};
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNotNull("Returned input stream should not be null.", archiveIn);
            assertTrue("Returned input stream should be of type CpioArchiveInputStream.", archiveIn instanceof CpioArchiveInputStream);
        } catch (ArchiveException e) {
            fail("An exception occurred while creating archive input stream for CPIO format: " + e.getMessage());
        }
        
        // Test DUMP archive format
        try {
            byte[] signature = {120, -100, 102, -34};
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNotNull("Returned input stream should not be null.", archiveIn);
            assertTrue("Returned input stream should be of type DumpArchiveInputStream.", archiveIn instanceof DumpArchiveInputStream);
        } catch (ArchiveException e) {
            fail("An exception occurred while creating archive input stream for DUMP format: " + e.getMessage());
        }
        
        // Test TAR archive format
        try {
            byte[] signature = {48, -104, 48, 56};
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNotNull("Returned input stream should not be null.", archiveIn);
            assertTrue("Returned input stream should be of type TarArchiveInputStream.", archiveIn instanceof TarArchiveInputStream);
        } catch (ArchiveException e) {
            fail("An exception occurred while creating archive input stream for TAR format: " + e.getMessage());
        }
        
        // Test unknown archive format
        try {
            byte[] signature = {0, 0, 0, 0};
            InputStream in = new ByteArrayInputStream(signature);
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNull("Returned input stream should be null.", archiveIn);
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}