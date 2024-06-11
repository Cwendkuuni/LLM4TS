package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void createArchiveInputStream_ValidArchiverNameAndInputStream_ReturnsArchiveInputStream() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, inputStream);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, inputStream);
        
        // Assert
        // Expects IllegalArgumentException
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "zip";
        InputStream inputStream = null;
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, inputStream);
        
        // Assert
        // Expects IllegalArgumentException
    }
    
    @Test(expected = ArchiveException.class)
    public void createArchiveInputStream_UnknownArchiverName_ThrowsArchiveException() throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        
        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, inputStream);
        
        // Assert
        // Expects ArchiveException
    }
    
    // Repeat similar test methods for other methods in ArchiveStreamFactory class

}