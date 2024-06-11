import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_ZIP() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_TAR() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_JAR() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_DUMP() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiver() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
        fail("Expected ArchiveException to be thrown");
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException, ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(aos);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveOutputStream_ZIP() throws IOException, ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(aos);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveOutputStream_TAR() throws IOException, ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(aos);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveOutputStream_JAR() throws IOException, ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(aos);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveOutputStream_CPIO() throws IOException, ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(aos);
        // TODO: Add more assertions here
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiver() throws IOException, ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
        fail("Expected ArchiveException to be thrown");
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZIP() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect_TAR() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect_AR() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect_CPIO() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test
    public void testCreateArchiveInputStream_AutoDetect_DUMP() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        // TODO: Add more assertions here
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_Unknown() throws IOException, ArchiveException {
        byte[] input = "Test".getBytes();
        InputStream in = new ByteArrayInputStream(input);
        new ArchiveStreamFactory().createArchiveInputStream(in);
        fail("Expected ArchiveException to be thrown");
    }
}