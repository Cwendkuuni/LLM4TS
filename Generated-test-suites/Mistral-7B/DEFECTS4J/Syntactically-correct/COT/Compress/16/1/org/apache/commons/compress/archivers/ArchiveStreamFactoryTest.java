import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());
        String archiveName = ArchiveStreamFactory.ZIP;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveName, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiveName() {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        String archiveName = ArchiveStreamFactory.ZIP;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveName, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiveFormat() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());
        String archiveName = "unknownFormat";
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveName, inputStream);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStreamWithInvalidStreamData() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("invalidArchiveData".getBytes());
        String archiveName = ArchiveStreamFactory.ZIP;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiveName, inputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        String archiveName = ArchiveStreamFactory.ZIP;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiveName, outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiveName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        String archiveName = ArchiveStreamFactory.ZIP;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiveName, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiveFormat() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        String archiveName = "unknownFormat";
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiveName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutomatically() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertEquals("zip", archiveInputStream.getFormat().getName());
        archiveInputStream.close();
    }
}