import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithAR() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(archiveInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithJAR() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(archiveInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithCPIO() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertNotNull(archiveInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithDUMP() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertNotNull(archiveInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithTAR() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertNotNull(archiveInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithZIP() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownFormat() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream("unknownFormat", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithAR() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithJAR() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithCPIO() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithDUMP() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithTAR() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithZIP() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownFormat() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknownFormat", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetection() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        // One of the following assertions will pass, depending on the input data
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertThrows(IllegalArgumentException.class, () -> archiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        assertThrows(IllegalArgumentException.class, () -> archiveOutputStream);
    }
}