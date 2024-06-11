import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test AR archive format
        String archiverName = ArchiveStreamFactory.AR;
        InputStream in = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test ZIP archive format
        archiverName = ArchiveStreamFactory.ZIP;
        in = new ByteArrayInputStream(new byte[]{});
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test TAR archive format
        archiverName = ArchiveStreamFactory.TAR;
        in = new ByteArrayInputStream(new byte[]{});
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test JAR archive format
        archiverName = ArchiveStreamFactory.JAR;
        in = new ByteArrayInputStream(new byte[]{});
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test CPIO archive format
        archiverName = ArchiveStreamFactory.CPIO;
        in = new ByteArrayInputStream(new byte[]{});
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test DUMP archive format
        archiverName = ArchiveStreamFactory.DUMP;
        in = new ByteArrayInputStream(new byte[]{});
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test unknown archiver name
        archiverName = "unknown";
        in = new ByteArrayInputStream(new byte[]{});
        try {
            inputStream = factory.createArchiveInputStream(archiverName, in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }
    
    @Test 
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test AR archive format
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test ZIP archive format
        archiverName = ArchiveStreamFactory.ZIP;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test TAR archive format
        archiverName = ArchiveStreamFactory.TAR;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test JAR archive format
        archiverName = ArchiveStreamFactory.JAR;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test CPIO archive format
        archiverName = ArchiveStreamFactory.CPIO;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test unknown archiver name
        archiverName = "unknown";
        out = new ByteArrayOutputStream();
        try {
            outputStream = factory.createArchiveOutputStream(archiverName, out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test ZIP archive format
        InputStream in = new ByteArrayInputStream(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
        ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test JAR archive format
        in = new ByteArrayInputStream(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test AR archive format
        in = new ByteArrayInputStream(new byte[]{(byte)0x21, (byte)0x3C, (byte)0x61, (byte)0x72});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test CPIO archive format
        in = new ByteArrayInputStream(new byte[]{(byte)0x30, (byte)0x37, (byte)0x30, (byte)0x37});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test TAR archive format
        in = new ByteArrayInputStream(new byte[]{(byte)0x75, (byte)0x73, (byte)0x74, (byte)0x61});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test DUMP archive format
        in = new ByteArrayInputStream(new byte[]{(byte)0x7F, (byte)0x45, (byte)0x4C, (byte)0x46});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test unsupported archive format
        in = new ByteArrayInputStream(new byte[]{});
        try {
            inputStream = factory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}