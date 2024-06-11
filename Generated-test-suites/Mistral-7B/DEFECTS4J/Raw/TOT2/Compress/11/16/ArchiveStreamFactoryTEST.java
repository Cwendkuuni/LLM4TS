import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiverName = "zip";
        InputStream in = new ByteArrayInputStream("ZSTD compressed data".getBytes());
        ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertEquals(ArchiveStreamFactory.ZIP, archiverName);
        Assert.assertTrue(inputStream instanceof ZipArchiveInputStream);
        inputStream.close();

        archiverName = "tar";
        in = new ByteArrayInputStream(new byte[0]); // empty tar file
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertEquals(ArchiveStreamFactory.TAR, archiverName);
        Assert.assertTrue(inputStream instanceof TarArchiveInputStream);
        inputStream.close();

        archiverName = "jar";
        in = new ByteArrayInputStream(new byte[0]); // empty jar file
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertEquals(ArchiveStreamFactory.JAR, archiverName);
        Assert.assertTrue(inputStream instanceof JarArchiveInputStream);
        inputStream.close();

        archiverName = "ar";
        in = new ByteArrayInputStream(new byte[0]); // empty ar file
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertEquals(ArchiveStreamFactory.AR, archiverName);
        Assert.assertTrue(inputStream instanceof ArArchiveInputStream);
        inputStream.close();

        archiverName = "cpio";
        in = new ByteArrayInputStream(new byte[0]); // empty cpio file
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertEquals(ArchiveStreamFactory.CPIO, archiverName);
        Assert.assertTrue(inputStream instanceof CpioArchiveInputStream);
        inputStream.close();

        archiverName = "dump";
        in = new ByteArrayInputStream(new byte[0]); // empty dump file
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
        Assert.assertEquals(ArchiveStreamFactory.DUMP, archiverName);
        Assert.assertTrue(inputStream instanceof DumpArchiveInputStream);
        inputStream.close();

        String unsupportedArchiverName = "Test";
        in = new ByteArrayInputStream(new byte[0]); // empty test file
        try {
            inputStream = new ArchiveStreamFactory().createArchiveInputStream(unsupportedArchiverName, in);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiverName = "zip";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        outputStream.putArchiveEntry(new ZipArchiveEntry("testdata/test1.xml"));
        outputStream.write(new byte[1024], 0, 1024);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        byte[] result = ((ByteArrayOutputStream) out).toByteArray();
        Assert.assertArrayEquals(new byte[0], result, 0, 0); // check if empty

        String anotherArchiverName = "tar";
        out = new ByteArrayOutputStream();
        outputStream = new ArchiveStreamFactory().createArchiveOutputStream(anotherArchiverName, out);
        outputStream.putArchiveEntry(new TarArchiveEntry("testdata/test1.xml"));
        outputStream.write(new byte[1024], 0, 1024);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        result = ((ByteArrayOutputStream) out).toByteArray();
        Assert.assertArrayEquals(new byte[0], result, 0, 0); // check if empty

        // Test with a file
        InputStream fileInput = new FileInputStream("testdata/test_file.txt");
        archiverName = "jar";
        out = new ByteArrayOutputStream();
        outputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
        outputStream.putNextEntry(new JarArchiveEntry("testdata/test_file.txt"));
        IOUtils.copy(fileInput, outputStream);
        outputStream.closeEntry();
        outputStream.finish();
        outputStream.close();
        fileInput.close();

        result = ((ByteArrayOutputStream) out).toByteArray();
        Assert.assertArrayEquals(IOUtils.toByteArray(new FileInputStream("testdata/test_file.txt")), result);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidData() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("Test", in);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        String archiverName = ArchiveStreamFactory.ZIP;
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() {
        String archiverName = "Test";
        OutputStream out = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        new ArchiveStreamFactory().createArchiveInputStream("zip", null);
    }
}