import static org.junit.Assert.*;
import org.junit.Test;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        
        try {
            archiveStreamFactory.createArchiveInputStream("invalid", inputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveInputStream(null, inputStream);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        
        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        
        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
        
        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
        
        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
        
        try {
            archiveStreamFactory.createArchiveOutputStream("invalid", outputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveOutputStream(null, outputStream);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() throws ArchiveException, IOException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        
        inputStream = new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x03, 0x04});
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        
        inputStream = new ByteArrayInputStream(new byte[]{0x52, 0x61, 0x72, 0x21, 0x1A, 0x07, 0x00});
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        
        inputStream = new ByteArrayInputStream(new byte[]{0x75, 0x73, 0x74, 0x61, 0x72, 0x00, 0x30});
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
        
        inputStream = new ByteArrayInputStream(new byte[]{0x55, 0x75, 0x30, 0x30, 0x39});
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
        
        inputStream = new ByteArrayInputStream(new byte[]{0x50, 0x4B, 0x05, 0x06});
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
        
        inputStream = new ByteArrayInputStream(new byte[]{0x1F, 0x8B, 0x08});
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        
        inputStream = new ByteArrayInputStream(new byte[]{0x1F, 0x9D});
        archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        
        try {
            inputStream = new ByteArrayInputStream(new byte[]{0x00});
            archiveStreamFactory.createArchiveInputStream(inputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
        
        try {
            inputStream = new ByteArrayInputStream(new byte[]{0x00});
            archiveStreamFactory.createArchiveInputStream(inputStream);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Could not use reset and mark operations.", e.getMessage());
        }
    }
}