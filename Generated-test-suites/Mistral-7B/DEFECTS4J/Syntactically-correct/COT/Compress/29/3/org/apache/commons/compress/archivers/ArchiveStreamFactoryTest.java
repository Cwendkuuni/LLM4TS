import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.Streams;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.zip.ZipEntry;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test creating archive input streams with various archive types
        InputStream input = new ByteArrayInputStream(new byte[0]);
        for (String archiverName : ArchiveStreamFactory.values()) {
            ArchiveInputStream archiveInputStream = null;
            try {
                archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, input);
                Assert.assertNotNull("Failed to create input stream for " + archiverName, archiveInputStream);
                archiveInputStream.close();
            } catch (ArchiveException e) {
                Assert.fail("Failed to create input stream for " + archiverName + ": " + e.getMessage());
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test creating archive output streams with various archive types
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (String archiverName : ArchiveStreamFactory.values()) {
            ArchiveOutputStream archiveOutputStream = null;
            try {
                archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
                Assert.assertNotNull("Failed to create output stream for " + archiverName, archiveOutputStream);
                archiveOutputStream.close();
            } catch (ArchiveException e) {
                Assert.fail("Failed to create output stream for " + archiverName + ": " + e.getMessage());
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromAutodetection() throws Exception {
        // Test creating archive input streams using autodetection
        InputStream input = new ByteArrayInputStream(new byte[0]);
        try {
            input.mark(512);
            byte[] tarHeader = new byte[512];
            int read = IOUtils.readFully(input, tarHeader);
            input.reset();
            TarArchiveInputStream tarInputStream = new TarArchiveInputStream(input);
            TarEntry entry = tarInputStream.getNextEntry();
            Assert.assertNotNull("Failed to read TarEntry from autodetected tar stream", entry);
            tarInputStream.close();
        } catch (IOException e) {
            Assert.fail("Failed to create input stream using autodetection: " + e.getMessage());
        }
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateSevenZInputStream() throws Exception {
        // Test creating a SevenZArchiveInputStream which is not supported
        InputStream input = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, input);
    }

    @Test
    public void testCreateArchiveInputStreamWithEncoding() throws Exception {
        // Test creating archive input streams with encoding
        String encoding = "UTF-8";
        InputStream input = new ByteArrayInputStream("Test".getBytes(encoding));
        for (String archiverName : ArchiveStreamFactory.values()) {
            ArchiveInputStream archiveInputStream = null;
            try {
                archiveInputStream = new ArchiveStreamFactory(encoding).createArchiveInputStream(archiverName, input);
                Assert.assertNotNull("Failed to create input stream for " + archiverName, archiveInputStream);
                archiveInputStream.close();
            } catch (ArchiveException e) {
                Assert.fail("Failed to create input stream for " + archiverName + ": " + e.getMessage());
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithEncoding() throws Exception {
        // Test creating archive output streams with encoding
        String encoding = "UTF-8";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (String archiverName : ArchiveStreamFactory.values()) {
            ArchiveOutputStream archiveOutputStream = null;
            try {
                archiveOutputStream = new ArchiveStreamFactory(encoding).createArchiveOutputStream(archiverName, outputStream);
                Assert.assertNotNull("Failed to create output stream for " + archiverName, archiveOutputStream);
                archiveOutputStream.finish();
                archiveOutputStream.close();
            } catch (ArchiveException | IOException e) {
                Assert.fail("Failed to create output stream for " + archiverName + ": " + e.getMessage());
            }
        }

        // Test writing an entry to an archive output stream
        ZipEntry entry = new ZipEntry("Test.txt");
        byte[] data = "Test data".getBytes();
        ArchiveOutputStream archiveOutputStream = null;
        try {
            archiveOutputStream = new ArchiveStreamFactory(encoding).createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
            archiveOutputStream.putNextEntry(entry);
            archiveOutputStream.write(data, 0, data.length);
            archiveOutputStream.finish();
            archiveOutputStream.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveOutputStream.toByteArray());
            ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream);
            ZipEntry testEntry = zipInput.getNextEntry();
            Assert.assertEquals("Test.txt", testEntry.getName());
            byte[] readData = new byte[data.length];
            IOUtils.readFully(zipInput, readData);
            Assert.assertArrayEquals("Test data", data, readData);
        } catch (IOException e) {
            Assert.fail("Failed to write test data to archive: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(archiveOutputStream);
            IOUtils.closeQuietly(zipInput);
        }
    }
}