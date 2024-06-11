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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws ArchiveException {
        // Test creating a ZIP archive output stream with a specified name and output stream
        final String zipArchiveName = "test.zip";
        final byte[] zipBytes = new byte[1024];
        final InputStream inputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws ArchiveException {
        // Test creating a ZIP archive input stream with a specified name and input stream
        final String zipArchiveName = "test.zip";
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws ArchiveException {
        // Test creating an archive input stream from an input stream, autodetecting the archive type
        final byte[] zipBytes = new byte[1024];
        final InputStream inputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);

        // Test creating an AR archive input stream from an input stream
        final byte[] arBytes = new byte[32];
        final InputStream arInputStream = new ByteArrayInputStream(arBytes);
        arInputStream.mark(arBytes.length);
        inputStream.close();
        arInputStream.reset();
        ArchiveInputStream arArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(arInputStream);
        assertThrows(ArchiveException.class, arArchiveInputStream::getNextEntry);

        // Test creating an unknown archive input stream from an input stream
        final byte[] unknownBytes = new byte[1024];
        final InputStream unknownInputStream = new ByteArrayInputStream(unknownBytes);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(unknownInputStream));

        // Test creating an archive input stream with an invalid mark supported input stream
        final InputStream invalidMarkSupportedInputStream = new ByteArrayInputStream(zipBytes) {
            @Override
            public synchronized void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }
        };
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(invalidMarkSupportedInputStream));
    }
}