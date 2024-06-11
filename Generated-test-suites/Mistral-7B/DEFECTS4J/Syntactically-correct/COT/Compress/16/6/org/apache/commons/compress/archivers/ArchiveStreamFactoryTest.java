import org.apache.commons.compress.archivers.*;
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
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        String archiverName = ArchiveStreamFactory.ZIP;
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("invalidArchiver", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws ArchiveException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        String archiverName = ArchiveStreamFactory.ZIP;
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("invalidArchiver", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithValidSignature() throws ArchiveException, IOException {
        byte[] zipSignature = new byte[4];
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"PK\u0003\u0004"}.concat(zipSignature).toByteArray());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInputStreamWithInvalidSignature() throws ArchiveException, IOException {
        byte[] invalidSignature = new byte[4];
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"PK\u0003\u0005"}.concat(invalidSignature).toByteArray());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            fail();
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithoutSignature() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream); // or any other supported format
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveInputStreamFromInputStreamWithoutMarkSupport() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes()) {
            @Override
            public synchronized void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            fail();
        } catch (IOException e) {
            // expected
        }
    }
}