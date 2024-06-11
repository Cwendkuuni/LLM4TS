import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();

            InputStream in = new ByteArrayInputStream(new byte[0]);

            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.ar.ArArchiveInputStream);

            ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.zip.ZipArchiveInputStream);

            ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.tar.TarArchiveInputStream);

            ais = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.jar.JarArchiveInputStream);

            ais = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream);

            try {
                ais = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
                fail("Expected ArchiveException");
            } catch (ArchiveException e) {
                // Expected
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();

            OutputStream out = new ByteArrayOutputStream();

            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertNotNull(aos);
            assertTrue(aos instanceof org.apache.commons.compress.archivers.ar.ArArchiveOutputStream);

            aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(aos);
            assertTrue(aos instanceof org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream);

            aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(aos);
            assertTrue(aos instanceof org.apache.commons.compress.archivers.tar.TarArchiveOutputStream);

            aos = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertNotNull(aos);
            assertTrue(aos instanceof org.apache.commons.compress.archivers.jar.JarArchiveOutputStream);

            aos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertNotNull(aos);
            assertTrue(aos instanceof org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream);

            try {
                aos = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
                fail("Expected ArchiveException");
            } catch (ArchiveException e) {
                // Expected
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();

            byte[] data = new byte[0];
            ByteArrayInputStream in = new ByteArrayInputStream(data);

            ArchiveInputStream ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.zip.ZipArchiveInputStream);

            data = new byte[2];
            in = new ByteArrayInputStream(data);

            ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.jar.JarArchiveInputStream);

            data = new byte[8];
            in = new ByteArrayInputStream(data);

            ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.ar.ArArchiveInputStream);

            data = new byte[10];
            in = new ByteArrayInputStream(data);

            ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream);

            data = new byte[32];
            in = new ByteArrayInputStream(data);

            ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.dump.DumpArchiveInputStream);

            data = new byte[512];
            in = new ByteArrayInputStream(data);

            ais = factory.createArchiveInputStream(in);
            assertNotNull(ais);
            assertTrue(ais instanceof org.apache.commons.compress.archivers.tar.TarArchiveInputStream);

            data = new byte[1000];
            in = new ByteArrayInputStream(data);

            try {
                ais = factory.createArchiveInputStream(in);
                fail("Expected ArchiveException");
            } catch (ArchiveException e) {
                // Expected
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}