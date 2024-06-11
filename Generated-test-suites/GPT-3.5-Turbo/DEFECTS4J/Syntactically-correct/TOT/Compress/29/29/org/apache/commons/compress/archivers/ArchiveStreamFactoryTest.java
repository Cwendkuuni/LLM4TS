import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveFactory;

    @Before
    public void setUp() {
        archiveFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_Ar_ValidInputStream() {
        // Test ar archive format with a valid input stream
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        
        ArchiveInputStream archiveInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        
        // Assert that the created archive input stream is not null
        assertNotNull(archiveInputStream);
        
        // Assert that the created archive input stream is of type ArArchiveInputStream
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Ar_NullInputStream() {
        // Test ar archive format with a null input stream
        InputStream inputStream = null;
        
        // Assert that creating an archive input stream with a null input stream throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            archiveFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_Ar_ValidOutputStream() {
        // Test ar archive format with a valid output stream
        OutputStream outputStream = new ByteArrayOutputStream();
        
        ArchiveOutputStream archiveOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        
        // Assert that the created archive output stream is not null
        assertNotNull(archiveOutputStream);
        
        // Assert that the created archive output stream is of type ArArchiveOutputStream
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Ar_NullOutputStream() {
        // Test ar archive format with a null output stream
        OutputStream outputStream = null;
        
        // Assert that creating an archive output stream with a null output stream throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        });
    }

    // Continue the test cases for other archive formats...

}