import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    // Test createArchiveInputStream with valid archiverName
    @Test
    public void testCreateArchiveInputStream_ValidArchiverName() {
        // create input stream
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            assertNotNull(archiveInputStream);
        } catch (Exception e) {
            fail("Exception not expected here");
        }
    }
    
    // Test createArchiveInputStream with null archiverName
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() {
        // create input stream
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null, inputStream);
    }
    
    // Test createArchiveOutputStream with valid archiverName
    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName() {
        // create output stream
        OutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            assertNotNull(archiveOutputStream);
        } catch (Exception e) {
            fail("Exception not expected here");
        }
    }
    
    // Test createArchiveOutputStream with null archiverName
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() {
        // create output stream
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null, outputStream);
    }
    
    // Test createArchiveInputStream with autodetect
    @Test
    public void testCreateArchiveInputStream_Autodetect() {
        // create input stream
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertNotNull(archiveInputStream);
        } catch (Exception e) {
            fail("Exception not expected here");
        }
    }
    
    // Test createArchiveInputStream with unsupported mark
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_UnsupportedMark() {
        // create input stream without mark support
        InputStream inputStream = new FileInputStream("file.txt");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(inputStream);
    }
}