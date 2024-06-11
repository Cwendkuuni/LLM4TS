import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

  @Test
  public void testCreateArchiveInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      
      // Test for AR archive format
      ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
      assertNotNull(arInputStream);
      
      // Test for ZIP archive format
      ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
      assertNotNull(zipInputStream);
      
      // Test for TAR archive format
      ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
      assertNotNull(tarInputStream);
      
      // Test for JAR archive format
      ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
      assertNotNull(jarInputStream);
      
      // Test for CPIO archive format
      ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
      assertNotNull(cpioInputStream);
      
      // Test for DUMP archive format
      ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
      assertNotNull(dumpInputStream);
      
    } catch (ArchiveException e) {
      fail("ArchiveException thrown: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveOutputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      
      // Test for AR archive format
      ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
      assertNotNull(arOutputStream);
      
      // Test for ZIP archive format
      ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
      assertNotNull(zipOutputStream);
      
      // Test for TAR archive format
      ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
      assertNotNull(tarOutputStream);
      
      // Test for JAR archive format
      ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
      assertNotNull(jarOutputStream);
      
      // Test for CPIO archive format
      ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
      assertNotNull(cpioOutputStream);
      
    } catch (ArchiveException e) {
      fail("ArchiveException thrown: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown: " + e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStream_autoDetect() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      
      // Test for ZIP archive format
      InputStream zipInput = null; // initialize with actual input stream
      ArchiveInputStream zipInputStream = factory.createArchiveInputStream(zipInput);
      assertNotNull(zipInputStream);
      
      // Test for JAR archive format
      InputStream jarInput = null; // initialize with actual input stream
      ArchiveInputStream jarInputStream = factory.createArchiveInputStream(jarInput);
      assertNotNull(jarInputStream);
      
      // Test for AR archive format
      InputStream arInput = null; // initialize with actual input stream
      ArchiveInputStream arInputStream = factory.createArchiveInputStream(arInput);
      assertNotNull(arInputStream);
      
      // Test for CPIO archive format
      InputStream cpioInput = null; // initialize with actual input stream
      ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(cpioInput);
      assertNotNull(cpioInputStream);
      
      // Test for TAR archive format
      InputStream tarInput = null; // initialize with actual input stream
      ArchiveInputStream tarInputStream = factory.createArchiveInputStream(tarInput);
      assertNotNull(tarInputStream);
      
      // Test for DUMP archive format
      InputStream dumpInput = null; // initialize with actual input stream
      ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(dumpInput);
      assertNotNull(dumpInputStream);
      
    } catch (ArchiveException e) {
      fail("ArchiveException thrown: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      fail("IllegalArgumentException thrown: " + e.getMessage());
    }
  }
}