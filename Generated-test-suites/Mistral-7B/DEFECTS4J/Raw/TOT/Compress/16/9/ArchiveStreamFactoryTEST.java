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
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromName() throws Exception {
        final String archiverName = "zip";
        final InputStream inputStream = new ByteArrayInputStream(getTestFile("test.zip").array());
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        Assert.assertTrue(ArchiveInputStream.class.isInstance(archiveInputStream));
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertEquals(expectedArchiveInputStream.getFormat(), archiveInputStream.getFormat());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromName() throws Exception {
        final String archiverName = "jar";
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        Assert.assertTrue(ArchiveOutputStream.class.isInstance(archiveOutputStream));
        ArchiveOutputStream expectedArchiveOutputStream = new JarArchiveOutputStream(outputStream);
        Assert.assertEquals(expectedArchiveOutputStream.getFormat(), archiveOutputStream.getFormat());
        archiveOutputStream.finish();
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(getTestFile("test.zip").array());
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream("zip", inputStream);
        Assert.assertTrue(ArchiveInputStream.class.isInstance(archiveInputStream));
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);
        Assert.assertEquals(expectedArchiveInputStream.getFormat(), archiveInputStream.getFormat());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromStream() throws Exception {
        final OutputStream outputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("jar", outputStream);
        Assert.assertTrue(ArchiveOutputStream.class.isInstance(archiveOutputStream));
        ArchiveOutputStream expectedArchiveOutputStream = new JarArchiveOutputStream(outputStream);
        Assert.assertEquals(expectedArchiveOutputStream.getFormat(), archiveOutputStream.getFormat());
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write("Test content.".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();
        Assert.assertEquals("Test content.", new String(getTestFile("test-out.jar").array(), "UTF-8"));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownName() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(getTestFile("test.zip").array());
        final String unknownArchiverName = "unknown";
        new ArchiveStreamFactory().createArchiveInputStream(unknownArchiverName, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownName() throws Exception {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamFromNullInputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveInputStream("zip", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamFromNullOutputStream() throws Exception {
        new ArchiveStreamFactory().createArchiveOutputStream("jar", null);
    }

    private byte[] getTestFile(String fileName) throws IOException {
        final InputStream inputStream = getClass().getResourceAsStream("/test-resources/" + fileName);
        return IOUtils.toByteArray(inputStream);
    }

}