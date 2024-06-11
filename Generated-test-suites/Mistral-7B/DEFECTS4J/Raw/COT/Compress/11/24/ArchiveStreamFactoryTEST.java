import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws ArchiveException {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            assertFalse(e.getMessage().isEmpty());
        } finally {
            assertNull(archiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = null;
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            assertFalse(e.getMessage().isEmpty());
        } finally {
            assertNull(archiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithEmptyInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            assertFalse(e.getMessage().isEmpty());
        } finally {
            assertNull(archiveInputStream);
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws ArchiveException {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = null;
        try {
            archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            assertFalse(e.getMessage().isEmpty());
        } finally {
            assertNull(archiveOutputStream);
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = null;
        ArchiveOutputStream archiveOutputStream = null;
        try {
            archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            assertFalse(e.getMessage().isEmpty());
        } finally {
            assertNull(archiveOutputStream);
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithValidArguments() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithNullInputStream() {
        InputStream inputStream = null;
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // Expected
        } finally {
            assertNull(archiveInputStream);
        }
    }

    @Test
    public void testCreateArArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("header\0".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArArchiveInputStreamWithInvalidHeader() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("invalid-header\0".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
    }

    @Test
    public void testCreateArArchiveOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
    }

    @Test
    public void testCreateCpioArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("magic\0".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateCpioArchiveInputStreamWithInvalidMagic() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("invalid-magic\0".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
    }

    @Test
    public void testCreateCpioArchiveOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateCpioArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
    }

    @Test
    public void testCreateDumpArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("magic\0".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateDumpArchiveInputStreamWithInvalidMagic() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("invalid-magic\0".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
    }

    @Test
    public void testCreateDumpArchiveOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateDumpArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
    }

    @Test
    public void testCreateJarArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("magic\0".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateJarArchiveInputStreamWithInvalidMagic() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("invalid-magic\0".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
    }

    @Test
    public void testCreateJarArchiveOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateJarArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
    }

    @Test
    public void testCreateTarArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("magic\0".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateTarArchiveInputStreamWithInvalidMagic() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("invalid-magic\0".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    }

    @Test
    public void testCreateTarArchiveOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateTarArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    }

    @Test
    public void testCreateZipArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("local-file-header\0".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateZipArchiveInputStreamWithInvalidLocalFileHeader() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("invalid-local-file-header\0".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    }

    @Test
    public void testCreateZipArchiveOutputStream() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateZipArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
    }
}