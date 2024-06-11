import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

  private static final ArchiveEntry TEST_ENTRY = new ArchiveEntry() {
    @Override
    public int getMethod() {
      return 0;
    }

    @Override
    public String getName() {
      return "test-entry";
    }

    @Override
    public long getSize() {
      return 100;
    }

    @Override
    public long getTime() {
      return 0;
    }

    @Override
    public boolean isDirectory() {
      return false;
    }
  };

  @Test
  public void testToString() {
    String expected = "-    100 test-entry";
    assertEquals(expected, ArchiveUtils.toString(TEST_ENTRY));
  }

  @Test
  public void testMatchAsciiBuffer() {
    String expected = "hello";
    byte[] buffer = expected.getBytes();
    assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
  }

  @Test
  public void testMatchAsciiBuffer_OffsetLength() {
    String expected = "hello";
    byte[] buffer = ("world" + expected + "2020").getBytes();
    int offset = 5;
    int length = expected.length();
    assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length));
  }

  @Test
  public void testToAsciiBytes() {
    String inputString = "test string";
    byte[] expected = inputString.getBytes();
    assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
  }

  @Test
  public void testToAsciiString() {
    byte[] inputBytes = "test string".getBytes();
    String expected = new String(inputBytes);
    assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
  }

  @Test
  public void testToAsciiString_OffsetLength() {
    byte[] inputBytes = "hello world 2020".getBytes();
    int offset = 6;
    int length = 5;
    String expected = new String(inputBytes, offset, length);
    assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
  }

  @Test
  public void testIsEqual() {
    byte[] buffer1 = "hello".getBytes();
    byte[] buffer2 = "hello".getBytes();
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
  }

  @Test
  public void testIsEqual_OffsetLength() {
    byte[] buffer1 = "hello world 2020".getBytes();
    int offset1 = 6;
    int length1 = 5;
    byte[] buffer2 = "world".getBytes();
    int offset2 = 0;
    int length2 = buffer2.length;
    assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
  }

  @Test
  public void testIsEqual_IgnoreTrailingNulls() {
    byte[] buffer1 = "hello\0\0".getBytes();
    byte[] buffer2 = "hello".getBytes();
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
  }

  @Test
  public void testIsEqualWithNull() {
    byte[] buffer1 = "hello\0\0".getBytes();
    int offset1 = 0;
    int length1 = buffer1.length;
    byte[] buffer2 = "hello".getBytes();
    int offset2 = 0;
    int length2 = buffer2.length;
    assertTrue(ArchiveUtils.isEqualWithNull(buffer1, offset1, length1, buffer2, offset2, length2));
  }

  @Test
  public void testIsArrayZero() {
    byte[] a = new byte[5];
    int size = 5;
    assertTrue(ArchiveUtils.isArrayZero(a, size));
  }

  @Test
  public void testSanitize() {
    String input = "test \0 string";
    String expected = "test ? string";
    assertEquals(expected, ArchiveUtils.sanitize(input));
  }

  @Test
  public void testSanitize_UnicodeBlockSpecials() {
    String input = "test \2 string";
    String expected = "test ? string";
    assertEquals(expected, ArchiveUtils.sanitize(input));
  }

  @Test
  public void testSanitize_UnicodeBlockNull() {
    String input = "test \0 string";
    String expected = "test ? string";
    assertEquals(expected, ArchiveUtils.sanitize(input));
  }

  @Test
  public void testSanitize_EmptyString() {
    String input = "";
    String expected = "";
    assertEquals(expected, ArchiveUtils.sanitize(input));
  }

}