import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    private ArchiveStreamFactory factory;
    
    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }
    
    // Test createArchiveInputStream(archiverName, in) method
    
    @Test
    public void testCreateArchiveInputStreamWithArFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertTrue(ais instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithZipFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithTarFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertTrue(ais instanceof TarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithJarFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertTrue(ais instanceof JarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithCpioFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertTrue(ais instanceof CpioArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithDumpFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertTrue(ais instanceof DumpArchiveInputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", in);
        fail("Expected ArchiveException to be thrown.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
        fail("Expected IllegalArgumentException to be thrown.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        fail("Expected IllegalArgumentException to be thrown.");
    }
    
    // Test createArchiveOutputStream(archiverName, out) method
    
    @Test
    public void testCreateArchiveOutputStreamWithArFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertTrue(aos instanceof ArArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithZipFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertTrue(aos instanceof ZipArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithTarFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertTrue(aos instanceof TarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithJarFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertTrue(aos instanceof JarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithCpioFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertTrue(aos instanceof CpioArchiveOutputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
        fail("Expected ArchiveException to be thrown.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
        fail("Expected IllegalArgumentException to be thrown.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        fail("Expected IllegalArgumentException to be thrown.");
    }
    
    // Test createArchiveInputStream(in) method
    
    @Test
    public void testCreateArchiveInputStreamWithZipSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[] {80, 75, 3, 4});
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithJarSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[] {80, 75, 3, 4});
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertTrue(ais instanceof JarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithArSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[] {33, 60, 97, 114, 62, 10});
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertTrue(ais instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithCpioSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[] {070, 061, 0, 50, 0});
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertTrue(ais instanceof CpioArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithDumpSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[] {85, 115, 116, 97, 114, 0, 32, 32, 32, 32, 0});
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertTrue(ais instanceof DumpArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithTarSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[] {0, 61, 61, 61});
        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertTrue(ais instanceof TarArchiveInputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(in);
        fail("Expected ArchiveException to be thrown.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream2() throws Exception {
        factory.createArchiveInputStream(null);
        fail("Expected IllegalArgumentException to be thrown.");
    }
}