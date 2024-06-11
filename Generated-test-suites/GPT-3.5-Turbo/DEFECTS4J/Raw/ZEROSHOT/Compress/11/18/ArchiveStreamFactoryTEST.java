import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
        assertNotNull(archiveInputStream);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected=ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws Exception {
        String archiverName = "unknown";
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    }

    @Test(expected=ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws Exception {
        String archiverName = "unknown";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    }

    @Test
    public void testCreateArchiveInputStreamWithStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullStream() throws Exception {
        InputStream in = null;
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithUnsupportedStream() throws Exception {
        InputStream in = new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        };
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
    }

    @Test
    public void testCreateArchiveInputStreamWithZipSignature() throws Exception {
        byte[] signature = {'P', 'K', 0x03, 0x04};
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithJarSignature() throws Exception {
        byte[] signature = {'P', 'K', 0x03, 0x04, 0x0A};
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithArSignature() throws Exception {
        byte[] signature = {'#', '!', 0x60};
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithCpioSignature() throws Exception {
        byte[] signature = {0xC7, '7', 0x71, 0xC7, 0x72};
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
    }

    @Test(expected=ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature() throws Exception {
        byte[] signature = {0x00, 0x01, 0x02, 0x03};
        InputStream in = new ByteArrayInputStream(signature);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(in);
    }
}