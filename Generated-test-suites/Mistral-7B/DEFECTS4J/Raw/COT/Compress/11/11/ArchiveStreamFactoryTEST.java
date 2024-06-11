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
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_validArchiverNameAndInputStream() {
        final String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(result);
    }

    @Test
    public void testCreateArchiveInputStream_invalidArchiverName() {
        final String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_nullInputStream() {
        final String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = null;
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_noArchiverDetected() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream));
    }

    @Test
    public void testCreateArchiveOutputStream_validArchiverNameAndOutputStream() {
        final String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream result = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(result);
    }

    @Test
    public void testCreateArchiveOutputStream_invalidArchiverName() {
        final String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream));
    }

    @Test
    public void testCreateArchiveOutputStream_nullOutputStream() {
        final String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = null;
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream_validArchiveType() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertNotNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream_invalidArchiveType() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream_noArchiveTypeDetected() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream));
    }
}