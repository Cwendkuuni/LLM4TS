import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test creation with known archive types
        InputStream input = new ByteArrayInputStream(new byte[]{"P!PK\003\021\010\000\000\000"});
        ArchiveInputStream expectedZip = new ZipArchiveInputStream(input);
        ArchiveInputStream actualZip = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertEquals(expectedZip.getClass(), actualZip.getClass());

        input = new ByteArrayInputStream(new byte[]{"!<arch>\037\020tar\014\000\000\000"});
        ArchiveInputStream expectedTar = new TarArchiveInputStream(input);
        ArchiveInputStream actualTar = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        assertEquals(expectedTar.getClass(), actualTar.getClass());

        // Test creation with unknown archive type
        input = new ByteArrayInputStream(new byte[]{"!<arch>\014dump\014\000\000\000"});
        ArchiveInputStream result = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, input);
        assertInstanceOf(ArchiveException.class, result);

        // Test creation with null input
        assertThrows(NullPointerException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(null, null));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test creation with known archive types
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream expectedZip = new ZipArchiveOutputStream(output);
        ArchiveOutputStream actualZip = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertEquals(expectedZip.getClass(), actualZip.getClass());

        output = new ByteArrayOutputStream();
        ArchiveOutputStream expectedTar = new TarArchiveOutputStream(output);
        ArchiveOutputStream actualTar = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertEquals(expectedTar.getClass(), actualTar.getClass());

        // Test creation with unknown archive type
        output = new ByteArrayOutputStream();
        ArchiveOutputStream result = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertInstanceOf(ArchiveException.class, result);

        // Test creation with null output
        assertThrows(NullPointerException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(null, null));
    }

    @Test
    public void testAutoDetectArchiveType() throws Exception {
        // Test known archive types
        InputStream input = new ByteArrayInputStream(new byte[]{"P!PK\003\021\010\000\000\000"});
        ArchiveInputStream result = new ArchiveStreamFactory().createArchiveInputStream(null, input);
        assertEquals(ZipArchiveInputStream.class, result.getClass());

        input = new ByteArrayInputStream(new byte[]{"!<arch>\037\020tar\014\000\000\000"});
        ArchiveInputStream result2 = new ArchiveStreamFactory().createArchiveInputStream(null, input);
        assertEquals(TarArchiveInputStream.class, result2.getClass());

        // Test unknown archive type
        input = new ByteArrayInputStream(new byte[]{"!<arch>\014dump\014\000\000\000"});
        ArchiveInputStream result3 = new ArchiveStreamFactory().createArchiveInputStream(null, input);
        assertInstanceOf(ArchiveException.class, result3);
    }
}