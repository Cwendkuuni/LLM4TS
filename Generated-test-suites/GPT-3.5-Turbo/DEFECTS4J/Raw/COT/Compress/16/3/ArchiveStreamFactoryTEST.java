
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;


public class ArchiveStreamFactoryTest {
  
  @Test
  public void testCreateArchiveInputStream() throws Exception {
    ArchiveStreamFactory archiveFactory = new ArchiveStreamFactory();
    
    // Create input stream for testing
    byte[] data = "Test data".getBytes();
    InputStream inputStream = new ByteArrayInputStream(data);
    
    // Test for AR format
    try {
      ArchiveInputStream arInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
      assertTrue(arInputStream instanceof ArArchiveInputStream);
    } catch (Exception e) {
      fail("AR archive format not supported");
    }
    
    // Test for ZIP format
    try {
      ArchiveInputStream zipInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
      assertTrue(zipInputStream instanceof ZipArchiveInputStream);
    } catch (Exception e) {
      fail("ZIP archive format not supported");
    }
    
    // Test for TAR format
    try {
      ArchiveInputStream tarInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
      assertTrue(tarInputStream instanceof TarArchiveInputStream);
    } catch (Exception e) {
      fail("TAR archive format not supported");
    }
    
    // Test for JAR format
    try {
      ArchiveInputStream jarInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
      assertTrue(jarInputStream instanceof JarArchiveInputStream);
    } catch (Exception e) {
      fail("JAR archive format not supported");
    }
    
    // Test for CPIO format
    try {
      ArchiveInputStream cpioInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
      assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
    } catch (Exception e) {
      fail("CPIO archive format not supported");
    }
    
    // Test for DUMP format
    try {
      ArchiveInputStream dumpInputStream = archiveFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
      assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
    } catch (Exception e) {
      fail("DUMP archive format not supported");
    }
  }
  
  @Test
  public void testCreateArchiveOutputStream() throws Exception {
    ArchiveStreamFactory archiveFactory = new ArchiveStreamFactory();

    // Create output stream for testing
    OutputStream outputStream = new ByteArrayOutputStream();

    // Test for AR format
    try {
      ArchiveOutputStream arOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
      assertTrue(arOutputStream instanceof ArArchiveOutputStream);
    } catch (Exception e) {
      fail("AR archive format not supported");
    }

    // Test for ZIP format
    try {
      ArchiveOutputStream zipOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
      assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);
    } catch (Exception e) {
      fail("ZIP archive format not supported");
    }

    // Test for TAR format
    try {
      ArchiveOutputStream tarOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
      assertTrue(tarOutputStream instanceof TarArchiveOutputStream);
    } catch (Exception e) {
      fail("TAR archive format not supported");
    }

    // Test for JAR format
    try {
      ArchiveOutputStream jarOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
      assertTrue(jarOutputStream instanceof JarArchiveOutputStream);
    } catch (Exception e) {
      fail("JAR archive format not supported");
    }

    // Test for CPIO format
    try {
      ArchiveOutputStream cpioOutputStream = archiveFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
      assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);
    } catch (Exception e) {
      fail("CPIO archive format not supported");
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamAutoDetect() throws Exception {
    ArchiveStreamFactory archiveFactory = new ArchiveStreamFactory();

    // Create input stream for testing
    byte[] data = "Test data".getBytes();
    InputStream inputStream = new ByteArrayInputStream(data);

    // Test for ZIP format
    try {
      ArchiveInputStream zipInputStream = archiveFactory.createArchiveInputStream(inputStream);
      assertTrue(zipInputStream instanceof ZipArchiveInputStream);
    } catch (Exception e) {
      fail("ZIP archive format not supported");
    }

    // Test for JAR format
    try {
      ArchiveInputStream jarInputStream = archiveFactory.createArchiveInputStream(inputStream);
      assertTrue(jarInputStream instanceof JarArchiveInputStream);
    } catch (Exception e) {
      fail("JAR archive format not supported");
    }

    // Test for AR format
    try {
      ArchiveInputStream arInputStream = archiveFactory.createArchiveInputStream(inputStream);
      assertTrue(arInputStream instanceof ArArchiveInputStream);
    } catch (Exception e) {
      fail("AR archive format not supported");
    }

    // Test for CPIO format
    try {
      ArchiveInputStream cpioInputStream = archiveFactory.createArchiveInputStream(inputStream);
      assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
    } catch (Exception e) {
      fail("CPIO archive format not supported");
    }

    // Test for DUMP format
    try {
      ArchiveInputStream dumpInputStream = archiveFactory.createArchiveInputStream(inputStream);
      assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
    } catch (Exception e) {
      fail("DUMP archive format not supported");
    }

    // Test for TAR format
    try {
      ArchiveInputStream tarInputStream = archiveFactory.createArchiveInputStream(inputStream);
      assertTrue(tarInputStream instanceof TarArchiveInputStream);
    } catch (Exception e) {
      fail("TAR archive format not supported");
    }
  }
  
}
