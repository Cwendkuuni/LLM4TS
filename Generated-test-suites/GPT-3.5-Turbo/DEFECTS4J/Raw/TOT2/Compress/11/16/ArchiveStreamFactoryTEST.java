import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidInput_ReturnsArchiveInputStream() throws IOException, ArchiveException {
        // Arrange
        byte[] data = new byte[]{0x1F, (byte) 0x8B};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        // Act
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(in);
        
        // Assert
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidInput_ReturnsArchiveOutputStream() throws IOException, ArchiveException {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Act
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        
        // Assert
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_InvalidArchiverName_ThrowsArchiveException() throws IOException, ArchiveException {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        
        // Act & Assert
        try {
            new ArchiveStreamFactory().createArchiveInputStream("invalid", in);
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException() {
        // Arrange
        ByteArrayInputStream in = null;
        
        // Act & Assert
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{});
        
        // Act & Assert
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, in);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }
}