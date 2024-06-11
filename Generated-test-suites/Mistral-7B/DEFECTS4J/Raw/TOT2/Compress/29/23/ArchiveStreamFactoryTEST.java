import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;
    private OutputStream outputStream;
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        archiveStreamFactory = new ArchiveStreamFactory();
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @Test
    public void testCreateArchiveInputStreamFromArchiverNameAndInputStream() throws Exception {
        // Test createArchiveInputStream method with valid archiver name and InputStream
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInvalidArchiverNameAndInputStream() throws Exception {
        // Test createArchiveInputStream method with invalid archiver name and InputStream
        archiveStreamFactory.createArchiveInputStream("INVALID_ARCHIVER", inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamFromNullInputStream() throws Exception {
        // Test createArchiveInputStream method with null InputStream
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveOutputStreamFromArchiverNameAndOutputStream() throws Exception {
        // Test createArchiveOutputStream method with valid archiver name and OutputStream
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromInvalidArchiverNameAndOutputStream() throws Exception {
        // Test createArchiveOutputStream method with invalid archiver name and OutputStream
        archiveStreamFactory.createArchiveOutputStream("INVALID_ARCHIVER", outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamFromNullOutputStream() throws Exception {
        // Test createArchiveOutputStream method with null OutputStream
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test createArchiveInputStream method with InputStream and auto-detect archiver
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromNullInputStreamWithAutoDetection() throws Exception {
        // Test createArchiveInputStream method with null InputStream and auto-detection
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test
    public void testCloseArchiveInputStream() throws Exception {
        // Test closeArchiveEntry method with valid ArchiveInputStream
        InputStream testInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveOutputStream() throws Exception {
        // Test putArchiveEntry and closeArchiveEntry methods with valid ArchiveOutputStream
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        ArchiveEntry archiveEntry = new JarArchiveEntry("test.txt");
        archiveOutputStream.putNextEntry(archiveEntry);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveOutputStream() throws Exception {
        // Test putArchiveEntry method with null ArchiveOutputStream
        ArchiveEntry archiveEntry = new JarArchiveEntry("test.txt");
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null).putNextEntry(archiveEntry);
    }

    @Test
    public void testCreateArchiveInputStreamWithCustomEncodingAndCheckEntryEncoding() throws Exception {
        // Test createArchiveInputStream method with custom encoding and check entry encoding
        String encoding = "UTF-8";
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream, encoding);
        Assert.assertEquals(encoding, archiveInputStream.getEntryEncoding());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithCustomEncodingAndCheckEntryEncoding() throws Exception {
        // Test createArchiveOutputStream method with custom encoding and check entry encoding
        String encoding = "UTF-8";
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream, encoding);
        Assert.assertEquals(encoding, archiveOutputStream.getEncoding());
        archiveOutputStream.close();
    }
}