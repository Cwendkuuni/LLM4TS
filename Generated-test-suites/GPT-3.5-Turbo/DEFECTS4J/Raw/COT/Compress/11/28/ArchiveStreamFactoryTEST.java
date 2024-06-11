

import org.junit.Before;
import org.junit.Test;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
        inputStream = new ByteArrayInputStream(new byte[0]);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateArchiveInputStreamAr() throws ArchiveException {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, this.inputStream);
        assertTrue(inputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws ArchiveException {
        ArchiveInputStream inputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, this.inputStream);
        assertTrue(inputStream instanceof ZipArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidArchiverName() throws ArchiveException {
        factory.createArchiveInputStream("invalid", this.inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws ArchiveException {
        factory.createArchiveInputStream(null, this.inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws ArchiveException {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStreamAr() throws ArchiveException {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, this.outputStream);
        assertTrue(outputStream instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws ArchiveException {
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, this.outputStream);
        assertTrue(outputStream instanceof ZipArchiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalidArchiverName() throws ArchiveException {
        factory.createArchiveOutputStream("invalid", this.outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws ArchiveException {
        factory.createArchiveOutputStream(null, this.outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException {
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectZip() throws ArchiveException, IOException {
        byte[] zipSignature = {0x50, 0x4B, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(zipSignature);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    // Add more tests for other archive format autodetection and exceptions

}
