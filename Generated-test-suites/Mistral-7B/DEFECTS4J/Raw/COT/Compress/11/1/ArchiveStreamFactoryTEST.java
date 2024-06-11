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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiveNameAndInputStream() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "zip";

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiveName() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = null;

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws Exception {
        String archiveName = "zip";
        InputStream is = null;

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiveName() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "invalid";

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveNameAndOutputStream() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "zip";

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiveName() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = null;

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws Exception {
        String archiveName = "zip";
        OutputStream os = null;

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArchiveName() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "invalid";

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test
    public void testCreateArchiveInputStream_AutodetectValidFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = null;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(is);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutodetectInvalidFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = null;

        new ArchiveStreamFactory().createArchiveInputStream(is);
    }

    @Test
    public void testCreateArchiveInputStream_ArFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = ArchiveStreamFactory.AR;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
        assertNotNull(archiveInputStream);
        Assert.assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "ARInvalid";

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test
    public void testCreateArchiveOutputStream_ArFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = ArchiveStreamFactory.AR;

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
        assertNotNull(archiveOutputStream);
        Assert.assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidArFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "ARInvalid";

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test
    public void testCreateArchiveInputStream_CpioFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = ArchiveStreamFactory.CPIO;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
        assertNotNull(archiveInputStream);
        Assert.assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidCpioFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "CPIOInvalid";

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test
    public void testCreateArchiveOutputStream_CpioFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = ArchiveStreamFactory.CPIO;

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
        assertNotNull(archiveOutputStream);
        Assert.assertEquals(CpioArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidCpioFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "CPIOInvalid";

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test
    public void testCreateArchiveInputStream_DumpFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = ArchiveStreamFactory.DUMP;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
        assertNotNull(archiveInputStream);
        Assert.assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidDumpFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "DUMPInvalid";

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test
    public void testCreateArchiveOutputStream_DumpFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = ArchiveStreamFactory.DUMP;

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
        assertNotNull(archiveOutputStream);
        Assert.assertEquals(DumpArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidDumpFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "DUMPInvalid";

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test
    public void testCreateArchiveInputStream_JarFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = ArchiveStreamFactory.JAR;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
        assertNotNull(archiveInputStream);
        Assert.assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidJarFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "JARInvalid";

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test
    public void testCreateArchiveOutputStream_JarFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = ArchiveStreamFactory.JAR;

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
        assertNotNull(archiveOutputStream);
        Assert.assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidJarFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "JARInvalid";

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test
    public void testCreateArchiveInputStream_TarFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = ArchiveStreamFactory.TAR;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
        assertNotNull(archiveInputStream);
        Assert.assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidTarFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "TARInvalid";

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test
    public void testCreateArchiveOutputStream_TarFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = ArchiveStreamFactory.TAR;

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
        assertNotNull(archiveOutputStream);
        Assert.assertEquals(TarArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidTarFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "TARInvalid";

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }

    @Test
    public void testCreateArchiveInputStream_ZipFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = ArchiveStreamFactory.ZIP;

        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
        assertNotNull(archiveInputStream);
        Assert.assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidZipFormat() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1]);
        String archiveName = "ZIPInvalid";

        new ArchiveStreamFactory().createArchiveInputStream(archiveName, is);
    }

    @Test
    public void testCreateArchiveOutputStream_ZipFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = ArchiveStreamFactory.ZIP;

        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
        assertNotNull(archiveOutputStream);
        Assert.assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_InvalidZipFormat() throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        String archiveName = "ZIPInvalid";

        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, os);
    }
}