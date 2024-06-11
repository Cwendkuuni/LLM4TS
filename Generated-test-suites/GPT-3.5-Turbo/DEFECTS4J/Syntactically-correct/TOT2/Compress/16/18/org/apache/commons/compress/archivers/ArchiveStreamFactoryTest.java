import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() {
        // Test for AR format
        try {
            archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, System.in);
        } catch (ArchiveException e) {
            fail("Exception should not be thrown for AR format");
        }

        // Test for unknown format
        try {
            archiveStreamFactory.createArchiveInputStream("invalid", System.in);
            fail("Exception should be thrown for unknown format");
        } catch (ArchiveException e) {
            // Expected exception
        }

        // Test for null archiver name
        try {
            archiveStreamFactory.createArchiveInputStream(null, System.in);
            fail("Exception should be thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test for null input stream
        try {
            archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Exception should be thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test for ZIP format
        try {
            archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, System.out);
        } catch (ArchiveException e) {
            fail("Exception should not be thrown for ZIP format");
        }

        // Test for unknown format
        try {
            archiveStreamFactory.createArchiveOutputStream("invalid", System.out);
            fail("Exception should be thrown for unknown format");
        } catch (ArchiveException e) {
            // Expected exception
        }

        // Test for null archiver name
        try {
            archiveStreamFactory.createArchiveOutputStream(null, System.out);
            fail("Exception should be thrown for null archiver name");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test for null output stream
        try {
            archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            fail("Exception should be thrown for null output stream");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        // Test for ZIP format
        try {
            archiveStreamFactory.createArchiveInputStream(System.in);
        } catch (ArchiveException e) {
            fail("Exception should not be thrown for ZIP format auto-detection");
        }

        // Test for unsupported mark
        try {
            archiveStreamFactory.createArchiveInputStream(System.out);
            fail("Exception should be thrown for stream that does not support mark");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test for unknown format
        try {
            byte[] data = new byte[16];
            InputStream inputStream = new ByteArrayInputStream(data);
            archiveStreamFactory.createArchiveInputStream(inputStream);
            fail("Exception should be thrown for unknown format");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }
}