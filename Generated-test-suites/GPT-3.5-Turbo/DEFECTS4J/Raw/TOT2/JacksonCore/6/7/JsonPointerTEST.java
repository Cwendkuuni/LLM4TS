import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

  @Test
  public void testEmptyConstructor() {
    JsonPointer jsonPointer = new JsonPointer();
    assertEquals("", jsonPointer.toString());
  }
  
  @Test
  public void testConstructorWithSegments() {
    JsonPointer jsonPointer = new JsonPointer("abc", "def", null);
    assertEquals("abc", jsonPointer.toString());
  }
  
  @Test
  public void testCompileWithValidInput() {
    JsonPointer jsonPointer = JsonPointer.compile("/test");
    assertEquals("/test", jsonPointer.toString());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCompileWithInvalidInput() {
    JsonPointer jsonPointer = JsonPointer.compile("test");
  }
  
  @Test
  public void testMatches() {
    JsonPointer jsonPointer = JsonPointer.compile("/");
    assertTrue(jsonPointer.matches());
  }
  
  @Test
  public void testGetMatchingProperty() {
    JsonPointer jsonPointer = JsonPointer.compile("/test");
    assertEquals("test", jsonPointer.getMatchingProperty());
  }

  @Test
  public void testGetMatchingIndex() {
    JsonPointer jsonPointer = JsonPointer.compile("/0");
    assertEquals(0, jsonPointer.getMatchingIndex());
  }

  @Test
  public void testMayMatchProperty() {
    JsonPointer jsonPointer = JsonPointer.compile("/test");
    assertTrue(jsonPointer.mayMatchProperty());
  }
  
  @Test
  public void testMayMatchElement() {
    JsonPointer jsonPointer = JsonPointer.compile("/0");
    assertTrue(jsonPointer.mayMatchElement());
  }

  @Test
  public void testMatchProperty() {
    JsonPointer jsonPointer = JsonPointer.compile("/test");
    JsonPointer nextPointer = jsonPointer.matchProperty("test");
    assertNull(nextPointer);
  }
  
  @Test
  public void testMatchElement() {
    JsonPointer jsonPointer = JsonPointer.compile("/0");
    JsonPointer nextPointer = jsonPointer.matchElement(1);
    assertNull(nextPointer);
  }
  
  @Test
  public void testTail() {
    JsonPointer jsonPointer = JsonPointer.compile("/test");
    JsonPointer nextPointer = jsonPointer.tail();
    assertNull(nextPointer);
  }

}