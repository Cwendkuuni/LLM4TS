import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test case 1: Test for known archiver name "ar" and a valid input stream
        String archiverName = "ar";
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        assertNotNull(archiveInputStream);
        
        // Test case 2: Test for unknown archiver name and a valid input stream
        String unknownArchiverName = "unknown";
        try {
            ArchiveInputStream unknownArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(unknownArchiverName, in);
            fail("ArchiveException should be thrown for unknown archiver name");
        } catch (ArchiveException e) {
            // Exception was expected
        }
        
        // Test case 3: Test for known archiver name with null input stream
        try {
            ArchiveInputStream nullInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
            fail("IllegalArgumentException should be thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // Exception was expected
        }
    }
}