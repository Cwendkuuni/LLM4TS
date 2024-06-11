import org.apache.commons.compress.archivers.*;
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
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_InvalidFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("invalidFormat", inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_CPIO_InvalidFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("invalidFormat", inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_DUMP_InvalidFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("invalidFormat", inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_InvalidFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("invalidFormat", inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_TAR_InvalidFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("invalidFormat", inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_ZIP_InvalidFormat() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[100]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream("invalidFormat", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AR_NullOutput() throws Exception {
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_CPIO_NullOutput() throws Exception {
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_DUMP_NullOutput() throws Exception {
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, null);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_JAR_NullOutput() throws Exception {
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_TAR_NullOutput() throws Exception {
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_ZIP_NullOutput() throws Exception {
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }
}