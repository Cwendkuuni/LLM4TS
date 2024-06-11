import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_withValidArchiverNameAndInputStream_shouldCreateArchiveInputStream() throws Exception {
        // Arrange
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        // Assert
        Assert.assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws Exception {
        // Arrange
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        // Assert: Expecting IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws Exception {
        // Arrange
        String archiverName = "zip";
        InputStream inputStream = null;

        // Act
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        // Assert: Expecting IllegalArgumentException
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withUnknownArchiverName_shouldThrowArchiveException() throws Exception {
        // Arrange
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        // Assert: Expecting ArchiveException
    }

    @Test
    public void testCreateArchiveOutputStream_withValidArchiverNameAndOutputStream_shouldCreateArchiveOutputStream() throws Exception {
        // Arrange
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);

        // Assert
        Assert.assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws Exception {
        // Arrange
        String archiverName = null;
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);

        // Assert: Expecting IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream_shouldThrowIllegalArgumentException() throws Exception {
        // Arrange
        String archiverName = "zip";
        OutputStream outputStream = null;

        // Act
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);

        // Assert: Expecting IllegalArgumentException
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_withUnknownArchiverName_shouldThrowArchiveException() throws Exception {
        // Arrange
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);

        // Assert: Expecting ArchiveException
    }

    @Test
    public void testCreateArchiveInputStream_withValidInputStream_shouldCreateArchiveInputStream() throws Exception {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert
        Assert.assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws Exception {
        // Arrange
        InputStream inputStream = null;

        // Act
        new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert: Expecting IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withInputStreamThatDoesNotSupportMark_shouldThrowIllegalArgumentException() throws Exception {
        // Arrange
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        // Act
        new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert: Expecting IllegalArgumentException
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withUnknownArchiveFormat_shouldThrowArchiveException() throws Exception {
        // Arrange
        byte[] invalidArchiveData = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(invalidArchiveData);

        // Act
        new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert: Expecting ArchiveException
    }

    @Test
    public void testCreateArchiveInputStream_withValidArchiveData_shouldCreateArchiveInputStream() throws Exception {
        // Arrange
        byte[] validZipArchiveData = new byte[] { 80, 75, 3, 4 };
        InputStream inputStream = new ByteArrayInputStream(validZipArchiveData);

        // Act
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert
        Assert.assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withValidDumpArchiveData_shouldCreateArchiveInputStream() throws Exception {
        // Arrange
        byte[] validDumpArchiveData = new byte[] { 68, 85, 77, 80 };
        InputStream inputStream = new ByteArrayInputStream(validDumpArchiveData);

        // Act
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert
        Assert.assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withValidTarArchiveData_shouldCreateArchiveInputStream() throws Exception {
        // Arrange
        byte[] validTarArchiveData = new byte[512];
        InputStream inputStream = new ByteArrayInputStream(validTarArchiveData);

        // Act
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert
        Assert.assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidTarArchiveData_shouldThrowArchiveException() throws Exception {
        // Arrange
        byte[] invalidTarArchiveData = new byte[] { 80, 75, 3, 4 };
        InputStream inputStream = new ByteArrayInputStream(invalidTarArchiveData);

        // Act
        new ArchiveStreamFactory().createArchiveInputStream(inputStream);

        // Assert: Expecting ArchiveException
    }
}