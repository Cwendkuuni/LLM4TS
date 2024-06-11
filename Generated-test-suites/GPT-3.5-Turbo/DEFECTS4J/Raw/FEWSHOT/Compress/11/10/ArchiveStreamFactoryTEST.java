import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Mock
    private InputStream inputStream;

    @Mock
    private OutputStream outputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", inputStream);
        assertNotNull(archiveInputStream);

        archiveInputStream = archiveStreamFactory.createArchiveInputStream("ar", inputStream);
        assertNotNull(archiveInputStream);

        archiveInputStream = archiveStreamFactory.createArchiveInputStream("tar", inputStream);
        assertNotNull(archiveInputStream);

        archiveInputStream = archiveStreamFactory.createArchiveInputStream("jar", inputStream);
        assertNotNull(archiveInputStream);

        archiveInputStream = archiveStreamFactory.createArchiveInputStream("cpio", inputStream);
        assertNotNull(archiveInputStream);

        archiveInputStream = archiveStreamFactory.createArchiveInputStream("dump", inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamInvalidArchiverName() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveInputStream("invalid", inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveInputStream("zip", null);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", outputStream);
        assertNotNull(archiveOutputStream);

        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("ar", outputStream);
        assertNotNull(archiveOutputStream);

        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("tar", outputStream);
        assertNotNull(archiveOutputStream);

        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("jar", outputStream);
        assertNotNull(archiveOutputStream);

        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("cpio", outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamInvalidArchiverName() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveOutputStream("invalid", outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveOutputStream("zip", null);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException, IOException {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectNullInputStream() throws ArchiveException, IOException {
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectUnsupportedMark() throws ArchiveException, IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[10]);
        archiveStreamFactory.createArchiveInputStream(byteArrayInputStream);
    }

}