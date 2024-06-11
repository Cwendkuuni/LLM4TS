import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Test
    public void testCreateArchiveInputStreamWithKnownArchiverName() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        factory = new ArchiveStreamFactory();
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws Exception {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream("unknown", in);
            fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithKnownArchiverName() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        factory = new ArchiveStreamFactory();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(aos);
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream("unknown", out);
            fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws Exception {
        InputStream in = new FileInputStream("test.txt");
        factory = new ArchiveStreamFactory();
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectInvalidStream() throws Exception {
        try {
            factory = new ArchiveStreamFactory();
            ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
            in.mark(2); // Mark not supported
            factory.createArchiveInputStream(in);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}