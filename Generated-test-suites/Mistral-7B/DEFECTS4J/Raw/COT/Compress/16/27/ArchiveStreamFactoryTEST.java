import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream expectedArchiveInputStream = new ArArchiveInputStream(in);
        ArchiveInputStream actualArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertEquals(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_UnknownName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInput() throws ArchiveException {
        assertNull(new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveInputStream_AR_EmptyInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actualArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(actualArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(in);
        ArchiveInputStream actualArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertEquals(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    // Add test cases for other methods similarly

    @Test
    public void testCreateArchiveOutputStream_AR() throws ArchiveException, IOException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream expectedArchiveOutputStream = new ArArchiveOutputStream(out);
        ArchiveOutputStream actualArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertEquals(expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AR_UnknownName() throws ArchiveException, IOException {
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullOutput() throws ArchiveException, IOException {
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveOutputStream_AR_EmptyOutputStream() throws ArchiveException, IOException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actualArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(actualArchiveOutputStream);
    }

    // Add test cases for other methods similarly

    // Add edge case and exception handling tests here

}