import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
  
  @Test
  public void testMatches() {
    JsonPointer pointer = new JsonPointer();
    assertTrue(pointer.matches());
  }
  
  @Test
  public void testGetMatchingProperty() {
    JsonPointer pointer = new JsonPointer();
    assertEquals("", pointer.getMatchingProperty());
  }
  
  @Test
  public void testGetMatchingIndex() {
    JsonPointer pointer = new JsonPointer();
    assertEquals(-1, pointer.getMatchingIndex());
  }
  
  @Test
  public void testMayMatchProperty() {
    JsonPointer pointer = new JsonPointer();
    assertTrue(pointer.mayMatchProperty());
  }
  
  @Test
  public void testMayMatchElement() {
    JsonPointer pointer = new JsonPointer();
    assertFalse(pointer.mayMatchElement());
  }
  
  @Test
  public void testMatchProperty() {
    JsonPointer pointer = new JsonPointer();
    assertNull(pointer.matchProperty("test"));
  }
  
  @Test
  public void testMatchElement() {
    JsonPointer pointer = new JsonPointer();
    assertNull(pointer.matchElement(0));
  }
  
  @Test
  public void testTail() {
    JsonPointer pointer = new JsonPointer();
    assertNull(pointer.tail());
  }
  
  @Test
  public void testCompile() {
    JsonPointer pointer = JsonPointer.compile("/test");
    assertNotNull(pointer);
  }
  
  @Test
  public void testValueOf() {
    JsonPointer pointer = JsonPointer.valueOf("/test");
    assertNotNull(pointer);
  }
  
  @Test
  public void testToString() {
    JsonPointer pointer = new JsonPointer();
    assertEquals("", pointer.toString());
  }
  
  @Test
  public void testHashCode() {
    JsonPointer pointer = new JsonPointer();
    assertNotNull(pointer.hashCode());
  }
  
  @Test
  public void testEquals() {
    JsonPointer pointer1 = new JsonPointer();
    JsonPointer pointer2 = new JsonPointer();
    assertTrue(pointer1.equals(pointer2));
  }
  
  @Test
  public void test_parseIndex() {
    String str = "123";
    int index = JsonPointer._parseIndex(str);
    assertEquals(123, index);
  }
  
  @Test
  public void test_parseIndexInvalid() {
    String str = "invalid";
    int index = JsonPointer._parseIndex(str);
    assertEquals(-1, index);
  }
  
  @Test
  public void test_parseTail() {
    String input = "/test";
    JsonPointer pointer = JsonPointer._parseTail(input);
    assertNotNull(pointer);
  }
  
  @Test
  public void test_parseQuotedTail() {
    String input = "~0test";
    int i = 2;
    JsonPointer pointer = JsonPointer._parseQuotedTail(input, i);
    assertNotNull(pointer);
  }
  
  @Test
  public void test_appendEscape() {
    StringBuilder sb = new StringBuilder();
    char c = '0';
    JsonPointer._appendEscape(sb, c);
    assertEquals("~0", sb.toString());
  }
}