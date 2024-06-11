import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() {
        try {
            InputStream in = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(archiveInputStream);
        } catch (ArchiveException e) {
            fail("Exception thrown unexpectedly: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertNotNull(archiveOutputStream);
        } catch (ArchiveException e) {
            fail("Exception thrown unexpectedly: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullArchiverName() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamNullInputStream() {
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputNullArchiverName() {
        OutputStream out = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputNullOutputStream() {
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamStreamNull() {
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        } catch (ArchiveException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
}