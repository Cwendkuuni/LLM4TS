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
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream("AR archive data".getBytes());
        ArchiveInputStream expectedResult = new ArArchiveInputStream(inputStream);

        ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);

        assertEquals(expectedResult.getClass(), result.getClass());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() {
        String archiverName = "unknown";
        InputStream inputStream = new ByteArrayInputStream("AR archive data".getBytes());

        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();

        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInput() {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = null;

        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();

        factory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = null;

        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();

        factory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expectedResult = new ArArchiveOutputStream(outputStream);

        ArchiveOutputStream result = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);

        assertEquals(expectedResult.getClass(), result.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetection_AR() {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream("AR archive data".getBytes());
        inputStream.mark(12);

        ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);

        assertEquals(ArArchiveInputStream.class, result.getClass());
        inputStream.reset();
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetection_Unknown() {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream("unknown data".getBytes());

        try {
            ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
            fail("An ArchiveException should be thrown.");
        } catch (ArchiveException e) {
            // expected
        }
    }
}