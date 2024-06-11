
import org.junit.Test;
import org.apache.commons.compress.archivers.*;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR format
        String archiverName = ArchiveStreamFactory.AR;
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test ZIP format
        archiverName = ArchiveStreamFactory.ZIP;
        in = new ByteArrayInputStream(new byte[0]);
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test TAR format
        archiverName = ArchiveStreamFactory.TAR;
        in = new ByteArrayInputStream(new byte[0]);
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test JAR format
        archiverName = ArchiveStreamFactory.JAR;
        in = new ByteArrayInputStream(new byte[0]);
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test CPIO format
        archiverName = ArchiveStreamFactory.CPIO;
        in = new ByteArrayInputStream(new byte[0]);
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);
        
        // Test DUMP format
        archiverName = ArchiveStreamFactory.DUMP;
        in = new ByteArrayInputStream(new byte[0]);
        inputStream = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(inputStream);

        // Test unknown format
        archiverName = "unknown";
        in = new ByteArrayInputStream(new byte[0]);
        try {
            inputStream = factory.createArchiveInputStream(archiverName, in);
            fail("Expected ArchiveException to be thrown.");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR format
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test ZIP format
        archiverName = ArchiveStreamFactory.ZIP;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test TAR format
        archiverName = ArchiveStreamFactory.TAR;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test JAR format
        archiverName = ArchiveStreamFactory.JAR;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);
        
        // Test CPIO format
        archiverName = ArchiveStreamFactory.CPIO;
        out = new ByteArrayOutputStream();
        outputStream = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(outputStream);

        // Test unknown format
        archiverName = "unknown";
        out = new ByteArrayOutputStream();
        try {
            outputStream = factory.createArchiveOutputStream(archiverName, out);
            fail("Expected ArchiveException to be thrown.");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutodetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP format
        InputStream in = new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04});
        ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test JAR format
        in = new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test AR format
        in = new ByteArrayInputStream(new byte[]{0x21, 0x3C, 0x61, 0x72, 0x63, 0x68, 0x3E, 0x0D, 0x0A});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test CPIO format
        in = new ByteArrayInputStream(new byte[]{0x30, 0x37, 0x30, 0x37, 0x30});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test DUMP format
        in = new ByteArrayInputStream(new byte[]{0x42, 0x30, 0x30, 0x30});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test TAR format
        in = new ByteArrayInputStream(new byte[]{0x75, 0x73, 0x74, 0x61, 0x72, 0x20, 0x20, 0x20});
        inputStream = factory.createArchiveInputStream(in);
        assertNotNull(inputStream);
        
        // Test unknown format
        in = new ByteArrayInputStream(new byte[0]);
        try {
            inputStream = factory.createArchiveInputStream(in);
            fail("Expected ArchiveException to be thrown.");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }
}
