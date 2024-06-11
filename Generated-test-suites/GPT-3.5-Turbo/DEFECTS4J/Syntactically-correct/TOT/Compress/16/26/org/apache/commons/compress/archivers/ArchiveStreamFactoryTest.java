import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

  private ArchiveStreamFactory factory;

  @Before
  public void setUp() {
    factory = new ArchiveStreamFactory();
  }

  @Test
  public void testCreateArchiveInputStream_AR_ValidInputStream() {
    try {
      InputStream in = new FileInputStream(new File("test.ar"));
      ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
      assertNotNull(ais);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStream_AR_NullInputStream() {
    try {
      ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
      fail("Expected IllegalArgumentException but no exception occurred");
    } catch (IllegalArgumentException e) {
      assertEquals("InputStream must not be null.", e.getMessage());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStream_AR_InvalidArchiverName() {
    try {
      InputStream in = new FileInputStream(new File("test.ar"));
      ArchiveInputStream ais = factory.createArchiveInputStream("invalid", in);
      fail("Expected ArchiveException but no exception occurred");
    } catch (ArchiveException e) {
      assertEquals("Archiver: invalid not found.", e.getMessage());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveOutputStream_ZIP_ValidOutputStream() {
    try {
      OutputStream out = new FileOutputStream(new File("test.zip"));
      ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
      assertNotNull(aos);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveOutputStream_ZIP_NullOutputStream() {
    try {
      ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
      fail("Expected IllegalArgumentException but no exception occurred");
    } catch (IllegalArgumentException e) {
      assertEquals("OutputStream must not be null.", e.getMessage());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveOutputStream_ZIP_InvalidArchiverName() {
    try {
      OutputStream out = new FileOutputStream(new File("test.zip"));
      ArchiveOutputStream aos = factory.createArchiveOutputStream("invalid", out);
      fail("Expected ArchiveException but no exception occurred");
    } catch (ArchiveException e) {
      assertEquals("Archiver: invalid not found.", e.getMessage());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStream_Autodetect_ValidInputStream() {
    try {
      InputStream in = new FileInputStream(new File("test.zip"));
      ArchiveInputStream ais = factory.createArchiveInputStream(in);
      assertNotNull(ais);
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStream_Autodetect_NullInputStream() {
    try {
      ArchiveInputStream ais = factory.createArchiveInputStream(null);
      fail("Expected IllegalArgumentException but no exception occurred");
    } catch (IllegalArgumentException e) {
      assertEquals("Stream must not be null.", e.getMessage());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStream_Autodetect_InvalidSignature() {
    try {
      InputStream in = new FileInputStream(new File("test.invalid"));
      ArchiveInputStream ais = factory.createArchiveInputStream(in);
      fail("Expected ArchiveException but no exception occurred");
    } catch (ArchiveException e) {
      assertEquals("No Archiver found for the stream signature", e.getMessage());
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
}