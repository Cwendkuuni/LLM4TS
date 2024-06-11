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
    public void testCreateArchiveInputStreamStringInputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        archiveStreamFactory.createArchiveInputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromBytesZip() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        byte[] zipData = new byte[1024]; // 1 KB is a reasonable size
        InputStream inputStream = new ByteArrayInputStream(zipData);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromBytesUnknownFormat() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        byte[] unknownData = new byte[1024]; // 1 KB is a reasonable size
        InputStream inputStream = new ByteArrayInputStream(unknownData);
        archiveStreamFactory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamStringOutputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        archiveStreamFactory.createArchiveOutputStream("zip", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamOutputStreamWritesData() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", outputStream);
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write("Hello, world!".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        // The actual data written doesn't matter here, but the archive format must be correct
        assertFalse(Arrays.equals(result, new byte[0]));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamAutodetectsZip() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]); // 1 KB is a reasonable size
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream.getFormat().equals(ArchiveFormat.ZIP));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamAutodetectsTar() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[512]); // A tar header is about 512 bytes
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertFalse(archiveInputStream.getFormat().equals(ArchiveFormat.ZIP));
        assertTrue(archiveInputStream.getFormat().equals(ArchiveFormat.TAR) || archiveInputStream.getFormat().equals(ArchiveFormat.GZIP_TAR));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInputStreamAutodetectsUnknownFormat() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]); // 1 KB is a reasonable size
        archiveStreamFactory.createArchiveInputStream(inputStream);
    }
}