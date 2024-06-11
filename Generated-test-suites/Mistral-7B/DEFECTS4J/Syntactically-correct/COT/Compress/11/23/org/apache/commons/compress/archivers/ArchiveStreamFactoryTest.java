import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertInstanceOf(ArArchiveInputStream.class, ais);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]); // Zip signature
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertInstanceOf(ZipArchiveInputStream.class, ais);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Zip() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]); // Zip signature
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertInstanceOf(ZipArchiveInputStream.class, ais);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Autodetect_InvalidFormat() throws ArchiveException, IOException {
        InputStream in = new ByteArrayInputStream(new byte[10]); // random bytes
        new ArchiveStreamFactory().createArchiveInputStream(in);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertInstanceOf(ArArchiveOutputStream.class, aos);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownFormat() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertInstanceOf(ZipArchiveOutputStream.class, aos);
    }

    @Test
    public void testCreateArchiveOutputStream_Autodetect() throws ArchiveException, IOException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(out);
        // this is a Tar archive, it should still work
        assertInstanceOf(TarArchiveOutputStream.class, aos);
    }
}