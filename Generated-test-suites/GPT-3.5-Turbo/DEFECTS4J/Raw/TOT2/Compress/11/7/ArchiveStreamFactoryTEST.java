import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiveNameAndInputStream_ReturnsArchiveInputStream() throws Exception {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream mockInputStream = mock(InputStream.class);
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream("zip", mockInputStream);
        
        // Assert
        assertEquals(ZipArchiveInputStream.class, result.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveNameAndOutputStream_ReturnsArchiveOutputStream() throws Exception {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream mockOutputStream = mock(OutputStream.class);
        
        // Act
        ArchiveOutputStream result = factory.createArchiveOutputStream("tar", mockOutputStream);
        
        // Assert
        assertEquals(TarArchiveOutputStream.class, result.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiveNameAndInputStream_ThrowsArchiveException() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream mockInputStream = mock(InputStream.class);
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream("invalid", mockInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiveNameAndInputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiveNameAndOutputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Act
        ArchiveOutputStream result = factory.createArchiveOutputStream(null, null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectArchiveFromStreamSignature_ReturnsArchiveInputStream() throws Exception {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] zipSignature = "PK".getBytes(); // Zip file signature
        InputStream inputStream = new ByteArrayInputStream(zipSignature);
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(inputStream);
        
        // Assert
        assertEquals(ZipArchiveInputStream.class, result.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownStreamSignature_ThrowsArchiveException() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] unknownSignature = "UNKNOWN".getBytes();
        InputStream inputStream = new ByteArrayInputStream(unknownSignature);
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(inputStream);
    }

}