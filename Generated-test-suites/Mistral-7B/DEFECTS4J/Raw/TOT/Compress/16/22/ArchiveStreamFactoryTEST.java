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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        final String archiverName = "zip";
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream expected = new ZipArchiveInputStream(inputStream);
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        assertEquals(expected.getClass(), actual.getClass());
        IOUtils.closeQuietly(expected);
        IOUtils.closeQuietly(actual);
    }

    @Test(expected = ArchiveException.class)
    public void testUnknownArchiverName() throws Exception {
        final String archiverName = "unknown";
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ArchiveInputStream actual = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertEquals(ZipArchiveInputStream.class, actual.getClass());
        IOUtils.closeQuietly(actual);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        final String archiverName = "tar";
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expected = new TarArchiveOutputStream(outputStream);
        ArchiveOutputStream actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        assertEquals(expected.getClass(), actual.getClass());
        IOUtils.closeQuietly(expected);
        IOUtils.closeQuietly(actual);
    }

    @Test
    public void testCreateArchiveOutputStreamToNull() throws Exception {
        final String archiverName = "ar";
        final OutputStream outputStream = null;
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithEntry() throws Exception {
        final String archiverName = "zip";
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putArchiveEntry(new JarArchiveEntry("test.txt"));
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testUnknownArchiverNameForOutputStream() throws Exception {
        final String archiverName = "unknown";
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }
}