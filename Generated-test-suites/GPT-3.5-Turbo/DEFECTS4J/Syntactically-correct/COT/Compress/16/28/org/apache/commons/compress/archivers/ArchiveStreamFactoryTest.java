import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test creating AR ArchiveInputStream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            assertNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating AR ArchiveInputStream");
        }

        // Test creating ZIP ArchiveInputStream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating ZIP ArchiveInputStream");
        }

        // Test creating TAR ArchiveInputStream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            assertNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating TAR ArchiveInputStream");
        }

        // Test creating JAR ArchiveInputStream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            assertNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating JAR ArchiveInputStream");
        }

        // Test creating CPIO ArchiveInputStream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating CPIO ArchiveInputStream");
        }

        // Test creating DUMP ArchiveInputStream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            assertNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating DUMP ArchiveInputStream");
        }

        // Test creating ArchiveInputStream with unknown archiver name
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream("unknown", null);
            assertNull(inputStream);
            fail("Exception not thrown when creating ArchiveInputStream with unknown archiver name");
        } catch (ArchiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Exception thrown when creating ArchiveInputStream with unknown archiver name");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test creating AR ArchiveOutputStream
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            assertNull(outputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating AR ArchiveOutputStream");
        }

        // Test creating ZIP ArchiveOutputStream
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(outputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating ZIP ArchiveOutputStream");
        }

        // Test creating TAR ArchiveOutputStream
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            assertNull(outputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating TAR ArchiveOutputStream");
        }

        // Test creating JAR ArchiveOutputStream
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            assertNull(outputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating JAR ArchiveOutputStream");
        }

        // Test creating CPIO ArchiveOutputStream
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(outputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating CPIO ArchiveOutputStream");
        }

        // Test creating ArchiveOutputStream with unknown archiver name
        try {
            ArchiveOutputStream outputStream = factory.createArchiveOutputStream("unknown", null);
            assertNull(outputStream);
            fail("Exception not thrown when creating ArchiveOutputStream with unknown archiver name");
        } catch (ArchiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Exception thrown when creating ArchiveOutputStream with unknown archiver name");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test creating ArchiveInputStream with null stream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(null);
            assertNull(inputStream);
            fail("Exception not thrown when creating ArchiveInputStream with null stream");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Exception thrown when creating ArchiveInputStream with null stream");
        }

        // Test creating ArchiveInputStream without mark support
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNull(inputStream);
            fail("Exception not thrown when creating ArchiveInputStream without mark support");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Exception thrown when creating ArchiveInputStream without mark support");
        }

        // Test creating ZIP ArchiveInputStream
        try {
            byte[] zipSignature = {(byte) 0x50, (byte) 0x4B};
            ByteArrayInputStream in = new ByteArrayInputStream(zipSignature);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNotNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating ZIP ArchiveInputStream with autodetection");
        }

        // Test creating JAR ArchiveInputStream
        try {
            byte[] jarSignature = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04};
            ByteArrayInputStream in = new ByteArrayInputStream(jarSignature);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNotNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating JAR ArchiveInputStream with autodetection");
        }

        // Test creating AR ArchiveInputStream
        try {
            byte[] arSignature = {(byte) 0x21, (byte) 0x3C, (byte) 0x61, (byte) 0x72};
            ByteArrayInputStream in = new ByteArrayInputStream(arSignature);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNotNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating AR ArchiveInputStream with autodetection");
        }

        // Test creating CPIO ArchiveInputStream
        try {
            byte[] cpioSignature = {(byte) 0x30, (byte) 0x37, (byte) 0x30, (byte) 0x37};
            ByteArrayInputStream in = new ByteArrayInputStream(cpioSignature);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNotNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating CPIO ArchiveInputStream with autodetection");
        }

        // Test creating DUMP ArchiveInputStream
        try {
            byte[] dumpSignature = {(byte) 0x1F, (byte) 0x8B, (byte) 0x08, (byte) 0x00};
            ByteArrayInputStream in = new ByteArrayInputStream(dumpSignature);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNotNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating DUMP ArchiveInputStream with autodetection");
        }

        // Test creating TAR ArchiveInputStream
        try {
            byte[] tarSignature = {(byte) 0x75, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72};
            ByteArrayInputStream in = new ByteArrayInputStream(tarSignature);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNotNull(inputStream);
        } catch (Exception e) {
            fail("Exception thrown when creating TAR ArchiveInputStream with autodetection");
        }

        // Test creating ArchiveInputStream with unknown signature
        try {
            byte[] unknownSignature = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78};
            ByteArrayInputStream in = new ByteArrayInputStream(unknownSignature);
            ArchiveInputStream inputStream = factory.createArchiveInputStream(in);
            assertNull(inputStream);
            fail("Exception not thrown when creating ArchiveInputStream with unknown signature");
        } catch (ArchiveException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Exception thrown when creating ArchiveInputStream with unknown signature");
        }
    }

}