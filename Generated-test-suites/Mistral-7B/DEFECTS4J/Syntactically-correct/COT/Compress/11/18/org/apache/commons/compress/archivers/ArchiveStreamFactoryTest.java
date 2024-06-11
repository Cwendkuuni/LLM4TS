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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream expectedResult = new ZipArchiveInputStream(inputStream);

        ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(result);
        assertTrue(result.getClass().isAssignableFrom(expectedResult.getClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[10]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        factory.createArchiveInputStream("UnknownArchiver", inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expectedResult = new JarArchiveOutputStream(outputStream);

        ArchiveOutputStream result = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(result);
        assertTrue(result.getClass().isAssignableFrom(expectedResult.getClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() {
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("UnknownArchiver", outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidSignature() {
        InputStream inputStream = new ByteArrayInputStream(ZipArchiveInputStream.SIGNATURE.getBytes());
        ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(result);
        assertTrue(result.getClass().isAssignableFrom(ZipArchiveInputStream.class));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidSignature() {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetection() {
        InputStream inputStream = new ByteArrayInputStream(new byte[512]);
        ArchiveInputStream result = ArchiveStreamFactory.newInstance().createArchiveInputStream(ArchiveStreamFactory.UNKNOWN, inputStream);
        // This test assumes that the autodetection is implemented correctly
        // and returns an instance of TarArchiveInputStream for a tarball.
        assertTrue(result.getClass().isAssignableFrom(TarArchiveInputStream.class));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidAutodetection() {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveInputStream(ArchiveStreamFactory.UNKNOWN, inputStream);
    }
}