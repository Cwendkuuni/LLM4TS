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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws IOException, ArchiveException {
        String archiverName = "zip";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
        Assert.assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        String archiverName = null;
        InputStream input = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        String archiverName = "zip";
        InputStream input = null;
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws IOException, ArchiveException {
        String archiverName = "invalid";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithZipArchive() throws IOException, ArchiveException {
        String archiverName = "zip";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws IOException, ArchiveException {
        String archiverName = "zip";
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
        Assert.assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        String archiverName = null;
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        String archiverName = "zip";
        OutputStream output = null;
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws IOException, ArchiveException {
        String archiverName = "invalid";
        OutputStream output = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithZipArchive() throws IOException, ArchiveException {
        String archiverName = "zip";
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, output);
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testDetectArchiveTypeWithValidArchive() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x50, (byte) 0x4b, (byte) 0x03, (byte) 0x04});
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("", input);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testDetectArchiveTypeWithInvalidArchive() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x50, (byte) 0x4b, (byte) 0x03, (byte) 0x05});
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("", input);
        } catch (ArchiveException e) {
            // expected
        }
        Assert.assertNull(archiveInputStream);
    }
}