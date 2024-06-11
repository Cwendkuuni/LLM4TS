
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
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
    
    @Test
    public void testCreateArchiveInputStream_AR() throws Exception {
        InputStream in = new FileInputStream("test.ar");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_ZIP() throws Exception {
        InputStream in = new FileInputStream("test.zip");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_TAR() throws Exception {
        InputStream in = new FileInputStream("test.tar");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_JAR() throws Exception {
        InputStream in = new FileInputStream("test.jar");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_CPIO() throws Exception {
        InputStream in = new FileInputStream("test.cpio");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        Assert.assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_DUMP() throws Exception {
        InputStream in = new FileInputStream("test.dump");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        Assert.assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_AR() throws Exception {
        OutputStream out = new FileOutputStream("test.ar");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        Assert.assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_ZIP() throws Exception {
        OutputStream out = new FileOutputStream("test.zip");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_TAR() throws Exception {
        OutputStream out = new FileOutputStream("test.tar");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        Assert.assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_JAR() throws Exception {
        OutputStream out = new FileOutputStream("test.jar");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        Assert.assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_CPIO() throws Exception {
        OutputStream out = new FileOutputStream("test.cpio");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        Assert.assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withInputStream() throws Exception {
        InputStream in = new FileInputStream("test.zip");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withInputStream_unknownArchive() throws Exception {
        InputStream in = new FileInputStream("test.unknown");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveInputStream(in);
            Assert.fail("ArchiveException should be thrown for unknown archive");
        } catch (ArchiveException e) {
            Assert.assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream_withInputStream() throws Exception {
        OutputStream out = new FileOutputStream("test.zip");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_withInputStream_unknownArchive() throws Exception {
        OutputStream out = new FileOutputStream("test.unknown");
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN, out);
            Assert.fail("ArchiveException should be thrown for unknown archive");
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: UNKNOWN not found.", e.getMessage());
        }
    }
    
}

