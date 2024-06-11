
import org.junit.Test;
import org.junit.Assert;

public class ArchiveStreamFactoryTest {

    @Test
    public void createArchiveInputStream_InvalidArchiverName_ThrowsArchiveException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        try {
            factory.createArchiveInputStream("invalidArchiverName", inputStream);
            Assert.fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            // Exception is expected
        }
    }

    @Test
    public void createArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream("zip", null);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void createArchiveOutputStream_InvalidArchiverName_ThrowsArchiveException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            factory.createArchiveOutputStream("invalidArchiverName", outputStream);
            Assert.fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            // Exception is expected
        }
    }

    @Test
    public void createArchiveOutputStream_NullOutputStream_ThrowsIllegalArgumentException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream("zip", null);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void createArchiveInputStream_ArchiveNameAR_ReturnsArArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("ar", inputStream);
            Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void createArchiveInputStream_ArchiveNameARJ_ReturnsArjArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("arj", inputStream);
            Assert.assertTrue(archiveInputStream instanceof ArjArchiveInputStream);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // More test cases for each method...
    
    // ...

}

