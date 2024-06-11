
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        String archiverName = "zip";
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        try {
            ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);

            Assert.assertNotNull(ais);
            Assert.assertTrue(ais instanceof ZipArchiveInputStream);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        String archiverName = "zip";
        OutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        try {
            ArchiveOutputStream aos = factory.createArchiveOutputStream(archiverName, out);

            Assert.assertNotNull(aos);
            Assert.assertTrue(aos instanceof ZipArchiveOutputStream);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        try {
            ArchiveInputStream ais = factory.createArchiveInputStream(in);

            Assert.assertNotNull(ais);
            Assert.assertTrue(ais instanceof ZipArchiveOutputStream);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
}
