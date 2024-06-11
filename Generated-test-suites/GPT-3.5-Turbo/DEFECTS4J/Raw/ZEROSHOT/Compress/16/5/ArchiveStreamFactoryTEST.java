import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null, new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("zip", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream(null, new OutputStream() {
            @Override
            public void write(int b) {
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("zip", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullStream() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_StreamDoesNotSupportMark() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        });
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[1024]));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() throws ArchiveException {
        archiveStreamFactory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_NoArchiverForStreamSignature() throws ArchiveException {
        archiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[12]));
    }

    @Test
    public void testCreateArchiveInputStream_Zip() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream("zip", in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.zip.ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Jar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream("jar", in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.jar.JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Ar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream("ar", in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.ar.ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Cpio() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream("cpio", in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream("zip", out);
        assertNotNull(aos);
        assertTrue(aos instanceof org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream("jar", out);
        assertNotNull(aos);
        assertTrue(aos instanceof org.apache.commons.compress.archivers.jar.JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Ar() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream("ar", out);
        assertNotNull(aos);
        assertTrue(aos instanceof org.apache.commons.compress.archivers.ar.ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream("cpio", out);
        assertNotNull(aos);
        assertTrue(aos instanceof org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Zip() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.zip.ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Jar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.jar.JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Ar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.ar.ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Cpio() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_Tar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.tar.TarArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Autodetect_NoArchiverForStreamSignature() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[12]);
        archiveStreamFactory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Autodetect_IOExceptionOnReset() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof org.apache.commons.compress.archivers.zip.ZipArchiveInputStream);
        // Force IOException on reset
        ais.reset();
    }
}