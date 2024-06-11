import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Basic functionality test
        // Create a ByteArrayInputStream with some test data
        byte[] testData = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        
        // Test with AR archive format
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);
        assertNotNull(arInputStream);
        
        // Test with ZIP archive format
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertNotNull(zipInputStream);
        
        // Test with TAR archive format
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        assertNotNull(tarInputStream);
        
        // Test with JAR archive format
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        assertNotNull(jarInputStream);
        
        // Test with CPIO archive format
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        assertNotNull(cpioInputStream);
        
        // Test with DUMP archive format
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
        assertNotNull(dumpInputStream);
        
        // Test with unknown archive format
        try {
            ArchiveInputStream unknownInputStream = factory.createArchiveInputStream("unknown", input);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Basic functionality test
        // Create a ByteArrayOutputStream to capture the output
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        // Test with AR archive format
        ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertNotNull(arOutputStream);
        
        // Test with ZIP archive format
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertNotNull(zipOutputStream);
        
        // Test with TAR archive format
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertNotNull(tarOutputStream);
        
        // Test with JAR archive format
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertNotNull(jarOutputStream);
        
        // Test with CPIO archive format
        ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertNotNull(cpioOutputStream);
        
        // Test with unknown archive format
        try {
            ArchiveOutputStream unknownOutputStream = factory.createArchiveOutputStream("unknown", output);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Basic functionality test
        // Create a ByteArrayInputStream with some test data
        byte[] testData = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        
        // Test with ZIP archive format
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(input);
        assertNotNull(zipInputStream);
        
        // Test with JAR archive format
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(input);
        assertNotNull(jarInputStream);
        
        // Test with AR archive format
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(input);
        assertNotNull(arInputStream);
        
        // Test with CPIO archive format
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(input);
        assertNotNull(cpioInputStream);
        
        // Test with DUMP archive format
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(input);
        assertNotNull(dumpInputStream);
        
        // Test with TAR archive format
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(input);
        assertNotNull(tarInputStream);
        
        // Test with unknown archive format
        try {
            ArchiveInputStream unknownInputStream = factory.createArchiveInputStream(input);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

}