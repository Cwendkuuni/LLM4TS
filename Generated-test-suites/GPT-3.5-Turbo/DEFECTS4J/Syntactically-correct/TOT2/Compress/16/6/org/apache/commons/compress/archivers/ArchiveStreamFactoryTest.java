import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void createArchiveInputStream_ValidArchiverNameAndInputStream_Success() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        } catch (ArchiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void createArchiveOutputStream_ValidArchiverNameAndOutputStream_Success() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = System.out;
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        } catch (ArchiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void createArchiveInputStream_ValidInputStream_Success() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        try {
            factory.createArchiveInputStream(in);
        } catch (ArchiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void createArchiveInputStream_InvalidArchiverName_ExceptionThrown() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveInputStream("invalid", in);
        });
    }

    @Test
    public void createArchiveInputStream_NullArchiverName_ExceptionThrown() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null, in);
        });
    }

    @Test
    public void createArchiveInputStream_NullInputStream_ExceptionThrown() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        });
    }

    @Test
    public void createArchiveInputStream_NullInputStreamInAutoDetect_ExceptionThrown() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null);
        });
    }

    @Test
    public void createArchiveInputStream_NonMarkSupportedInputStream_ExceptionThrown() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = mock(InputStream.class);
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(in);
        });
    }
}