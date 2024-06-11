
import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {
  
  @Test
  public void testEncodedLength_basic() {
    CharSequence sequence = "Hello World";
    int expected = 11;
    int actual = Utf8.encodedLength(sequence);
    assertEquals(expected, actual);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testEncodedLength_overflow() {
    CharSequence sequence = "A" + new String(new char[Integer.MAX_VALUE]);
    Utf8.encodedLength(sequence);
  }
  
  @Test
  public void testIsWellFormed_true() {
    byte[] bytes = new byte[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
    assertTrue(Utf8.isWellFormed(bytes));
  }
  
  @Test
  public void testIsWellFormed_false() {
    byte[] bytes = new byte[] { 72, 101, -1, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
    assertFalse(Utf8.isWellFormed(bytes));
  }
}
