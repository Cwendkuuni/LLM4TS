import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_withValidArchiverNameAndInputStream_shouldReturnArchiveInputStream() throws ArchiveException {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws ArchiveException {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        String archiverName = "zip";
        InputStream inputStream = null;

        archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withUnknownArchiverName_shouldThrowArchiveException() throws ArchiveException {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_withValidArchiverNameAndOutputStream_shouldReturnArchiveOutputStream()
            throws ArchiveException {
        String archiverName = "zip";
        OutputStream outputStream = System.out;

        ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName_shouldThrowIllegalArgumentException() throws ArchiveException {
        String archiverName = null;
        OutputStream outputStream = System.out;

        archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        String archiverName = "zip";
        OutputStream outputStream = null;

        archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_withUnknownArchiverName_shouldThrowArchiveException() throws ArchiveException {
        String archiverName = "unknown";
        OutputStream outputStream = System.out;

        archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withValidInputStream_shouldReturnArchiveInputStream() throws ArchiveException, IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(inputStream);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws ArchiveException {
        InputStream inputStream = null;

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withUnsupportedMark_shouldThrowIllegalArgumentException() throws ArchiveException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withUnrecognizedSignature_shouldThrowArchiveException() throws ArchiveException {
        byte[] signature = "UNKNOWN".getBytes();
        InputStream inputStream = new ByteArrayInputStream(signature);

        archiveStreamFactory.createArchiveInputStream(inputStream);
    }
}