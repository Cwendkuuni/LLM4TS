import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0])));
        
        // Test ZIP archive format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0])));
        
        // Test TAR archive format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0])));
        
        // Test JAR archive format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0])));
        
        // Test CPIO archive format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0])));
        
        // Test DUMP archive format
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0])));
        
        // Test ArchiveException when archiver name is not known
        try {
            factory.createArchiveInputStream("invalidFormat", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
        
        // Test IllegalArgumentException when archiver name is null
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test IllegalArgumentException when input stream is null
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, System.out));
        
        // Test ZIP archive format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, System.out));
        
        // Test TAR archive format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, System.out));
        
        // Test JAR archive format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, System.out));
        
        // Test CPIO archive format
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, System.out));
        
        // Test ArchiveException when archiver name is not known
        try {
            factory.createArchiveOutputStream("invalidFormat", System.out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
        
        // Test IllegalArgumentException when archiver name is null
        try {
            factory.createArchiveOutputStream(null, System.out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test IllegalArgumentException when output stream is null
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP archive format
        assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04})));
        
        // Test JAR archive format
        assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04})));
        
        // Test AR archive format
        assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'!', '<', 'a', 'r', 'c', 'h', '>', 0x0A})));
        
        // Test CPIO archive format
        assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'0', '7', '0', '7'})));
        
        // Test DUMP archive format
        assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'U', 'S', 'T', 'A', 'R', ' ', ' '})));


        // Test Tar archive format
        assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{0x75, 0x73, 0x74, 0x61, 0x72, 0x20, 0x20, 0x20, 0x20, 0x00})));

        // Test ArchiveException when archiver is not found
        try {
            factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
        
        // Test IllegalArgumentException when input stream is null
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}