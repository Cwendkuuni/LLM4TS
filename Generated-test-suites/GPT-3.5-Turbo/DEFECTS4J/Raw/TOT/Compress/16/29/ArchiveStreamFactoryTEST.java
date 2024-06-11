import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

  @Test
  public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      InputStream in = new ByteArrayInputStream(new byte[10]);
      ArchiveInputStream result = factory.createArchiveInputStream("zip", in);
      assertNotNull(result);
    } catch (Exception e) {
      fail("Exception occurred");
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithNullArchiverNameAndInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      InputStream in = new ByteArrayInputStream(new byte[10]);
      ArchiveInputStream result = factory.createArchiveInputStream(null, in);
      fail("ArchiveException should have been thrown");
    } catch (ArchiveException e) {
      assertEquals("Archivername must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithInvalidArchiverNameAndInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      InputStream in = new ByteArrayInputStream(new byte[10]);
      ArchiveInputStream result = factory.createArchiveInputStream("invalid", in);
      fail("ArchiveException should have been thrown");
    } catch (ArchiveException e) {
      assertEquals("Archiver: invalid not found.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithNullInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      ArchiveInputStream result = factory.createArchiveInputStream("zip", null);
      fail("IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("InputStream must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      OutputStream out = new ByteArrayOutputStream();
      ArchiveOutputStream result = factory.createArchiveOutputStream("zip", out);
      assertNotNull(result);
    } catch (Exception e) {
      fail("Exception occurred");
    }
  }
  
  @Test
  public void testCreateArchiveOutputStreamWithNullArchiverNameAndOutputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      OutputStream out = new ByteArrayOutputStream();
      ArchiveOutputStream result = factory.createArchiveOutputStream(null, out);
      fail("ArchiveException should have been thrown");
    } catch (ArchiveException e) {
      assertEquals("Archivername must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveOutputStreamWithInvalidArchiverNameAndOutputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      OutputStream out = new ByteArrayOutputStream();
      ArchiveOutputStream result = factory.createArchiveOutputStream("invalid", out);
      fail("ArchiveException should have been thrown");
    } catch (ArchiveException e) {
      assertEquals("Archiver: invalid not found.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveOutputStreamWithNullOutputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      ArchiveOutputStream result = factory.createArchiveOutputStream("zip", null);
      fail("IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("OutputStream must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithValidInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      InputStream in = new ByteArrayInputStream(new byte[12]);
      ArchiveInputStream result = factory.createArchiveInputStream(in);
      assertNotNull(result);
    } catch (Exception e) {
      fail("Exception occurred");
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithNullInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      ArchiveInputStream result = factory.createArchiveInputStream(null);
      fail("IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Stream must not be null.", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithInputStreamNotSupportingMark() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      InputStream in = new ByteArrayInputStream(new byte[10]);
      ArchiveInputStream result = factory.createArchiveInputStream(in);
      fail("IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Mark is not supported.", e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStreamWithInvalidSignature() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      InputStream in = new ByteArrayInputStream(new byte[10]);
      ArchiveInputStream result = factory.createArchiveInputStream(in);
      fail("ArchiveException should have been thrown");
    } catch (ArchiveException e) {
      assertEquals("No Archiver found for the stream signature", e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamWithValidSignature() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      byte[] signature = new byte[4];
      signature[0] = 'Z';
      signature[1] = 'i';
      signature[2] = 'p';
      signature[3] = '\0';
      InputStream in = new ByteArrayInputStream(signature);
      ArchiveInputStream result = factory.createArchiveInputStream(in);
      assertNotNull(result);
    } catch (Exception e) {
      fail("Exception occurred");
    }
  }
  
}