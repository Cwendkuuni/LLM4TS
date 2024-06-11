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

public class ArchiveStreamFactoryTest {

    /**
     * Test instance without constructor arguments.
     */
    private ArchiveStreamFactory factory;

    /**
     * Test instance with encoding argument.
     */
    private ArchiveStreamFactory factoryWithEncoding;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
        factoryWithEncoding = new ArchiveStreamFactory("UTF-8");
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream inputStream = new ByteArrayInputStream("JarArchiveInputStream test data".getBytes());
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws ArchiveException, IOException {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream("ArchiveInputStream test data".getBytes());
        try {
            factory.createArchiveInputStream(archiverName, inputStream);
            Assert.fail("ArchiveException expected");
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws IOException, ArchiveException {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("ArchiveException expected");
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream inputStream = null;
        try {
            factory.createArchiveInputStream(archiverName, inputStream);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = null;
        try {
            factory.createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownArchiveFormat() throws ArchiveException, IOException {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream("ArchiveInputStream test data".getBytes());
        try {
            factory.createArchiveInputStream(archiverName, inputStream);
            Assert.fail("ArchiveException expected");
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknownArchiveFormatWithEncoding() throws IOException, ArchiveException {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            factoryWithEncoding.createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("ArchiveException expected");
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArchiveFormatAndEncoding() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream("ArchiveInputStream test data".getBytes());
        ArchiveInputStream archiveInputStream = factoryWithEncoding.createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals("UTF-8", archiveInputStream.getEncoding());
        archiveInputStream.close();
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStreamWithSevenZFormat() throws IOException {
        String archiverName = ArchiveStreamFactory.SEVEN_Z;
        InputStream inputStream = new ByteArrayInputStream("ArchiveInputStream test data".getBytes());
        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveInputStreamWithSevenZFormatWithEncoding() throws IOException {
        String archiverName = ArchiveStreamFactory.SEVEN_Z;
        InputStream inputStream = new ByteArrayInputStream("ArchiveInputStream test data".getBytes());
        factoryWithEncoding.createArchiveInputStream(archiverName, inputStream);
    }

}