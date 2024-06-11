import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
  
  @Test
  public void testCreateArchiveInputStream() throws ArchiveException {
    // Test with known archive names
    ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
    assertNotNull(arInputStream);
    
    ArchiveInputStream zipInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
    assertNotNull(zipInputStream);
    
    ArchiveInputStream tarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
    assertNotNull(tarInputStream);
    
    ArchiveInputStream jarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
    assertNotNull(jarInputStream);
    
    ArchiveInputStream cpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
    assertNotNull(cpioInputStream);
    
    // Test with unknown archive name
    try {
      ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
      fail("Expected ArchiveException to be thrown");
    } catch (ArchiveException e) {
      assertEquals("Archiver: unknown not found.", e.getMessage());
    }
    
    // Test with null archiver name
    try {
      ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Archivername must not be null.", e.getMessage());
    }
    
    // Test with null input stream
    try {
      ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("InputStream must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveOutputStream() throws ArchiveException {
    // Test with known archive names
    ArchiveOutputStream arOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
    assertNotNull(arOutputStream);
    
    ArchiveOutputStream zipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
    assertNotNull(zipOutputStream);
    
    ArchiveOutputStream tarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
    assertNotNull(tarOutputStream);
    
    ArchiveOutputStream jarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
    assertNotNull(jarOutputStream);
    
    ArchiveOutputStream cpioOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
    assertNotNull(cpioOutputStream);
    
    // Test with unknown archive name
    try {
      ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayOutputStream());
      fail("Expected ArchiveException to be thrown");
    } catch (ArchiveException e) {
      assertEquals("Archiver: unknown not found.", e.getMessage());
    }
    
    // Test with null archiver name
    try {
      ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(null, new ByteArrayOutputStream());
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Archivername must not be null.", e.getMessage());
    }
    
    // Test with null output stream
    try {
      ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("OutputStream must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStream_autodetect() throws ArchiveException {
    // Test with valid ZIP input stream
    try {
      byte[] zipSignature = new byte[] { 80, 75, 3, 4 };
      ByteArrayInputStream zipInputStream = new ByteArrayInputStream(zipSignature);
      ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(zipInputStream);
      assertNotNull(archiveInputStream);
      assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    } catch (ArchiveException e) {
      fail("Expected ArchiveException not to be thrown");
    }
    
    // Test with valid JAR input stream
    try {
      byte[] jarSignature = new byte[] { 80, 75, 3, 4 };
      ByteArrayInputStream jarInputStream = new ByteArrayInputStream(jarSignature);
      ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(jarInputStream);
      assertNotNull(archiveInputStream);
      assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    } catch (ArchiveException e) {
      fail("Expected ArchiveException not to be thrown");
    }
    
    // Test with valid AR input stream
    try {
      byte[] arSignature = new byte[] { 33, 60, 97, 114 };
      ByteArrayInputStream arInputStream = new ByteArrayInputStream(arSignature);
      ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(arInputStream);
      assertNotNull(archiveInputStream);
      assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    } catch (ArchiveException e) {
      fail("Expected ArchiveException not to be thrown");
    }
    
    // Test with valid CPIO input stream
    try {
      byte[] cpioSignature = new byte[] { 59, 46, 46, 60 };
      ByteArrayInputStream cpioInputStream = new ByteArrayInputStream(cpioSignature);
      ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(cpioInputStream);
      assertNotNull(archiveInputStream);
      assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    } catch (ArchiveException e) {
      fail("Expected ArchiveException not to be thrown");
    }
    
    // Test with invalid input stream
    try {
      ByteArrayInputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
      ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(invalidInputStream);
      fail("Expected ArchiveException to be thrown");
    } catch (ArchiveException e) {
      assertEquals("No Archiver found for the stream signature", e.getMessage());
    }
    
    // Test with null input stream
    try {
      ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(null);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Stream must not be null.", e.getMessage());
    }
    
    // Test with input stream not supporting mark
    try {
      ByteArrayInputStream unsupportedInputStream = new ByteArrayInputStream(new byte[0]);
      ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(unsupportedInputStream);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Mark is not supported.", e.getMessage());
    }
  }
}