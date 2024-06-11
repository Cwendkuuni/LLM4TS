import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

  @Test
  public void testCreateArchiveInputStream_validNameValidStream() {
    // Arrange
    String archiverName = "zip";
    InputStream in = new ByteArrayInputStream(new byte[10]);

    // Act
    ArchiveInputStream result = ArchiveStreamFactory.createArchiveInputStream(archiverName, in);

    // Assert
    assertNotNull(result);
    assertEquals(ZipArchiveInputStream.class, result.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_nullNameValidStream() {
    // Arrange
    String archiverName = null;
    InputStream in = new ByteArrayInputStream(new byte[10]);

    // Act
    ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_validNameNullStream() {
    // Arrange
    String archiverName = "zip";
    InputStream in = null;

    // Act
    ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
  }

  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStream_invalidNameValidStream() {
    // Arrange
    String archiverName = "invalid";
    InputStream in = new ByteArrayInputStream(new byte[10]);

    // Act
    ArchiveStreamFactory.createArchiveInputStream(archiverName, in);
  }

  @Test
  public void testCreateArchiveOutputStream_validNameValidStream() {
    // Arrange
    String archiverName = "zip";
    OutputStream out = new ByteArrayOutputStream();

    // Act
    ArchiveOutputStream result = ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);

    // Assert
    assertNotNull(result);
    assertEquals(ZipArchiveOutputStream.class, result.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_nullNameValidStream() {
    // Arrange
    String archiverName = null;
    OutputStream out = new ByteArrayOutputStream();

    // Act
    ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_validNameNullStream() {
    // Arrange
    String archiverName = "zip";
    OutputStream out = null;

    // Act
    ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
  }

  @Test(expected = ArchiveException.class)
  public void testCreateArchiveOutputStream_invalidNameValidStream() {
    // Arrange
    String archiverName = "invalid";
    OutputStream out = new ByteArrayOutputStream();

    // Act
    ArchiveStreamFactory.createArchiveOutputStream(archiverName, out);
  }

  @Test
  public void testCreateArchiveInputStream_validStream() {
    // Arrange
    InputStream in = new ByteArrayInputStream(new byte[10]);

    // Act
    ArchiveInputStream result = ArchiveStreamFactory.createArchiveInputStream(in);

    // Assert
    assertNotNull(result);
    assertEquals(ZipArchiveInputStream.class, result.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_nullStream() {
    // Arrange
    InputStream in = null;

    // Act
    ArchiveStreamFactory.createArchiveInputStream(in);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_unsupportedMarkStream() {
    // Arrange
    InputStream in = new FileInputStream(file);

    // Act
    ArchiveStreamFactory.createArchiveInputStream(in);
  }

  @Test
  public void testCreateArchiveInputStream_dumpSignature() {
    // Arrange
    InputStream in = new ByteArrayInputStream(new byte[]{68, 85, 77, 80});

    // Act
    ArchiveInputStream result = ArchiveStreamFactory.createArchiveInputStream(in);

    // Assert
    assertNotNull(result);
    assertEquals(DumpArchiveInputStream.class, result.getClass());
  }

  @Test
  public void testCreateArchiveInputStream_tarSignature() {
    // Arrange
    InputStream in = new ByteArrayInputStream(new byte[]{0x75, 0x73, 0x74, 0x61, 0x72, 0x20, 0x20, 0x20});

    // Act
    ArchiveInputStream result = ArchiveStreamFactory.createArchiveInputStream(in);

    // Assert
    assertNotNull(result);
    assertEquals(TarArchiveInputStream.class, result.getClass());
  }

  @Test
  public void testCreateArchiveInputStream_invalidSignatures() {
    // Arrange
    InputStream in = new ByteArrayInputStream(new byte[]{0x00, 0x00, 0x00, 0x00});

    // Act
    try {
      ArchiveStreamFactory.createArchiveInputStream(in);
      fail("Expected ArchiveException to be thrown");
    } catch (ArchiveException e) {
      // Assert
      assertEquals("No Archiver found for the stream signature", e.getMessage());
    }
  }

}