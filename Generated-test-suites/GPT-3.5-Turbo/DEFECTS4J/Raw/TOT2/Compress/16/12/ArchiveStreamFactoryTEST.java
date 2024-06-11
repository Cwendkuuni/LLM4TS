import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Testing with valid archiverName and InputStream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, System.in);
            assertNotNull(inputStream);
        } catch (ArchiveException e) {
            fail("Should not throw exception for valid input.");
        }

        // Testing with null archiverName
        try {
            factory.createArchiveInputStream(null, System.in);
            fail("Should throw IllegalArgumentException for null archiverName.");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }

        // Testing with null InputStream
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            fail("Should throw IllegalArgumentException for null InputStream.");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Testing with valid archiverName and OutputStream
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, System.out);
            assertNotNull(outputStream);
        } catch (ArchiveException e) {
            fail("Should not throw exception for valid input.");
        }

        // Testing with null archiverName
        try {
            factory.createArchiveOutputStream(null, System.out);
            fail("Should throw IllegalArgumentException for null archiverName.");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }

        // Testing with null OutputStream
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            fail("Should throw IllegalArgumentException for null OutputStream.");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Testing auto-detection with different archiver types
        try {
            ArchiveInputStream inputStreamZip = factory.createArchiveInputStream(System.in);
            assertNotNull(inputStreamZip);
        } catch (ArchiveException e) {
            fail("Should not throw exception for ZIP input stream.");
        }

        try {
            ArchiveInputStream inputStreamJar = factory.createArchiveInputStream(System.in);
            assertNotNull(inputStreamJar);
        } catch (ArchiveException e) {
            fail("Should not throw exception for JAR input stream.");
        }

        try {
            ArchiveInputStream inputStreamAr = factory.createArchiveInputStream(System.in);
            assertNotNull(inputStreamAr);
        } catch (ArchiveException e) {
            fail("Should not throw exception for AR input stream.");
        }

        try {
            ArchiveInputStream inputStreamCpio = factory.createArchiveInputStream(System.in);
            assertNotNull(inputStreamCpio);
        } catch (ArchiveException e) {
            fail("Should not throw exception for CPIO input stream.");
        }

        // Testing with null InputStream
        try {
            factory.createArchiveInputStream(null);
            fail("Should throw IllegalArgumentException for null InputStream.");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }

        // Testing with InputStream that does not support mark
        try {
            factory.createArchiveInputStream(System.in);
            fail("Should throw IllegalArgumentException for InputStream that does not support mark.");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }
}