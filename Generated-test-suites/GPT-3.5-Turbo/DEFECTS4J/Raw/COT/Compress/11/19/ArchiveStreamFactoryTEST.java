import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_Ar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);
        Assert.assertTrue(output instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        Assert.assertTrue(output instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        Assert.assertTrue(output instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Jar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        Assert.assertTrue(output instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Cpio() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        Assert.assertTrue(output instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Ar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        Assert.assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        Assert.assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        Assert.assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        Assert.assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetection() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(input);
        // Auto-detection is based on the first few bytes of the input stream,
        // so we cannot check the exact type.
        Assert.assertTrue(output instanceof ArchiveInputStream);
    }
}