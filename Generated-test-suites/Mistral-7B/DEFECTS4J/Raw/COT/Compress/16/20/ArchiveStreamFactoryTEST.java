import org.apache.commons.compress.ArchiveException;
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
    public void testCreateArchiveInputStream_AR() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(actual);
        assertNotNull(actual.getNextEntry());
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidSignature() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("InvalidArchiver", in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(actual);
        assertNotNull(actual.getNextEntry());
    }

    @Test
    public void testCreateArchiveInputStream_CPIO_InvalidSignature() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_CPIO_NullInputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_CPIO_InvalidArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("InvalidArchiver", in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(actual);
        assertNotNull(actual.getNextEntry());
    }

    @Test
    public void testCreateArchiveInputStream_DUMP_InvalidSignature() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_DUMP_NullInputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_DUMP_InvalidArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("InvalidArchiver", in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(actual);
        assertNotNull(actual.getNextEntry());
    }

    @Test
    public void testCreateArchiveInputStream_JAR_InvalidSignature() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_JAR_NullInputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_JAR_InvalidArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("InvalidArchiver", in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(actual);
        assertNotNull(actual.getNextEntry());
    }

    @Test
    public void testCreateArchiveInputStream_TAR_InvalidSignature() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_TAR_NullInputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_TAR_InvalidArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("InvalidArchiver", in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(actual);
        assertNotNull(actual.getNextEntry());
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_InvalidSignature() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_NullInputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_InvalidArchiverName() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("InvalidArchiver", in);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(actual);
        actual.putArchiveEntry(new ArArchiveEntry("test.txt"));
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullOutputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_AR_InvalidArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("InvalidArchiver", out);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(actual);
        actual.putNextEntry(new CpioArchiveEntry("test.txt"));
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO_NullOutputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO_InvalidArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("InvalidArchiver", out);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        assertNotNull(actual);
        actual.putNextEntry(new DumpArchiveEntry("test.txt"));
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP_NullOutputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP_InvalidArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("InvalidArchiver", out);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(actual);
        actual.putNextEntry(new JarArchiveEntry("test.txt"));
    }

    @Test
    public void testCreateArchiveOutputStream_JAR_NullOutputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_JAR_InvalidArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("InvalidArchiver", out);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(actual);
        actual.putNextEntry(new TarArchiveEntry("test.txt"));
    }

    @Test
    public void testCreateArchiveOutputStream_TAR_NullOutputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_TAR_InvalidArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("InvalidArchiver", out);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(actual);
        actual.putNextEntry(new ZipArchiveEntry("test.txt"));
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_NullOutputStream() throws ArchiveException {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_InvalidArchiverName() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("InvalidArchiver", out);
            assert false;
        } catch (ArchiveException e) {
            //expected
        }
    }
}