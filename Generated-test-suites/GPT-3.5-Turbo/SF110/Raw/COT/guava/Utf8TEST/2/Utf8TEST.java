
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
  
  @Test
  public void testEncodedLength_withEmptyString_returnsZero() {
    int result = Utf8.encodedLength("");
    assertEquals(0, result);
  }
  
  @Test
  public void testEncodedLength_withAsciiCharacters_returnsCorrectLength() {
    int result = Utf8.encodedLength("Hello");
    assertEquals(5, result);
  }
  
  @Test
  public void testEncodedLength_withNonAsciiCharacters_returnsCorrectLength() {
    int result = Utf8.encodedLength("你好");
    assertEquals(6, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testEncodedLength_withLongUtf8Length_throwsIllegalArgumentException() {
    Utf8.encodedLength("abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789");
  }
  
  @Test
  public void testIsWellFormed_withWellFormedBytes_returnsTrue() {
    byte[] bytes = "Hello".getBytes();
    boolean result = Utf8.isWellFormed(bytes);
    assertTrue(result);
  }
  
  @Test
  public void testIsWellFormed_withMalformedBytes_returnsFalse() {
    byte[] bytes = { -127 };
    boolean result = Utf8.isWellFormed(bytes);
    assertFalse(result);
  }
  
  @Test
  public void testIsWellFormed_withWellFormedBytesAndOffset_returnsTrue() {
    byte[] bytes = "Hello".getBytes();
    boolean result = Utf8.isWellFormed(bytes, 1, 4);
    assertTrue(result);
  }
  
  @Test
  public void testIsWellFormed_withMalformedBytesAndOffset_returnsFalse() {
    byte[] bytes = { 72, -127, 101, 108, 108, 111 };
    boolean result = Utf8.isWellFormed(bytes, 1, 5);
    assertFalse(result);
  }

  @Test
  public void testIsWellFormedSlowPath_withWellFormedBytes_returnsTrue() {
    byte[] bytes = { -62, -128 };
    boolean result = Utf8.isWellFormedSlowPath(bytes, 0, 2);
    assertTrue(result);
  }
  
  @Test
  public void testIsWellFormedSlowPath_withMalformedBytes_returnsFalse() {
    byte[] bytes = { -127 };
    boolean result = Utf8.isWellFormedSlowPath(bytes, 0, 1);
    assertFalse(result);
  }
  
  @Test
  public void testIsWellFormedSlowPath_withMalformedBytesAndOffset_returnsFalse() {
    byte[] bytes = { 72, -127, 101, 108, 108, 111 };
    boolean result = Utf8.isWellFormedSlowPath(bytes, 1, 6);
    assertFalse(result);
  }
}

