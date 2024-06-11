import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.format.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating ZIP ArchiveOutputStream and writing a file to it.
     */
    @Test
    public void testCreateZipArchiveOutputStream() throws IOException {
        final String outputFileName = "test_output.zip";
        final byte[] data = "Hello, World!".getBytes();

        try (OutputStream outputStream = new FileOutputStream(outputFileName)) {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
            archiveOutputStream.write(data, 0, data.length);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        try (InputStream inputStream = new FileInputStream(outputFileName)) {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            ArchiveEntry entry = archiveInputStream.getNextEntry();
            Assert.assertEquals("test.txt", entry.getName());
            Assert.assertTrue(entry instanceof ZipArchiveEntry);
            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) > 0) {
                Assert.assertEquals("Hello, World!", new String(buffer, 0, read));
            }
            archiveInputStream.close();
        }

        new File(outputFileName).delete();
    }

    /**
     * Test creating ZIP ArchiveInputStream and reading a file from it.
     */
    @Test
    public void testCreateZipArchiveInputStream() throws IOException {
        final String inputFileName = "test_input.zip";
        final byte[] data = "Hello, World!".getBytes();

        try (OutputStream outputStream = new FileOutputStream(inputFileName)) {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            archiveOutputStream.putArchiveEntry(new ZipArchiveEntry("test.txt"));
            archiveOutputStream.write(data, 0, data.length);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        try (InputStream inputStream = new FileInputStream(inputFileName)) {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            ArchiveEntry entry = archiveInputStream.getNextEntry();
            Assert.assertEquals("test.txt", entry.getName());
            Assert.assertTrue(entry instanceof ZipArchiveEntry);
            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) > 0) {
                Assert.assertEquals("Hello, World!", new String(buffer, 0, read));
            }
            archiveInputStream.close();
        }

        new File(inputFileName).delete();
    }

    /**
     * Test creating invalid ZIP ArchiveInputStream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateInvalidZipArchiveInputStream() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[]{0x75, 0x70, 0x6B, 0x69, 0x70, 0x65, 0x72, 0x63, 0x6F, 0x6D})) {
            new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        }
    }

    /**
     * Test creating AR ArchiveOutputStream and writing a file to it.
     */
    @Test
    public void testCreateArArchiveOutputStream() throws IOException {
        final String outputFileName = "test_output.ar";
        final byte[] data = "Hello, World!".getBytes();

        try (OutputStream outputStream = new FileOutputStream(outputFileName)) {
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
            archiveOutputStream.putArchiveEntry(new ArchiveEntry("test.txt"));
            archiveOutputStream.write(data, 0, data.length);
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        try (InputStream inputStream = new FileInputStream(outputFileName)) {
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
            ArchiveEntry entry = archiveInputStream.getNextEntry();
            Assert.assertEquals("test.txt", entry.getName());
            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) > 0) {
                Assert.assertEquals("Hello, World!", new String(buffer, 0, read));
            }
            archiveInputStream.close();
        }

        new File(outputFileName).delete();
    }

    /**
     * Test creating invalid AR ArchiveInputStream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateInvalidArArchiveInputStream() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A})) {
            new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        }
    }
}