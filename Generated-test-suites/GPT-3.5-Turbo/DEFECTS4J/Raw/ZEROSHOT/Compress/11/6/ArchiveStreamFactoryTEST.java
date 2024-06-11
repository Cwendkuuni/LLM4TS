import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArArchiverNameAndInputStream_ReturnsArArchiveInputStream()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, in);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArArchiverNameAndInputStream_ThrowsIllegalArgumentException()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_ArArchiverNameAndNullInputStream_ThrowsIllegalArgumentException()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        InputStream in = null;

        // Act
        factory.createArchiveInputStream(archiverName, in);
    }

    // Similar tests for other archiver names and input stream combinations

    @Test
    public void testCreateArchiveOutputStream_ValidArArchiverNameAndOutputStream_ReturnsArArchiveOutputStream()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        OutputStream out = new ByteArrayOutputStream();

        // Act
        ArchiveOutputStream result = factory.createArchiveOutputStream(archiverName, out);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArArchiverNameAndOutputStream_ThrowsIllegalArgumentException()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();

        // Act
        factory.createArchiveOutputStream(archiverName, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_ArArchiverNameAndNullOutputStream_ThrowsIllegalArgumentException()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = "ar";
        OutputStream out = null;

        // Act
        factory.createArchiveOutputStream(archiverName, out);
    }

    // Similar tests for other archiver names and output stream combinations

    @Test
    public void testCreateArchiveInputStream_ValidInputStream_ReturnsArchiveInputStream()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        ArchiveInputStream result = factory.createArchiveInputStream(in);

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream_ThrowsIllegalArgumentException()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = null;

        // Act
        factory.createArchiveInputStream(in);
    }

    // Similar tests for other input stream combinations

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidInputStream_ThrowsArchiveException()
            throws ArchiveException {
        // Arrange
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);

        // Act
        factory.createArchiveInputStream(in);
    }

    // Similar tests for other invalid input stream combinations

}