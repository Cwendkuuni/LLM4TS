import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test for known archiver names
        try {
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, System.in));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, System.in));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, System.in));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, System.in));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, System.in));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, System.in));
        } catch (ArchiveException ex) {
            fail("Exception thrown for known archiver names");
        }
        
        // Test for null input stream
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Exception not thrown for null input stream");
        } catch (IllegalArgumentException ex) {
            // Pass
        } catch (ArchiveException ex) {
            fail("Incorrect exception thrown for null input stream");
        }
        
        // Test for null archiver name
        try {
            factory.createArchiveInputStream(null, System.in);
            fail("Exception not thrown for null archiver name");
        } catch (IllegalArgumentException ex) {
            // Pass
        } catch (ArchiveException ex) {
            fail("Incorrect exception thrown for null archiver name");
        }
        
        // Test for unknown archiver name
        try {
            factory.createArchiveInputStream("invalid", System.in);
            fail("Exception not thrown for unknown archiver name");
        } catch (ArchiveException ex) {
            // Pass
        } catch (IllegalArgumentException ex) {
            fail("Incorrect exception thrown for unknown archiver name");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test for known archiver names
        try {
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, System.out));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, System.out));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, System.out));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, System.out));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, System.out));
        } catch (ArchiveException ex) {
            fail("Exception thrown for known archiver names");
        }
        
        // Test for null output stream
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Exception not thrown for null output stream");
        } catch (IllegalArgumentException ex) {
            // Pass
        } catch (ArchiveException ex) {
            fail("Incorrect exception thrown for null output stream");
        }
        
        // Test for null archiver name
        try {
            factory.createArchiveOutputStream(null, System.out);
            fail("Exception not thrown for null archiver name");
        } catch (IllegalArgumentException ex) {
            // Pass
        } catch (ArchiveException ex) {
            fail("Incorrect exception thrown for null archiver name");
        }
        
        // Test for unknown archiver name
        try {
            factory.createArchiveOutputStream("invalid", System.out);
            fail("Exception not thrown for unknown archiver name");
        } catch (ArchiveException ex) {
            // Pass
        } catch (IllegalArgumentException ex) {
            fail("Incorrect exception thrown for unknown archiver name");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test for known archive types
        try {
            assertNotNull(factory.createArchiveInputStream(System.in));
        } catch (ArchiveException ex) {
            fail("Exception thrown for known archive types");
        }
        
        // Test for null input stream
        try {
            factory.createArchiveInputStream(null);
            fail("Exception not thrown for null input stream");
        } catch (IllegalArgumentException ex) {
            // Pass
        } catch (ArchiveException ex) {
            fail("Incorrect exception thrown for null input stream");
        }
        
        // Test for input stream that does not support mark
        try {
            factory.createArchiveInputStream(System.out);
            fail("Exception not thrown for input stream that does not support mark");
        } catch (IllegalArgumentException ex) {
            // Pass
        } catch (ArchiveException ex) {
            fail("Incorrect exception thrown for input stream that does not support mark");
        }
    }
}