import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        JarArchiveInputStream jarArchiveInputStream = (JarArchiveInputStream) archiveInputStream;
        assertEquals("testdata", jarArchiveInputStream.getNextEntry().getName());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiver() throws Exception {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiverName = "tar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        TarArchiveOutputStream tarArchiveOutputStream = (TarArchiveOutputStream) archiveOutputStream;
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("testdata.txt"));
        tarArchiveOutputStream.write("Test data".getBytes(), 0, "Test data".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        assertEquals("testdata.txt\0", tarArchiveOutputStream.getNextEntry().getName());
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, new ByteArrayInputStream(bytes));
        TarArchiveInputStream tarArchiveInputStream = (TarArchiveInputStream) archiveInputStream;
        assertEquals("testdata.txt\0", tarArchiveInputStream.getNextEntry().getName());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiver() throws Exception {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        JarArchiveInputStream jarArchiveInputStream = (JarArchiveInputStream) archiveInputStream;
        assertEquals("testdata", jarArchiveInputStream.getNextEntry().getName());
        archiveInputStream.close();
    }
}