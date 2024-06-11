
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        try {
            ArchiveInputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test ZIP archive format
        try {
            ArchiveInputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test TAR archive format
        try {
            ArchiveInputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test JAR archive format
        try {
            ArchiveInputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test CPIO archive format
        try {
            ArchiveInputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test DUMP archive format
        try {
            ArchiveInputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test unknown archive format
        try {
            ArchiveInputStream unknownInput = factory.createArchiveInputStream("unknown", null);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Test passed
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        try {
            ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test ZIP archive format
        try {
            ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test TAR archive format
        try {
            ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test JAR archive format
        try {
            ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test CPIO archive format
        try {
            ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
        
        // Test unknown archive format
        try {
            ArchiveOutputStream unknownOutput = factory.createArchiveOutputStream("unknown", null);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Test passed
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test null stream
        try {
            ArchiveInputStream input = factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException");
        }
    }
}
