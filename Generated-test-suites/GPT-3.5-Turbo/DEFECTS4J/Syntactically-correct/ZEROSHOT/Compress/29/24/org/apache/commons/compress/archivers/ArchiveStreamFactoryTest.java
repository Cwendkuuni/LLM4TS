import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test for AR
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[1]));
        // Test for ARJ
        factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[1]));
        // Test for ZIP
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[1]));
        // Test for TAR
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[1]));
        // Test for JAR
        factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[1]));
        // Test for CPIO
        factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[1]));
        // Test for DUMP
        factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[1]));
        // Test for SEVEN_Z
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[1]));
            fail("Expected StreamingNotSupportedException to be thrown");
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test for AR
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        // Test for ZIP
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        // Test for TAR
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        // Test for JAR
        factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        // Test for CPIO
        factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        // Test for SEVEN_Z
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayOutputStream());
            fail("Expected StreamingNotSupportedException to be thrown");
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutodetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test for ZIP
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04}));
        // Test for JAR
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04}));
        // Test for AR
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'!', '<', 'a', 'r', 'c', 'h', '>'}));
        // Test for CPIO
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'0', '7', '0', '7'}));
        // Test for ARJ
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'R', 'a', 'r', '!', 0x1A, 0x07, 0x0D, 0x0A}));
        // Test for SEVEN_Z
        try {
            factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'7', 'z'}));
            fail("Expected StreamingNotSupportedException to be thrown");
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }
}