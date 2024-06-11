import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test for AR archive format
        try {
            ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            assertNull(arInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test for ZIP archive format
        try {
            ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(zipInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test for TAR archive format
        try {
            ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            assertNull(tarInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test for JAR archive format
        try {
            ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            assertNull(jarInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test for CPIO archive format
        try {
            ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(cpioInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test for DUMP archive format
        try {
            ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            assertNull(dumpInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test for unknown archive format
        try {
            ArchiveInputStream unknownInputStream = factory.createArchiveInputStream("unknown", null);
            assertNull(unknownInputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test for AR archive format
        try {
            ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            assertNull(arOutputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }

        // Test for ZIP archive format
        try {
            ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(zipOutputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }

        // Test for TAR archive format
        try {
            ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            assertNull(tarOutputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }

        // Test for JAR archive format
        try {
            ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            assertNull(jarOutputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }

        // Test for CPIO archive format
        try {
            ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(cpioOutputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }

        // Test for unknown archive format
        try {
            ArchiveOutputStream unknownOutputStream = factory.createArchiveOutputStream("unknown", null);
            assertNull(unknownOutputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test for null input stream
        try {
            ArchiveInputStream inputStream = factory.createArchiveInputStream(null);
            assertNull(inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }
}