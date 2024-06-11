import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory = new ArchiveStreamFactory();

    // Test createArchiveInputStream()

    @Test
    public void testCreateArchiveInputStream_ValidArchiverNameAndStream_ReturnsArchiveInputStream() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, in);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        // Arrange
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream(archiverName, in);

        // Assert
        // Expecting IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullStream_ThrowsIllegalArgumentException() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;

        // Act
        factory.createArchiveInputStream(archiverName, in);

        // Assert
        // Expecting IllegalArgumentException
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName_ThrowsArchiveException() {
        // Arrange
        String archiverName = "unknown";
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream(archiverName, in);

        // Assert
        // Expecting ArchiveException
    }

    // Test createArchiveOutputStream()

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverNameAndStream_ReturnsArchiveOutputStream() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();

        // Act
        ArchiveOutputStream result = factory.createArchiveOutputStream(archiverName, out);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName_ThrowsIllegalArgumentException() {
        // Arrange
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();

        // Act
        factory.createArchiveOutputStream(archiverName, out);

        // Assert
        // Expecting IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullStream_ThrowsIllegalArgumentException() {
        // Arrange
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;

        // Act
        factory.createArchiveOutputStream(archiverName, out);

        // Assert
        // Expecting IllegalArgumentException
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName_ThrowsArchiveException() {
        // Arrange
        String archiverName = "unknown";
        OutputStream out = new ByteArrayOutputStream();

        // Act
        factory.createArchiveOutputStream(archiverName, out);

        // Assert
        // Expecting ArchiveException
    }

    // Test createArchiveInputStream() with autodetection

    @Test
    public void testCreateArchiveInputStreamWithAutodetection_ValidStreamZip_ReturnsZipArchiveInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{80, 75, 3, 4, 20, 0, 6, 0});

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection_ValidStreamJar_ReturnsJarArchiveInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{80, 75, 3, 4, 14, 0, 6, 0});

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection_ValidStreamAr_ReturnsArArchiveInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{33, 60, 8, 0, 97, 114});

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection_ValidStreamCpio_ReturnsCpioArchiveInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{070, 063, 132, 000});

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection_ValidStreamDump_ReturnsDumpArchiveInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{139, 80, 78, 71});

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof DumpArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithAutodetection_NullStream_ThrowsIllegalArgumentException() {
        // Arrange
        InputStream in = null;

        // Act
        factory.createArchiveInputStream(in);

        // Assert
        // Expecting IllegalArgumentException
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithAutodetection_UnknownFormat_ThrowsArchiveException() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream(in);

        // Assert
        // Expecting ArchiveException
    }

}