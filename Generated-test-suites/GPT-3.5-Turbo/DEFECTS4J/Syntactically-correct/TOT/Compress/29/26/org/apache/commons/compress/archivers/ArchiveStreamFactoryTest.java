import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamForArFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream(new File("test.ar"));
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveInputStreamForArjFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream(new File("test.arj"));
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, in);
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveInputStreamForZipFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new FileInputStream(new File("test.zip"));
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
    }

    // Similar test cases for other formats

    @Test
    public void testCreateArchiveOutputStreamForArFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new FileOutputStream(new File("test.ar"));
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(aos);
    }

    @Test
    public void testCreateArchiveOutputStreamForZipFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new FileOutputStream(new File("test.zip"));
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(aos);
    }

    // Similar test cases for other formats

    @Test
    public void testCreateArchiveInputStreamFromStreamForZipFormat() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[] { 80, 75, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
    }

    // Similar test cases for other formats

}