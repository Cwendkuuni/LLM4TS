import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_validArguments_correctArchiverReturned() {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes()); // Zip signature for testing

        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveInputStream actualArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        assertSame(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullArchiverName() {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());

        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() {
        String archiverName = "zip";
        InputStream inputStream = null;

        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        } catch(ArchiveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiverName() {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());

        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetection_correctArchiverReturned() {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream("Zip signature".getBytes());

        ArchiveInputStream actualArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);

        assertInstanceOf(ZipArchiveInputStream.class, actualArchiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_validArguments_correctArchiverReturned() {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveOutputStream expectedArchiveOutputStream = new ZipArchiveOutputStream(outputStream);

        ArchiveOutputStream actualArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);

        assertSame(expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() {
        String archiverName = null;
        OutputStream outputStream = new ByteArrayOutputStream();

        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() {
        String archiverName = "zip";
        OutputStream outputStream = null;

        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        } catch(ArchiveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_unknownArchiverName() {
        String archiverName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();

        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }
}