import org.apache.commons.compress.archivers.ArchiveException;
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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new FileInputStream("test_files/test_archive.ar");
        ArchiveInputStream expectedResult = new ArArchiveInputStream(inputStream);

        ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        Assert.assertSame(expectedResult.getClass(), result.getClass());
        result.close();
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidArchiveName() throws Exception {
        String archiverName = "invalidArchiverName";
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));

        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalidArchiverName not found.", e.getMessage());
        }

        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = null;

        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullArchiverName() throws Exception {
        String archiverName = null;
        InputStream inputStream = new FileInputStream("test_files/test_archive.ar");

        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Archivername must not be null.", e.getMessage());
        }

        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_AR_SignatureCheck() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new FileInputStream("test_files/test_archive.ar");

        ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        IOUtils.closeQuietly(inputStream);

        ArchiveInputStream expectedResult = new ArArchiveInputStream(new FileInputStream("test_files/test_archive.ar"));
        IOUtils.closeQuietly(expectedResult);

        Assert.assertSame(expectedResult.getClass(), result.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_UnknownSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));

        try {
            ArchiveStreamFactory.newInstance().createArchiveInputStream(null, inputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("No Archiver found for the stream signature", e.getMessage());
        }

        inputStream.close();
    }

    //... Repeat the above test cases for other methods: createArchiveInputStream(String archiverName, OutputStream out), createArchiveInputStream(InputStream in)

    //... Add edge cases and exception handling scenarios tests for other methods
}