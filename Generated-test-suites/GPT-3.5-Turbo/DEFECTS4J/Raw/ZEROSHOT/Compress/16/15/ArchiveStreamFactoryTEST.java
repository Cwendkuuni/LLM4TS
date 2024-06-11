import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiverName() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);

        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        String archiverName = null;
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiverName() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);

        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetectSignature() throws ArchiveException, IOException {
        byte[] signature = new byte[4];
        for (int i = 0; i < signature.length; i++) {
            signature[i] = (byte) (i + 1);
        }
        ByteArrayInputStream in = new ByteArrayInputStream(signature);

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(in);

        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStreamForAutoDetectSignature() throws ArchiveException {
        InputStream in = null;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(in);
    }
}