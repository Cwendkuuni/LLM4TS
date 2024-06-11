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
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, input);
        assertNotNull(ais);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_UnknownFormat() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream("UnknownFormat", input);
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidStream() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        input.close();
        ArchiveInputStream ais = null;
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, input);
        } catch (Exception e) {
            ais = e.getCause();
        }
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_AR() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        ArArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertNotNull(ais);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownFormat() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        assertNotNull(ais);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_UnknownFormat() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream("UnknownFormat", input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_InvalidStream() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        input.close();
        ArchiveInputStream ais = null;
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        } catch (Exception e) {
            ais = e.getCause();
        }
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_JAR() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        JarArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertNotNull(ais);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_InvalidFormat() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertNotNull(aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AR_UnknownFormat() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", output);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_InvalidOutput() throws Exception {
        OutputStream output = null;
        ArchiveOutputStream aos = null;
        try {
            aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        } catch (Exception e) {
            aos = e.getCause();
        }
        assertNotNull(aos);
    }

    @Test
    public void testCreateArchiveOutputStream_AutoDetect_AR() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(output);
        assertNotNull(aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AutoDetect_UnknownFormat() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(output);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertNotNull(aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_JAR_UnknownFormat() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", output);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR_InvalidOutput() throws Exception {
        OutputStream output = null;
        ArchiveOutputStream aos = null;
        try {
            aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        } catch (Exception e) {
            aos = e.getCause();
        }
        assertNotNull(aos);
    }

    @Test
    public void testCreateArchiveOutputStream_AutoDetect_JAR() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(output);
        assertNotNull(aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AutoDetect_InvalidFormat() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(output);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertNotNull(aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_CPIO_UnknownFormat() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("UnknownFormat", output);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO_InvalidOutput() throws Exception {
        OutputStream output = null;
        ArchiveOutputStream aos = null;
        try {
            aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        } catch (Exception e) {
            aos = e.getCause();
        }
        assertNotNull(aos);
    }

    @Test
    public void testCreateArchiveOutputStream_AutoDetect_CPIO() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(output);
        assertNotNull(aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AutoDetect_UnknownFormat_CPIO() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(output);
    }
}