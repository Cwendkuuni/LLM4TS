import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

  private ArArchiveInputStream inputStream;

  @Before
  public void setUp() {
    InputStream testInput = new InputStream();
    inputStream = new ArArchiveInputStream(testInput);
  }

  @After
  public void tearDown() {
    inputStream.close();
  }

  @Test
  public void testGetNextArEntryValidEntry() {
    try {
      ArArchiveEntry entry = inputStream.getNextArEntry();
      assertNotNull(entry);
      assertEquals("TestEntry", entry.getName());
      assertEquals(10, entry.getSize());
    } catch (IOException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testGetNextArEntryNoMoreEntries() {
    try {
      ArArchiveEntry entry = inputStream.getNextArEntry();
      assertNull(entry);
    } catch (IOException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testGetNextEntry() {
    try {
      ArchiveEntry entry = inputStream.getNextEntry();
      assertNotNull(entry);
      assertEquals("TestEntry", entry.getName());
      assertEquals(10, entry.getSize());
    } catch (IOException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testClose() {
    try {
      inputStream.close();
      input.close();
      assertTrue(inputStream.isClosed());
    } catch (IOException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testRead() {
    try {
      int byteRead = inputStream.read();
      assertEquals(65, byteRead);
    } catch (IOException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testReadArray() {
    try {
      byte[] buffer = new byte[10];
      int bytesRead = inputStream.read(buffer);
      assertEquals(10, bytesRead);
    } catch (IOException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testReadArrayOffsetLength() {
    try {
      byte[] buffer = new byte[10];
      int bytesRead = inputStream.read(buffer, 0, 5);
      assertEquals(5, bytesRead);
    } catch (IOException e) {
      fail("Should not throw an exception");
    }
  }

  @Test
  public void testMatchesValidSignature() {
    byte[] signature = {(byte)0x21, (byte)0x3c, (byte)0x61, (byte)0x72, (byte)0x63, (byte)0x68, (byte)0x3e, (byte)0x0a};
    boolean result = ArArchiveInputStream.matches(signature, 8);
    assertTrue(result);
  }

  @Test
  public void testMatchesInvalidSignature() {
    byte[] signature = {(byte)0x21, (byte)0x3c, (byte)0x61, (byte)0x72, (byte)0x63, (byte)0x68, (byte)0x3e, (byte)0x0b};
    boolean result = ArArchiveInputStream.matches(signature, 8);
    assertFalse(result);
  }

}