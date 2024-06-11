import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.exception.ArchiveException;
import org.apache.commons.compress.archivers.tar.TarEntry;
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
        // Test creating ArchiveInputStream with known signatures
        InputStream input = new ByteArrayInputStream(ZipArchiveInputStream.getSignature());
        ArchiveInputStream zipStream = new ArchiveStreamFactory().createArchiveInputStream("zip", input);
        Assert.assertTrue(zipStream instanceof ZipArchiveInputStream);

        input = new ByteArrayInputStream(JarArchiveInputStream.getSignature());
        ArchiveInputStream jarStream = new ArchiveStreamFactory().createArchiveInputStream("jar", input);
        Assert.assertTrue(jarStream instanceof JarArchiveInputStream);

        input = new ByteArrayInputStream(ArArchiveInputStream.getSignature());
        ArchiveInputStream arStream = new ArchiveStreamFactory().createArchiveInputStream("ar", input);
        Assert.assertTrue(arStream instanceof ArArchiveInputStream);

        input = new ByteArrayInputStream(CpioArchiveInputStream.getSignature());
        ArchiveInputStream cpioStream = new ArchiveStreamFactory().createArchiveInputStream("cpio", input);
        Assert.assertTrue(cpioStream instanceof CpioArchiveInputStream);

        input = new ByteArrayInputStream(DumpArchiveInputStream.getSignature());
        ArchiveInputStream dumpStream = new ArchiveStreamFactory().createArchiveInputStream("dump", input);
        Assert.assertTrue(dumpStream instanceof DumpArchiveInputStream);

        input = new ByteArrayInputStream(TarArchiveInputStream.getSignature());
        ArchiveInputStream tarStream = new ArchiveStreamFactory().createArchiveInputStream("tar", input);
        Assert.assertTrue(tarStream instanceof TarArchiveInputStream);

        // Test creating ArchiveInputStream with unknown signature
        InputStream unknownSignature = new ByteArrayInputStream(new byte[10]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownSignature);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveInputStream with invalid signature
        InputStream invalidSignature = new ByteArrayInputStream(new byte[10]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", invalidSignature);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveInputStream with null input stream
        InputStream nullInputStream = null;
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", nullInputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating ArchiveInputStream with empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", emptyInputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveInputStream with input stream that doesn't support mark
        InputStream markNotSupportedInputStream = new ByteArrayInputStream(new byte[10]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", markNotSupportedInputStream);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating ArchiveInputStream with ArchiveException
        InputStream archiveExceptionInputStream = new ByteArrayInputStream(new byte[10]);
        TarArchiveInputStream tarStreamWithException = new TarArchiveInputStream(archiveExceptionInputStream);
        tarStreamWithException.setException(new IOException("Test error"));
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", archiveExceptionInputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
            Assert.assertEquals("Test error", e.getCause().getMessage());
        }

        // Test creating TarArchiveInputStream with a valid tar file
        InputStream tarInputStream = new ByteArrayInputStream(TarArchiveInputStream.getSignature());
        tarInputStream.read(new byte[TarArchiveInputStream.getSignature().length]);
        tarInputStream.reset();
        TarArchiveInputStream tarInput = new TarArchiveInputStream(tarInputStream);
        TarEntry entry = tarInput.getNextEntry();
        Assert.assertNotNull(entry);

        // Test creating ArchiveOutputStream with known archivers
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
        archiveOutputStream.close();

        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
        archiveOutputStream.putArchiveEntry(new TarEntry("test.txt"));
        archiveOutputStream.write(new byte[10], 0, 10);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        out.close();

        byte[] expectedZip = ZipArchiveOutputStream.getTestArchiveBytes();
        byte[] expectedTar = TarArchiveOutputStream.getTestArchiveBytes();

        Assert.assertArrayEquals(expectedZip, out.toByteArray());
        out = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
        archiveOutputStream.putArchiveEntry(new TarEntry("test.txt"));
        archiveOutputStream.write(new byte[10], 0, 10);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        out.close();

        Assert.assertArrayEquals(expectedTar, out.toByteArray());

        // Test creating ArchiveOutputStream with unknown archiver
        try {
            OutputStream unknownArchiverOutputStream = new ByteArrayOutputStream();
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", unknownArchiverOutputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveOutputStream with null output stream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("tar", null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating ArchiveOutputStream with empty output stream
        OutputStream emptyOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("tar", emptyOutputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
        }

        // Test creating ArchiveOutputStream with IOException
        OutputStream iOExceptionOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("tar", iOExceptionOutputStream);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        // Test creating ArchiveOutputStream with ArchiveException
        OutputStream archiveExceptionOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("tar", archiveExceptionOutputStream);
            TarArchiveOutputStream tarOutputWithException = (TarArchiveOutputStream) new ArchiveStreamFactory().createArchiveOutputStream("tar", archiveExceptionOutputStream);
            tarOutputWithException.setException(new IOException("Test error"));
            Assert.fail();
        } catch (ArchiveException e) {
            // Expected
            Assert.assertEquals("Test error", e.getCause().getMessage());
        }
    }
}