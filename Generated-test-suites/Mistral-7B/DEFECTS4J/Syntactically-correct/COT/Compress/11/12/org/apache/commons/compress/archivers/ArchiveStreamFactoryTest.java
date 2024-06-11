import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiveFormat() throws Exception {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[1]); // Any InputStream
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiveFormat() throws Exception {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[1]); // Any InputStream
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1]); // Any InputStream
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveFormat() throws Exception {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream(); // Any OutputStream
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArchiveFormat() throws Exception {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream(); // Any OutputStream
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }
}