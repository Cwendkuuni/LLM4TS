import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveException;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveException;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveException;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Testing createArchiveInputStream with valid archiver names
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());

        ArchiveInputStream inAr = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(inAr);

        ArchiveInputStream inCpio = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertNotNull(inCpio);

        ArchiveInputStream inDump = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertNotNull(inDump);

        ArchiveInputStream inJar = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(inJar);

        ArchiveInputStream inTar = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertNotNull(inTar);

        ArchiveInputStream inZip = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(inZip);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());

        try {
            ArchiveInputStream inInvalid = ArchiveStreamFactory.getInstance().createArchiveInputStream("invalidArchiver", inputStream);
            assert (false); // should not reach this point
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            assert (false); // should not reach this point
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Testing createArchiveOutputStream with valid archiver names
        OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveOutputStream outAr = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(outAr);

        ArchiveOutputStream outCpio = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertNotNull(outCpio);

        ArchiveOutputStream outDump = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, outputStream);
        assertNotNull(outDump);

        ArchiveOutputStream outJar = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertNotNull(outJar);

        ArchiveOutputStream outTar = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(outTar);

        ArchiveOutputStream outZip = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(outZip);
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            ArchiveOutputStream outInvalid = ArchiveStreamFactory.getInstance().createArchiveOutputStream("invalidArchiver", outputStream);
            assert (false); // should not reach this point
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            assert (false); // should not reach this point
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("validArchiveData".getBytes());

        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromNullInputStream() throws Exception {
        ArchiveStreamFactory.getInstance().createArchiveInputStream(null);
    }
}