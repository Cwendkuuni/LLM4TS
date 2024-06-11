import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", inputStream);

        // Assert
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Act
        factory.createArchiveInputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", outputStream);

        // Assert
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        factory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Act
        factory.createArchiveOutputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        factory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

        // Assert
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectWithNullInputStream() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Act
        factory.createArchiveInputStream(null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectWithUnknownSignature() {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream(inputStream);
    }
}