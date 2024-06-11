import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream input = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, input));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input));
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, input));
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            OutputStream output = new ByteArrayOutputStream();
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output));
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output));
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(null, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamException() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            InputStream input = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(input));
        } catch (ArchiveException e) {
            fail("ArchiveException should not be thrown");
        }
    }
}