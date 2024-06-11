
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream("zip", in);
        assertNotNull(result);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream result = factory.createArchiveOutputStream("zip", out);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream result = factory.createArchiveInputStream("zip", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream result = factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream result = factory.createArchiveOutputStream("zip", null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NullStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream result = factory.createArchiveInputStream(null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream result = factory.createArchiveInputStream(in);
    }
}
