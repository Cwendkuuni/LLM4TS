import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveInputStream archiveIn = factory.createArchiveInputStream("zip", in);
            assertNotNull(archiveIn);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveOutputStream archiveOut = factory.createArchiveOutputStream("zip", out);
            assertNotNull(archiveOut);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] signature = new byte[4];
        InputStream in = new ByteArrayInputStream(signature);
        try {
            ArchiveInputStream archiveIn = factory.createArchiveInputStream(in);
            assertNotNull(archiveIn);
        } catch (ArchiveException e) {
            fail("Exception thrown");
        }
    }
}