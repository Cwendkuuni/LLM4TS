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
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws ArchiveException, IOException {
        String archiverName = "ar";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream actualArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        ArchiveInputStream expectedArchiveInputStream = new ArArchiveInputStream(inputStream);
        assertEquals(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiverName() throws ArchiveException, IOException {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() {
        String archiverName = "ar";
        InputStream inputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws ArchiveException, IOException {
        String archiverName = "ar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream actualArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        ArchiveOutputStream expectedArchiveOutputStream = new ArArchiveOutputStream(outputStream);
        assertEquals(expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArchiverName() throws ArchiveException, IOException {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() {
        String archiverName = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() {
        String archiverName = "ar";
        OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetection_Zip() throws ArchiveException, IOException {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream actualArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);
        assertEquals(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetection_Tar() throws ArchiveException, IOException {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream actualArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        ArchiveInputStream expectedArchiveInputStream = new TarArchiveInputStream(inputStream);
        assertEquals(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetection_Unknown() throws ArchiveException, IOException {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws ArchiveException, IOException {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream actualArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);
        assertEquals(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws ArchiveException, IOException {
        String archiverName = "jar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream actualArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        ArchiveOutputStream expectedArchiveOutputStream = new JarArchiveOutputStream(outputStream);
        assertEquals(expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws ArchiveException, IOException {
        String archiverName = "tar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream actualArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        ArchiveOutputStream expectedArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        assertEquals(expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }
}