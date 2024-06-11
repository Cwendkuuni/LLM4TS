import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_Ar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Cpio() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Jar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Ar() throws ArchiveException {
        OutputStream outputStream = new FileOutputStream("test");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() throws ArchiveException {
        OutputStream outputStream = new FileOutputStream("test");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws ArchiveException {
        OutputStream outputStream = new FileOutputStream("test");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws ArchiveException {
        OutputStream outputStream = new FileOutputStream("test");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws ArchiveException {
        OutputStream outputStream = new FileOutputStream("test");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Zip() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Jar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Ar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Cpio() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Dump() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Tar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }
}