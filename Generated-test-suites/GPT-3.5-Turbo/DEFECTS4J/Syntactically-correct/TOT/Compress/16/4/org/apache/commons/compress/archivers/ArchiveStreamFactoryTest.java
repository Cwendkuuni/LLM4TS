import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Typical use case - create a Zip ArchiveInputStream
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertNotNull(zipArchiveInputStream);

        // Edge case - create an Ar ArchiveInputStream
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertNotNull(arArchiveInputStream);

        // Error scenario - unknown archiver name
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream unknownArchiveInputStream = factory.createArchiveInputStream("unknown", unknownInputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }

        // Error scenario - null archiver name
        try {
            ArchiveInputStream nullArchiverInputStream = factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Error scenario - null input stream
        try {
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Typical use case - create a Zip ArchiveOutputStream
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertNotNull(zipArchiveOutputStream);

        // Edge case - create an Ar ArchiveOutputStream
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertNotNull(arArchiveOutputStream);

        // Error scenario - unknown archiver name
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream unknownArchiveOutputStream = factory.createArchiveOutputStream("unknown", unknownOutputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }

        // Error scenario - null archiver name
        try {
            ArchiveOutputStream nullArchiverOutputStream = factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Error scenario - null output stream
        try {
            ArchiveOutputStream nullOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Typical use case - create a Zip ArchiveInputStream
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipArchiveInputStream = factory.createArchiveInputStream(zipInputStream);
        assertNotNull(zipArchiveInputStream);

        // Edge case - create an Ar ArchiveInputStream
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arArchiveInputStream = factory.createArchiveInputStream(arInputStream);
        assertNotNull(arArchiveInputStream);

        // Error scenario - unsupported mark
        InputStream unsupportedMarkInputStream = new ByteArrayInputStream(new byte[0]);
        unsupportedMarkInputStream.markSupported = false;
        try {
            ArchiveInputStream unsupportedMarkArchiveInputStream = factory.createArchiveInputStream(unsupportedMarkInputStream);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Error scenario - unknown archive type
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream unknownArchiveInputStream = factory.createArchiveInputStream(unknownInputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }
}