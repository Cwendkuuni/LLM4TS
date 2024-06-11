import org.junit.Test;
import org.junit.Assert;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        Assert.assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream("invalid", in);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        Assert.assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("invalid", out);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        Assert.assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamFromNullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInvalidInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream(in);
    }

}