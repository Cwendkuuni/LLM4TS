
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream_withArArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withZipArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withTarArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withJarArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withCpioArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        Assert.assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withDumpArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        Assert.assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_withArArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_withZipArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_withTarArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_withJarArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_withCpioArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);
    }
}
