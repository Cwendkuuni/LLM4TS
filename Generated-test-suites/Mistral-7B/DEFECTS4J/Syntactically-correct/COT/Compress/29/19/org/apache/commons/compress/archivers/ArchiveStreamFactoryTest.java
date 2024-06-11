import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_InvalidSignature() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x1, 0x2, 0x3});
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ARJ() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_ARJ_InvalidSignature() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_CPIO_InvalidSignature() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStream_SEVEN_Z() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_InvalidSignature() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Tar() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Tar_InvalidSignature() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Zip_InvalidSignature() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        archiveOutputStream.finish();
        Assert.assertNull(archiveOutputStream.getNextEntry());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, outputStream);
        archiveOutputStream.finish();
        Assert.assertNull(archiveOutputStream.getNextEntry());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        archiveOutputStream.finish();
        Assert.assertNull(archiveOutputStream.getNextEntry());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        archiveOutputStream.finish();
        Assert.assertNull(archiveOutputStream.getNextEntry());
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        jarOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        jarOutputStream.finish();
        Assert.assertNull(jarOutputStream.getNextEntry());
        jarOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.finish();
        Assert.assertNull(tarOutputStream.getNextEntry());
        tarOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws IOException {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        zipOutputStream.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipOutputStream.finish();
        Assert.assertNull(zipOutputStream.getNextEntry());
        zipOutputStream.close();
    }

}