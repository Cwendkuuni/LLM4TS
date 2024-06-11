import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
  
  @Test
  public void testEmptyConstructor() {
    JsonPointer pointer = new JsonPointer();
    assertTrue(pointer.matches());
    assertEquals("", pointer.getMatchingProperty());
    assertEquals(-1, pointer.getMatchingIndex());
    assertFalse(pointer.mayMatchProperty());
    assertFalse(pointer.mayMatchElement());
    assertNull(pointer.tail());
  }
  
  @Test
  public void testCompileValidInput() {
    JsonPointer pointer = JsonPointer.compile("/test");
    assertFalse(pointer.matches());
    assertEquals("test", pointer.getMatchingProperty());
    assertEquals(-1, pointer.getMatchingIndex());
    assertTrue(pointer.mayMatchProperty());
    assertFalse(pointer.mayMatchElement());
    assertNull(pointer.tail());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCompileInvalidInput() {
    JsonPointer.compile("test");
  }
  
  // Add more test cases to cover edge cases and exceptions

}