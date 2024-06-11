import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

  @Test
  public void testToString() {
    ArchiveEntry entry1 = new ArchiveEntry("main.c", false, 2000);
    assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));

    ArchiveEntry entry2 = new ArchiveEntry("testfiles", true, 100);
    assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
  }

  @Test
  public void testMatchAsciiBuffer() {
    assertTrue(ArchiveUtils.matchAsciiBuffer("hello", "hello".getBytes()));
    assertTrue(ArchiveUtils.matchAsciiBuffer("hello", "hello".getBytes(), 1, 3));
    assertFalse(ArchiveUtils.matchAsciiBuffer("world", "hello".getBytes()));
  }

  @Test
  public void testToAsciiBytes() {
    assertArrayEquals("hello".getBytes(), ArchiveUtils.toAsciiBytes("hello"));
  }

  @Test
  public void testToAsciiString() {
    assertEquals("hello", ArchiveUtils.toAsciiString("hello".getBytes()));
    assertEquals("ello", ArchiveUtils.toAsciiString("hello".getBytes(), 1, 4));
  }

  @Test
  public void testIsEqual() {
    assertTrue(ArchiveUtils.isEqual("hello".getBytes(), "hello".getBytes()));
    assertFalse(ArchiveUtils.isEqual("world".getBytes(), "hello".getBytes()));
  }

  @Test
  public void testIsEqualWithNull() {
    assertTrue(ArchiveUtils.isEqualWithNull("hello".getBytes(), "hello".getBytes()));
    assertFalse(ArchiveUtils.isEqualWithNull("world".getBytes(), "hello".getBytes()));
  }

  @Test
  public void testIsArrayZero() {
    byte[] array1 = new byte[] { 0, 0, 0 };
    assertTrue(ArchiveUtils.isArrayZero(array1, 3));

    byte[] array2 = new byte[] { 0, 1, 0 };
    assertFalse(ArchiveUtils.isArrayZero(array2, 3));
  }

  @Test
  public void testSanitize() {
    assertEquals("hello", ArchiveUtils.sanitize("hello"));
    assertEquals("?ello", ArchiveUtils.sanitize("\u0000ello"));
  }
}