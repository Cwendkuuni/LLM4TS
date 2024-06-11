import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName_ValidInputStream_ReturnsArchiveInputStream() throws ArchiveException {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, in);
        
        // Assert
        Assert.assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() throws ArchiveException {
        // Arrange
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, in);
        
        // Assert
        // Expecting IllegalArgumentException to be thrown
    }
    
    // Additional test cases for different archiver names, input streams and expected outputs
    
    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName_ValidOutputStream_ReturnsArchiveOutputStream() throws ArchiveException {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveOutputStream result = factory.createArchiveOutputStream(archiverName, out);
        
        // Assert
        Assert.assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() throws ArchiveException {
        // Arrange
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveOutputStream result = factory.createArchiveOutputStream(archiverName, out);
        
        // Assert
        // Expecting IllegalArgumentException to be thrown
    }
    
    // Additional test cases for different archiver names, output streams and expected outputs
    
    @Test
    public void testCreateArchiveInputStream_ValidInputStream_ReturnsArchiveInputStream() throws ArchiveException {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        
        // Assert
        Assert.assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        // Arrange
        InputStream in = null;
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        
        // Assert
        // Expecting IllegalArgumentException to be thrown
    }
    
    // Additional test cases for different input streams and expected outputs
    
    // Add more test cases for edge cases and error scenarios

}