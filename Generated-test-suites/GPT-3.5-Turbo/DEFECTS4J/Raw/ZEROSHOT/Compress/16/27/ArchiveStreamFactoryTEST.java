import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "zip";
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream result = factory.createArchiveInputStream(archiverName, in);
            assertNotNull(result);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "zip";
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream result = factory.createArchiveOutputStream(archiverName, out);
            assertNotNull(result);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream result = factory.createArchiveInputStream(in);
            assertNotNull(result);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }
    }
    
}