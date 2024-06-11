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
    public void testCreateArchiveInputStream_AR() {
        InputStream input = new ByteArrayInputStream("AR archive signature".getBytes());
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, input);
        ArArchiveInputStream expected = new ArArchiveInputStream(input);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_InvalidSignature() {
        InputStream input = new ByteArrayInputStream("Invalid AR signature".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, input);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() {
        InputStream input = new ByteArrayInputStream("CPIO archive signature".getBytes());
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        CpioArchiveInputStream expected = new CpioArchiveInputStream(input);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_CPIO_InvalidSignature() {
        InputStream input = new ByteArrayInputStream("Invalid CPIO signature".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
    }

    @Test
    public void testCreateArchiveInputStream_Dump() {
        InputStream input = new ByteArrayInputStream("Dump archive signature".getBytes());
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
        DumpArchiveInputStream expected = new DumpArchiveInputStream(input);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Dump_InvalidSignature() {
        InputStream input = new ByteArrayInputStream("Invalid Dump signature".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() {
        InputStream input = new ByteArrayInputStream("JAR archive signature".getBytes());
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        JarArchiveInputStream expected = new JarArchiveInputStream(input);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_InvalidSignature() {
        InputStream input = new ByteArrayInputStream("Invalid JAR signature".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
    }

    @Test
    public void testCreateArchiveInputStream_TAR() {
        InputStream input = new ByteArrayInputStream("TAR archive signature".getBytes());
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        TarArchiveInputStream expected = new TarArchiveInputStream(input);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_TAR_InvalidSignature() {
        InputStream input = new ByteArrayInputStream("Invalid TAR signature".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() {
        InputStream input = new ByteArrayInputStream("ZIP archive signature".getBytes());
        ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        ZipArchiveInputStream expected = new ZipArchiveInputStream(input);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_ZIP_InvalidSignature() {
        InputStream input = new ByteArrayInputStream("Invalid ZIP signature".getBytes());
        ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
    }

    @Test
    public void testCreateArchiveInputStream_Unknown() {
        InputStream input = new ByteArrayInputStream("Unknown signature".getBytes());
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", input);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_AR() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        ArArchiveOutputStream expected = new ArArchiveOutputStream(output);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AR_NullOutput() {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        CpioArchiveOutputStream expected = new CpioArchiveOutputStream(output);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_CPIO_NullOutput() {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
        DumpArchiveOutputStream expected = new DumpArchiveOutputStream(output);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_Dump_NullOutput() {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, null);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        JarArchiveOutputStream expected = new JarArchiveOutputStream(output);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_JAR_NullOutput() {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        TarArchiveOutputStream expected = new TarArchiveOutputStream(output);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_TAR_NullOutput() {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        ZipArchiveOutputStream expected = new ZipArchiveOutputStream(output);
        assertSame(expected.getClass(), actual.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_ZIP_NullOutput() {
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveOutputStream_Unknown() {
        OutputStream output = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", output);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }
}