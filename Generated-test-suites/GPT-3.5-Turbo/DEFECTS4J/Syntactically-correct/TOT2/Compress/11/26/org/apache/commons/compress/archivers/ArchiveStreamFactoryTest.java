import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiverNameValidInputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);

        // Assert
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverNameValidOutputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();

        // Act
        ArchiveOutputStream aos = factory.createArchiveOutputStream(archiverName, out);

        // Assert
        assertNotNull(aos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;

        // Act
        ArchiveOutputStream aos = factory.createArchiveOutputStream(archiverName, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;

        // Act
        ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "unknown";
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "unknown";
        OutputStream out = new ByteArrayOutputStream();

        // Act
        ArchiveOutputStream aos = factory.createArchiveOutputStream(archiverName, out);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ValidInputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = {80, 75, 3, 4}; // ZIP file signature
        InputStream in = new ByteArrayInputStream(data);

        // Act
        ArchiveInputStream ais = factory.createArchiveInputStream(in);

        // Assert
        assertNotNull(ais);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownSignature() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = {1, 2, 3, 4}; // Unknown signature
        InputStream in = new ByteArrayInputStream(data);

        // Act
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
    }
}