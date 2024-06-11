package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.compress.archivers.ArchiveStreamFactory.AR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ARJ;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.CPIO;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.DUMP;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.JAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.SEVEN_Z;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.TAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ZIP;

public class ArchiveStreamFactoryTest {

    

    @Test
    public void testCreateArchiveInputStream_AR() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("AR archive test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(AR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AR_InvalidSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Invalid AR archive signature".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(AR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ARJ() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("ARJ archive test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ARJ, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStream_ARJ_OutputStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(ARJ, outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("CPIO archive test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(CPIO, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_CPIO_InvalidSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Invalid CPIO archive signature".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(CPIO, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("Dump archive test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(DUMP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStream_Dump_InputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Dump archive test data".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(DUMP, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("JAR archive test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(JAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_JAR_InvalidSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Invalid JAR archive signature".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(JAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("TAR archive test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(TAR, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_TAR_InvalidSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Invalid TAR archive signature".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(TAR, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("ZIP archive test data".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ZIP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_ZIP_InvalidSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Invalid ZIP archive signature".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream(ZIP, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws ArchiveException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(AR, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.ar.ARFileEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.write(51);
        archiveOutputStream.finish();
        Assert.assertEquals("test data", IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_AR_NullOutputStream() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ() throws ArchiveException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ARJ, outputStream);
        archiveOutputStream.finish();
        Assert.assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ARJ, new ByteArrayOutputStream()));
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws ArchiveException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(CPIO, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.cpio.CpioFileEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.write(51);
        archiveOutputStream.finish();
        Assert.assertEquals("test data", IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_CPIO_NullOutputStream() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(CPIO, null);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws ArchiveException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(JAR, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.jar.JarEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.write(51);
        archiveOutputStream.finish();
        Assert.assertEquals("test data", IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_JAR_NullOutputStream() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(JAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws ArchiveException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(TAR, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.tar.TarEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.write(51);
        archiveOutputStream.finish();
        Assert.assertEquals("test data", IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_TAR_NullOutputStream() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(TAR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws ArchiveException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ZIP, outputStream);
        archiveOutputStream.putNextEntry(new org.apache.commons.compress.archivers.zip.ZipEntry("test.txt", "test data".getBytes()));
        archiveOutputStream.write(51);
        archiveOutputStream.finish();
        Assert.assertEquals("test data", IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_ZIP_NullOutputStream() throws IOException {
        new ArchiveStreamFactory().createArchiveOutputStream(ZIP, null);
    }

    @Test
    public void testCreateArchiveInputStream_ValidArchiveTypes() throws ArchiveException, IOException {
        String[] archiveTypes = {AR, ARJ, CPIO, DUMP, JAR, SEVEN_Z, TAR, ZIP};
        InputStream inputStream = new ByteArrayInputStream("Test data for valid archives".getBytes());
        for (String archiver : archiveTypes) {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiver, inputStream);
            archiveInputStream.close();
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidArchiveType() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data for valid archives".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("INVALID", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveTypes() throws ArchiveException, IOException {
        String[] archiveTypes = {AR, ARJ, CPIO, DUMP, JAR, SEVEN_Z, TAR, ZIP};
        OutputStream outputStream = new ByteArrayOutputStream();
        for (String archiver : archiveTypes) {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiver, outputStream);
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStream_SevenZ() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(SEVEN_Z, outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_NewInstanceWithEncoding() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data for archives with encoding".getBytes());
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory("UTF-8");
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(JAR, inputStream);
        archiveInputStream.close();
    }

    
}