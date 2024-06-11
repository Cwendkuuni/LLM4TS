import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

  @Test
  public void testGetNextArEntry() throws IOException {
    byte[] header = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    byte[] trailer = {0x60, 0x0a};

    byte[] entryData = new byte[16 + 12 + 6 + 6 + 8 + 10 + 8];
    System.arraycopy(header, 0, entryData, 0, header.length);
    System.arraycopy(trailer, 0, entryData, entryData.length-trailer.length, trailer.length);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(entryData);
    ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

    ArArchiveEntry entry = archiveInputStream.getNextArEntry();

    assertNotNull(entry);
    assertEquals("", entry.getName());
    assertEquals(0, entry.getSize());

    archiveInputStream.close();
  }

  @Test
  public void testGetNextEntry() throws IOException {
    byte[] header = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    byte[] trailer = {0x60, 0x0a};

    byte[] entryData = new byte[16 + 12 + 6 + 6 + 8 + 10 + 8];
    System.arraycopy(header, 0, entryData, 0, header.length);
    System.arraycopy(trailer, 0, entryData, entryData.length-trailer.length, trailer.length);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(entryData);
    ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

    ArchiveEntry entry = archiveInputStream.getNextEntry();

    assertNotNull(entry);
    assertEquals("", entry.getName());
    assertEquals(0, entry.getSize());

    archiveInputStream.close();
  }

  @Test
  public void testClose() throws IOException {
    byte[] header = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    byte[] trailer = {0x60, 0x0a};

    byte[] entryData = new byte[16 + 12 + 6 + 6 + 8 + 10 + 8];
    System.arraycopy(header, 0, entryData, 0, header.length);
    System.arraycopy(trailer, 0, entryData, entryData.length-trailer.length, trailer.length);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(entryData);
    ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

    archiveInputStream.close();

    assertTrue(archiveInputStream.isClosed());
  }

  @Test
  public void testRead() throws IOException {
    byte[] header = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    byte[] trailer = {0x60, 0x0a};

    byte[] entryData = new byte[16 + 12 + 6 + 6 + 8 + 10 + 8];
    System.arraycopy(header, 0, entryData, 0, header.length);
    System.arraycopy(trailer, 0, entryData, entryData.length-trailer.length, trailer.length);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(entryData);
    ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

    int readByte = archiveInputStream.read();

    assertEquals(header[0], readByte);

    archiveInputStream.close();
  }

  @Test
  public void testReadWithByteArray() throws IOException {
    byte[] header = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    byte[] trailer = {0x60, 0x0a};

    byte[] entryData = new byte[16 + 12 + 6 + 6 + 8 + 10 + 8];
    System.arraycopy(header, 0, entryData, 0, header.length);
    System.arraycopy(trailer, 0, entryData, entryData.length-trailer.length, trailer.length);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(entryData);
    ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

    byte[] buffer = new byte[entryData.length];
    int bytesRead = archiveInputStream.read(buffer);

    assertEquals(entryData.length, bytesRead);

    archiveInputStream.close();
  }

  @Test
  public void testReadWithOffsetAndLength() throws IOException {
    byte[] header = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    byte[] trailer = {0x60, 0x0a};

    byte[] entryData = new byte[16 + 12 + 6 + 6 + 8 + 10 + 8];
    System.arraycopy(header, 0, entryData, 0, header.length);
    System.arraycopy(trailer, 0, entryData, entryData.length-trailer.length, trailer.length);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(entryData);
    ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

    byte[] buffer = new byte[header.length];
    int bytesRead = archiveInputStream.read(buffer, 0, header.length);

    assertEquals(header.length, bytesRead);

    archiveInputStream.close();
  }

  @Test
  public void testMatches() {
    byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    int length = signature.length;

    boolean result = ArArchiveInputStream.matches(signature, length);

    assertTrue(result);
  }

}