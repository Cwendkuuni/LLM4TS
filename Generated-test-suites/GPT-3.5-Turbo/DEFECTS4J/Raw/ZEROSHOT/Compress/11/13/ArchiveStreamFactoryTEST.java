import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.*;
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

    private static final String TEST_DATA = "Just a test";

    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        
        assertTrue(ais instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        
        assertTrue(ais instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        
        assertTrue(ais instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        
        assertTrue(ais instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        
        assertTrue(ais instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        
        assertTrue(ais instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        factory.createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        
        assertTrue(aos instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        
        assertTrue(aos instanceof CpioArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        
        assertTrue(aos instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        
        assertTrue(aos instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        
        assertTrue(aos instanceof ZipArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZIP() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertTrue(ais instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_JAR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertTrue(ais instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_AR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertTrue(ais instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_CPIO() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertTrue(ais instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_DUMP() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertTrue(ais instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_TAR() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        
        assertTrue(ais instanceof TarArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NullInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_InputMarkNotSupported() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_UnknownSignature() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(TEST_DATA.getBytes());
        factory.createArchiveInputStream(in);
    }
}