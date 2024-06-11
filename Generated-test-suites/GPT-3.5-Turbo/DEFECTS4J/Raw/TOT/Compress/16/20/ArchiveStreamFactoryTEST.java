import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
        inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateArchiveInputStream_withArFormat_shouldReturnArArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withZipFormat_shouldReturnZipArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withTarFormat_shouldReturnTarArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withJarFormat_shouldReturnJarArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withCpioFormat_shouldReturnCpioArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withDumpFormat_shouldReturnDumpArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws ArchiveException {
        factory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withUnknownArchiverName_shouldThrowArchiveException() throws ArchiveException {
        factory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_withArFormat_shouldReturnArArchiveOutputStream() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_withZipFormat_shouldReturnZipArchiveOutputStream() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_withTarFormat_shouldReturnTarArchiveOutputStream() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_withJarFormat_shouldReturnJarArchiveOutputStream() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_withCpioFormat_shouldReturnCpioArchiveOutputStream() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws ArchiveException {
        factory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_withUnknownArchiverName_shouldThrowArchiveException() throws ArchiveException {
        factory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withInputStream_shouldReturnCorrectArchiveInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        assertEquals(archiveInputStream.getClass(), factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream).getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        factory.createArchiveInputStream(null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidInputStream_shouldThrowArchiveException() throws ArchiveException {
        factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{}));
    }

}