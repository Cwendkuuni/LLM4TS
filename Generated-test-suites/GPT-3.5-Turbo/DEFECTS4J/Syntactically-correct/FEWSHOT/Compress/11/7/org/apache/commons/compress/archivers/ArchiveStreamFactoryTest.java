import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamWithAr() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithCpio() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithDump() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithJar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithTar() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithZip() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiver() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithAr() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithCpio() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithJar() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithTar() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithZip() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiver() throws ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws ArchiveException, IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(archiveInputStream, outputStream);
        byte[] extractedData = outputStream.toByteArray();

        assertEquals(data.length, extractedData.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], extractedData[i]);
        }

        assertTrue(inputStream.markSupported());
        try {
            inputStream.reset();
        } catch (IOException e) {
            fail("InputStream should support reset operation");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamFromNullInputStream() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamFromInputStreamWithoutMarkSupport() throws ArchiveException, IOException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }
}