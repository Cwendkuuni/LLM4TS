import org.apache.commons.compress.archivers.*; // Import all archivers to test various cases
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.input.ArArchiveInputStream;
import org.apache.commons.compress.archivers.input.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.input.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.input.JarArchiveInputStream;
import org.apache.commons.compress.archivers.input.TarArchiveInputStream;
import org.apache.commons.compress.archivers.output.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.output.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.output.DumpArchiveOutputStream;
import org.apache.commons.compress.archivers.output.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.output.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory class
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}.
     * Test case for valid archives.
     */
    @Test
    public void testCreateArchiveInputStreamWithValidArchive() throws IOException {
        String archiverName = "zip";

        InputStream inputStream = getTestArchiveInputStream(archiverName);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
        inputStream.close();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}.
     * Test case for unknown archiver.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiver() throws IOException {
        String unknownArchiverName = "unknown";
        InputStream inputStream = getTestArchiveInputStream(unknownArchiverName);

        new ArchiveStreamFactory().createArchiveInputStream(unknownArchiverName, inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}.
     * Test case for valid archives.
     */
    @Test
    public void testCreateArchiveInputStreamWithValidArchiveAutoDetect() throws IOException {
        InputStream inputStream = getTestArchiveInputStream("zip");
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertSame(ZipArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
        inputStream.close();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}.
     * Test case for unknown archiver.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiveAutoDetect() throws IOException {
        InputStream inputStream = getTestArchiveInputStream("unknown");
        new ArchiveStreamFactory().createArchiveInputStream(inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}.
     * Test case for valid archives.
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidArchive() throws IOException {
        String archiverName = "zip";

        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write("Test content".getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveOutputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        JarArchiveEntry jarArchiveEntry = (JarArchiveEntry) archiveOutputStream.getNextEntry();
        Assert.assertEquals("test.txt", jarArchiveEntry.getName());

        Assert.assertTrue(IOUtils.contentEquals("Test content", archiveOutputStream));

        archiveOutputStream.close();
        inputStream.close();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}.
     * Test case for unknown archiver.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiver() throws IOException {
        String unknownArchiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();

        new ArchiveStreamFactory().createArchiveOutputStream(unknownArchiverName, outputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(byte[], int)}
     * Test case for valid archives.
     */
    @Test
    public void testCreateArchiveInputStreamWithValidArchiveSignature() throws IOException {
        String archiverName = "tar";

        byte[] signature = TarArchiveInputStream.getMagicNumber();

        InputStream inputStream = new ByteArrayInputStream(signature);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        Assert.assertSame(TarArchiveInputStream.class, archiveInputStream.getClass());
        archiveInputStream.close();
        inputStream.close();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(byte[], int)}
     * Test case for unknown archiver.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiveSignature() throws IOException {
        byte[] unknownSignature = new byte[10]; // arbitrary signature

        InputStream inputStream = new ByteArrayInputStream(unknownSignature);
        new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(byte[], int)}
     * Test case for valid archives.
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidArchiveSignature() throws IOException {
        String archiverName = "tar";

        byte[] signature = TarArchiveOutputStream.getMagicNumber();

        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.writeTo("Test content".getBytes());
        archiveOutputStream.finishEntry();
        archiveOutputStream.close();

        byte[] output = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(output);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", tarArchiveEntry.getName());

        Assert.assertTrue(IOUtils.contentEquals("Test content", archiveInputStream));

        archiveInputStream.close();
        inputStream.close();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(byte[], int)}
     * Test case for unknown archiver.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiveSignature() throws IOException {
        byte[] unknownSignature = new byte[10]; // arbitrary signature

        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream, unknownSignature.length);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(byte[], int)}
     * Test case for invalid signature length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithInvalidSignatureLength() throws IOException {
        String archiverName = "tar";
        byte[] signature = TarArchiveInputStream.getMagicNumber();

        InputStream inputStream = new ByteArrayInputStream(signature);
        inputStream.mark(signature.length - 1);
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        inputStream.reset();
    }

    /**
     * Helper method to get a test archive input stream based on an archiver name.
     *
     * @param archiverName the archive name, i.e. "ar", "zip", "tar", "jar" or "cpio"
     * @return the archive input stream
     * @throws IOException if unable to create the test archive
     */
    private InputStream getTestArchiveInputStream(String archiverName) throws IOException {
        switch (archiverName) {
            case AR:
                return new ArArchiveInputStream(new ByteArrayInputStream(new ArTestData().getBytes()));
            case CPIO:
                return new CpioArchiveInputStream(new ByteArrayInputStream(new CpioTestData().getBytes()));
            case DUMP:
                return new DumpArchiveInputStream(new ByteArrayInputStream(new DumpTestData().getBytes()));
            case JAR:
                return new JarArchiveInputStream(new ByteArrayInputStream(new JarTestData().getBytes()));
            case TAR:
                return new TarArchiveInputStream(new ByteArrayInputStream(new TarTestData().getBytes()));
            case ZIP:
                return new ZipArchiveInputStream(new ByteArrayInputStream(new ZipTestData().getBytes()));
            default:
                throw new IllegalArgumentException("Invalid archiver name: " + archiverName);
        }
    }
}