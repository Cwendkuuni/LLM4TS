import static org.junit.Assert.*;
import org.junit.Test;
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

public class ArchiveStreamFactoryTest {
  
  @Test
  public void testCreateArchiveInputStream() throws ArchiveException {
    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
    assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    
    inputStream = new ByteArrayInputStream("test".getBytes());
    archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    
    inputStream = new ByteArrayInputStream("test".getBytes());
    archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
    assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    
    inputStream = new ByteArrayInputStream("test".getBytes());
    archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
    assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    
    inputStream = new ByteArrayInputStream("test".getBytes());
    archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
    assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    
    inputStream = new ByteArrayInputStream("test".getBytes());
    archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
    assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    
    try {
      archiveInputStream = archiveStreamFactory.createArchiveInputStream("invalid", inputStream);
      fail("Expected ArchiveException to be thrown");
    } catch (ArchiveException e) {
      assertEquals("Archiver: invalid not found.", e.getMessage());
    }
    
    try {
      archiveInputStream = archiveStreamFactory.createArchiveInputStream(null, inputStream);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Archivername must not be null.", e.getMessage());
    }
    
    try {
      archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
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
    
    outputStream = new ByteArrayOutputStream();
    archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
    assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    
    outputStream = new ByteArrayOutputStream();
    archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    
    outputStream = new ByteArrayOutputStream();
    archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
    assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    
    outputStream = new ByteArrayOutputStream();
    archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
    assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    
    try {
      archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("invalid", outputStream);
      fail("Expected ArchiveException to be thrown");
    } catch (ArchiveException e) {
      assertEquals("Archiver: invalid not found.", e.getMessage());
    }
    
    try {
      archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(null, outputStream);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Archivername must not be null.", e.getMessage());
    }
    
    try {
      archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("OutputStream must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithoutArchiverName() throws ArchiveException {
    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
    assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithoutArchiverNameNoMatch() throws ArchiveException {
    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
    InputStream inputStream = new ByteArrayInputStream("test".getBytes());
    try {
      archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
      fail("Expected ArchiveException to be thrown");
    } catch (ArchiveException e) {
      assertEquals("No Archiver found for the stream signature", e.getMessage());
    }
  }
}