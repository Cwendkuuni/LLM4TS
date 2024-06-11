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

    private static final String TEST_FILE_CONTENT = "Test data.";
    private static final String TEST_ARCHIVE_NAME = "test.tar";

    private OutputStream outputStream;
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes());
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
        // Basic test for creating ArchiveInputStream from name and InputStream
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        Assert.assertNotNull(archiveInputStream);

        // Test for edge case: invalid archiver name
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("InvalidArchiver", inputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: InvalidArchiver not found.", e.getMessage());
        }

        // Test for error scenario: null InputStream
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Basic test for creating ArchiveInputStream from InputStream
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);

        // Test for edge case: invalid signature
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream ao = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, os);
        ao.putNextEntry(new TarArchiveEntry("TestEntry.txt"));
        ao.write(TEST_FILE_CONTENT.getBytes());
        ao.closeArchiveEntry();
        ao.finish();
        ArchiveInputStream is = new TarArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(is);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("No Archiver found for the stream signature", e.getMessage());
        }

        // Test for error scenario: null InputStream
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(null);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        // Basic test for creating ArchiveOutputStream from name and OutputStream
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        Assert.assertNotNull(archiveOutputStream);

        // Test for edge case: invalid archiver name
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("InvalidArchiver", outputStream);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: InvalidArchiver not found.", e.getMessage());
        }

        // Test for error scenario: null OutputStream
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream("TAR", null);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws Exception {
        // Basic test for creating ArchiveOutputStream from OutputStream
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(outputStream);
        Assert.assertNotNull(archiveOutputStream);

        // Test for edge case: invalid signature
        ArchiveOutputStream os = new ArArchiveOutputStream(new ByteArrayOutputStream());
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(os);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: ar not found.", e.getMessage());
        }

        // Test for error scenario: null OutputStream
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(null);
            Assert.fail();
        } catch (ArchiveException e) {
            Assert.assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
}