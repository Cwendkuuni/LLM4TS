import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void shouldCreateArchiveInputStreamForValidArchiverNames() throws IOException, ArchiveException {
        // Arrange
        String[] archiverNames = { ArchiveStreamFactory.AR, ArchiveStreamFactory.ZIP, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.CPIO };

        // Act
        for (String archiverName : archiverNames) {
            InputStream inputStream = mock(InputStream.class);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);

            // Assert
            assertNotNull(archiveInputStream);
        }
    }

    @Test
    public void shouldCreateArchiveOutputStreamForValidArchiverNames() throws IOException, ArchiveException {
        // Arrange
        String[] archiverNames = { ArchiveStreamFactory.AR, ArchiveStreamFactory.ZIP, ArchiveStreamFactory.TAR,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.CPIO };
        
        // Act
        for (String archiverName : archiverNames) {
            OutputStream outputStream = mock(OutputStream.class);
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);

            // Assert
            assertNotNull(archiveOutputStream);
        }
    }

    @Test
    public void shouldThrowExceptionForNullArchiverNameInCreateArchiveInputStream() throws IOException {
        // Arrange
        InputStream inputStream = mock(InputStream.class);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            factory.createArchiveInputStream(null, inputStream);
        });
    }

    @Test
    public void shouldThrowExceptionForNullArchiverNameInCreateArchiveOutputStream() throws IOException {
        // Arrange
        OutputStream outputStream = mock(OutputStream.class);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            factory.createArchiveInputStream(null, outputStream);
        });
    }

    @Test
    public void shouldThrowExceptionForNullInputStreamInCreateArchiveInputStream() throws ArchiveException {
        // Arrange
        String archiverName = ArchiveStreamFactory.AR;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            factory.createArchiveInputStream(archiverName, null);
        });
    }

    @Test
    public void shouldThrowExceptionForNullOutputStreamInCreateArchiveOutputStream() throws ArchiveException {
        // Arrange
        String archiverName = ArchiveStreamFactory.AR;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            factory.createArchiveOutputStream(archiverName, null);
        });
    }

    @Test
    public void shouldCreateArchiveInputStreamForInputStreamWithArchiverSignature() throws IOException, ArchiveException {
        // Arrange
        byte[] zipSignature = { 0x50, 0x4B, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(zipSignature);

        // Act
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

        // Assert
        assertNotNull(archiveInputStream);
    }

    @Test
    public void shouldCreateArchiveOutputStreamForZipOutputStream() throws IOException, ArchiveException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP,
                outputStream);
        ZipEntry zipEntry = new ZipEntry("test.txt");

        // Act
        archiveOutputStream.putArchiveEntry(zipEntry);
        archiveOutputStream.write("Test Data".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Assert
        assertTrue(outputStream.toByteArray().length > 0);
    }
    
    @Test
    public void shouldCreateArchiveInputStreamForInputStreamWithTarHeaderSignature() throws IOException, ArchiveException {
        // Arrange
        byte[] tarHeaderSignature = new byte[512];
        TarEntry.createEntry("test.txt", true);
        InputStream inputStream = new ByteArrayInputStream(tarHeaderSignature);

        // Act
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

        // Assert
        assertNotNull(archiveInputStream);
    }
    
    @Test
    public void shouldThrowExceptionForInputStreamWithoutArchiverSignature() throws IOException {
        // Arrange
        byte[] invalidSignature = { 0x01 };
        InputStream inputStream = new ByteArrayInputStream(invalidSignature);

        // Assert
        assertThrows(ArchiveException.class, () -> {
            // Act
            factory.createArchiveInputStream(inputStream);
        });
    }

    @Test
    public void shouldThrowExceptionForInvalidArchiverNameInCreateArchiveInputStream() throws IOException {
        // Arrange
        String invalidArchiverName = "invalid";
        InputStream inputStream = mock(InputStream.class);

        // Assert
        assertThrows(ArchiveException.class, () -> {
            // Act
            factory.createArchiveInputStream(invalidArchiverName, inputStream);
        });
    }

    @Test
    public void shouldThrowExceptionForInvalidArchiverNameInCreateArchiveOutputStream() throws IOException {
        // Arrange
        String invalidArchiverName = "invalid";
        OutputStream outputStream = mock(OutputStream.class);

        // Assert
        assertThrows(ArchiveException.class, () -> {
            // Act
            factory.createArchiveOutputStream(invalidArchiverName, outputStream);
        });
    }

}