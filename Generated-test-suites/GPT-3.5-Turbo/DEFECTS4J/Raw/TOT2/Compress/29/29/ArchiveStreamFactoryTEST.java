import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test creating ArchiveInputStream with AR archiver
        InputStream in = new ByteArrayInputStream(new byte[]{});
        ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(arInputStream);

        // Test creating ArchiveInputStream with unknown archiver
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
            fail("Expected exception for unknown archiver");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveInputStream with null InputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream("ar", null);
            fail("Expected exception for null InputStream");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test creating ArchiveOutputStream with ZIP archiver
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(zipOutputStream);

        // Test creating ArchiveOutputStream with unknown archiver
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
            fail("Expected exception for unknown archiver");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveOutputStream with null OutputStream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
            fail("Expected exception for null OutputStream");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws Exception {
        // Test creating ArchiveInputStream with ZIP format using auto-detect
        byte[] data = {80, 75, 3, 4}; // ZIP file signature
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream zipInputStream = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(zipInputStream);

        // Test creating ArchiveInputStream with unknown format using auto-detect
        byte[] unknownData = {0, 1, 2, 3}; // Unknown file signature
        ByteArrayInputStream unknownIn = new ByteArrayInputStream(unknownData);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(unknownIn);
            fail("Expected exception for unknown format");
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveInputStream with null InputStream using auto-detect
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected exception for null InputStream");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}