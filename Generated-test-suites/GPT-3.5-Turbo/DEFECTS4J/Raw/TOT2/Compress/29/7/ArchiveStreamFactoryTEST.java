import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Typical use case
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);

        // Edge case: null archiverName
        assertNull(factory.createArchiveInputStream(null, in));

        // Error scenario: null InputStream
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Typical use case
        OutputStream out = Mockito.mock(OutputStream.class);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(aos);

        // Edge case: null archiverName
        assertNull(factory.createArchiveOutputStream(null, out));

        // Error scenario: null OutputStream
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        // Typical use case
        byte[] zipSignature = new byte[]{80, 75, 3, 4};
        ByteArrayInputStream in = new ByteArrayInputStream(zipSignature);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);

        // Edge case: unknown signature
        byte[] unknownSig = new byte[]{1, 2, 3, 4, 5};
        in = new ByteArrayInputStream(unknownSig);
        assertNull(factory.createArchiveInputStream(in));

        // Error scenario: null InputStream
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testGetEntryEncoding() {
        // Typical use case
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertNull(factory.getEntryEncoding());

        // Edge case: setEntryEncoding not used
        assertNull(factory.getEntryEncoding());

        // Error scenario: setEntryEncoding used
        factory.setEntryEncoding("UTF-8");
        assertEquals("UTF-8", factory.getEntryEncoding());
    }

}