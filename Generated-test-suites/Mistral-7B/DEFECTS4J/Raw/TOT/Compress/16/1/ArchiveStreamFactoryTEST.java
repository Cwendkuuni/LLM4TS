import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream from a known archive format.
     */
    @Test
    public void testCreateArchiveInputStreamFromKnownFormat() {
        final String archiveName = "zip";
        final byte[] zipSignature = new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x6B, (byte) 0x75, (byte) 0x72, (byte) 0x21, (byte) 0x1A, (byte) 0x07, (byte) 0x02, (byte) 0x00, (byte) 0x01};

        try (InputStream inputStream = new ByteArrayInputStream(zipSignature);
             ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveName, inputStream)) {
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        }
    }

    /**
     * Test creating an archive input stream from an unknown archive format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownFormat() {
        final String archiveName = "unknown";
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x00});

        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveName, inputStream);
    }

    /**
     * Test creating an archive output stream from a known archive format.
     */
    @Test
    public void testCreateArchiveOutputStreamFromKnownFormat() {
        final String archiveName = "tar";
        final OutputStream outputStream = new ByteArrayOutputStream();

        try (ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiveName, outputStream)) {
            assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
        }
    }

    /**
     * Test creating an archive output stream from an unknown archive format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownFormat() {
        final String archiveName = "unknown";
        final OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiveName, outputStream);
    }

    /**
     * Test creating an archive input stream from an input stream with an unknown archive format.
     */
    @Test
    public void testCreateArchiveInputStreamFromUnknownFormatFromStream() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A});

        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
            fail("Expected ArchiveException.");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    /**
     * Test creating an archive input stream from an input stream with a known archive format.
     */
    @Test
    public void testCreateArchiveInputStreamFromKnownFormatFromStream() {
        final InputStream zipInputStream = new ByteArrayInputStream(zipSignature);

        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("zip", zipInputStream);
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException.");
        }
    }
}