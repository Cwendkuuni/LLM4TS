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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private InputStream testInputStream;
    private OutputStream testOutputStream;

    @Before
    public void setup() {
        testInputStream = new ByteArrayInputStream(new byte[1024]);
        testOutputStream = new ByteArrayOutputStream(1024);
    }

    @After
    public void teardown() throws IOException {
        IOUtils.closeQuietly(testInputStream);
        IOUtils.closeQuietly(testOutputStream);
    }

    // Test for createArchiveInputStream

    @Test
    public void testCreateArchiveInputStreamFromAr() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream in = testInputStream;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
        Assert.assertEquals("Unexpected archive type", ArArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownFormat() throws IOException {
        String archiverName = "unknownFormat";
        InputStream in = testInputStream;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromZip() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream in = new ByteArrayInputStream(ZipArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
        Assert.assertEquals("Unexpected archive type", ZipArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromTar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        InputStream in = new ByteArrayInputStream(TarArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
        Assert.assertEquals("Unexpected archive type", TarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromJar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream in = new ByteArrayInputStream(JarArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
        Assert.assertEquals("Unexpected archive type", JarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromCpio() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.CPIO;
        InputStream in = new ByteArrayInputStream(CpioArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
        Assert.assertEquals("Unexpected archive type", CpioArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromDump() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.DUMP;
        InputStream in = new ByteArrayInputStream(DumpArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
        Assert.assertEquals("Unexpected archive type", DumpArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    // Test for createArchiveOutputStream

    @Test
    public void testCreateArchiveOutputStreamFromAr() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream out = testOutputStream;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
        Assert.assertEquals("Unexpected archive type", ArArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromZip() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream out = testOutputStream;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
        Assert.assertEquals("Unexpected archive type", ZipArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromTar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        OutputStream out = testOutputStream;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
        Assert.assertEquals("Unexpected archive type", TarArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromJar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream out = testOutputStream;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
        Assert.assertEquals("Unexpected archive type", JarArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromCpio() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.CPIO;
        OutputStream out = testOutputStream;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
        Assert.assertEquals("Unexpected archive type", CpioArchiveOutputStream.class, archiveOutputStream.getClass());
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownFormat() throws IOException {
        String archiverName = "unknownFormat";
        OutputStream out = testOutputStream;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
        archiveOutputStream.close();
    }

    // Test for createArchiveInputStream with auto-detection

    @Test
    public void testCreateArchiveInputStreamWithAutoDetectionFromZip() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(ZipArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(in);
        Assert.assertEquals("Unexpected archive type", ZipArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetectionFromTar() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(TarArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(in);
        Assert.assertEquals("Unexpected archive type", TarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetectionFromJar() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(JarArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(in);
        Assert.assertEquals("Unexpected archive type", JarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetectionFromAr() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(ArArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(in);
        Assert.assertEquals("Unexpected archive type", ArArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetectionFromCpio() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(CpioArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(in);
        Assert.assertEquals("Unexpected archive type", CpioArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetectionFromDump() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(DumpArchiveInputStream.TEST_DATA.getBytes());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(in);
        Assert.assertEquals("Unexpected archive type", DumpArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithAutoDetectionFromUnknownFormat() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(in);
        Assert.assertNull("Unexpected archive type", archiveInputStream.getClass());
        archiveInputStream.close();
    }
}