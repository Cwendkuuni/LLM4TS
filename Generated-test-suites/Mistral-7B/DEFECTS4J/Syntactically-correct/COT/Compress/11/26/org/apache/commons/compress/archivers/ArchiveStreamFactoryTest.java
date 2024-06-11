import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream(new byte[1024]);
        outputStream = new ByteArrayOutputStream();
    }

    // Test createArchiveInputStream(String, InputStream)

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
    }

    // Test createArchiveOutputStream(String, OutputStream)

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownFormat() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
    }

    // Test createArchiveInputStream(InputStream)

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat() throws Exception {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[1024]));
        assertFalse(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws Exception {
        InputStream zipInputStream = new ByteArrayInputStream(ZipArchiveInputStream.createTestData());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(zipInputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws Exception {
        InputStream tarInputStream = new ByteArrayInputStream(TarArchiveInputStream.createTestData());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(tarInputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat_Fail() throws Exception {
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(unknownInputStream);
        assertFalse(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_NoSupportedFormat() throws Exception {
        InputStream noSupportedInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(noSupportedInputStream);
        assertFalse(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = IOException.class) // IOException is thrown by createArchiveInputStream(InputStream)
    public void testCreateArchiveInputStream_IOException() throws Exception {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
        archiveInputStream.close();
    }
}