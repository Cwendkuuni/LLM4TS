import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class ZipArchiveInputStreamTest {
  
  @Test
  public void testGetNextZipEntry() throws IOException {
    InputStream inputStream = new FileInputStream("test.zip");
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

    ArchiveEntry entry = zipInputStream.getNextZipEntry();

    assertEquals("file1.txt", entry.getName());
    assertEquals(ArchiveEntry.STORED, entry.getMethod());
    assertEquals(10, entry.getSize());
    assertEquals(10, entry.getCompressedSize());
    assertEquals(123456, entry.getCrc());

    zipInputStream.close();
  }

  @Test
  public void testRead() throws IOException {
    InputStream inputStream = new FileInputStream("test.zip");
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

    zipInputStream.getNextZipEntry();

    byte[] buffer = new byte[100];
    int bytesRead = zipInputStream.read(buffer, 0, 100);

    assertEquals(10, bytesRead);
    assertEquals("This is a test", new String(buffer));

    zipInputStream.close();
  }

  @Test
  public void testClose() throws IOException {
    InputStream inputStream = new FileInputStream("test.zip");
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

    zipInputStream.close();

    try {
      zipInputStream.getNextZipEntry();
      fail("Expected IOException");
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testSkip() throws IOException {
    InputStream inputStream = new FileInputStream("test.zip");
    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

    zipInputStream.getNextZipEntry();
    zipInputStream.skip(5);

    byte[] buffer = new byte[100];
    int bytesRead = zipInputStream.read(buffer, 0, 100);

    assertEquals(5, bytesRead);
    assertEquals("is a test", new String(buffer));

    zipInputStream.close();
  }

  @Test
  public void testMatches() {
    byte[] signature1 = new byte[] {0x50, 0x4B, 0x03, 0x04};
    byte[] signature2 = new byte[] {0x50, 0x4B, 0x05, 0x06};

    assertTrue(ZipArchiveInputStream.matches(signature1, 4));
    assertFalse(ZipArchiveInputStream.matches(signature2, 4));
  }
}