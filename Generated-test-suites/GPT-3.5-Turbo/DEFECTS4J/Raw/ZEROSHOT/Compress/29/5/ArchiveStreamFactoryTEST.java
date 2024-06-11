import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactoryTest {

  @Test
  public void testCreateArchiveInputStream() {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    try {
        ArchiveInputStream input = factory.createArchiveInputStream("zip", null);
        fail("IllegalArgumentException not thrown for null InputStream");
    } catch (IllegalArgumentException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for null InputStream");
    }
    try {
        ArchiveInputStream input = factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[] {}));
        fail("IllegalArgumentException not thrown for null Archivername");
    } catch (IllegalArgumentException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for null Archivername");
    }
    try {
        ArchiveInputStream input = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[] {}));
        fail("ArchiveException not thrown for unknown Archivername");
    } catch (ArchiveException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for unknown Archivername");
    }
  }
  
  @Test
  public void testCreateArchiveOutputStream() {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    try {
        ArchiveOutputStream output = factory.createArchiveOutputStream("zip", null);
        fail("IllegalArgumentException not thrown for null OutputStream");
    } catch (IllegalArgumentException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for null OutputStream");
    }
    try {
        ArchiveOutputStream output = factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
        fail("IllegalArgumentException not thrown for null Archivername");
    } catch (IllegalArgumentException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for null Archivername");
    }
    try {
        ArchiveOutputStream output = factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
        fail("ArchiveException not thrown for unknown Archivername");
    } catch (ArchiveException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for unknown Archivername");
    }
  }
  
  @Test
  public void testCreateArchiveInputStream_autodetect() {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    try {
        ArchiveInputStream input = factory.createArchiveInputStream(null);
        fail("IllegalArgumentException not thrown for null InputStream");
    } catch (IllegalArgumentException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for null InputStream");
    }
    try {
        ArchiveInputStream input = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[] {}));
        fail("ArchiveException not thrown for unknown Archivername");
    } catch (ArchiveException e) {
        // Exception thrown as expected
    } catch (Exception e) {
        fail("Unexpected Exception thrown for unknown Archivername");
    }
  }
}