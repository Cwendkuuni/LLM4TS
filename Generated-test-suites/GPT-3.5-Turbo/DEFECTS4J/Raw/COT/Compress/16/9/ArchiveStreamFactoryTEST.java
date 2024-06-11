import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test case 1
        String archiverName = "zip";
        InputStream in = new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04});
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(ais);
        assertEquals(ZipArchiveInputStream.class, ais.getClass());

        // Test case 2
        archiverName = "tar";
        in = new ByteArrayInputStream(new byte[]{0x75, 0x73, 0x74, 0x61, 0x72});
        ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(ais);
        assertEquals(TarArchiveInputStream.class, ais.getClass());

        // Test case 3 (invalid archiver name)
        archiverName = "rar";
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: rar not found.", e.getMessage());
        }

        // Test case 4 (null archiver name)
        archiverName = null;
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }

        // Test case 5 (null input stream)
        archiverName = "zip";
        in = null;
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
}