import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, in);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        InputStream in = new ByteArrayInputStream("test".getBytes());
        factory.createArchiveInputStream(archiverName, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = null;
        factory.createArchiveInputStream(archiverName, in);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream result = factory.createArchiveOutputStream(archiverName, out);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = null;
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(archiverName, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = null;
        factory.createArchiveOutputStream(archiverName, out);
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = null;
        factory.createArchiveInputStream(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNonMarkableInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        factory.createArchiveInputStream(in);
    }
}