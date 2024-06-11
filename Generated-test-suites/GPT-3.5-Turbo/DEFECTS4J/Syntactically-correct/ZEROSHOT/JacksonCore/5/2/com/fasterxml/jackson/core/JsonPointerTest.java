import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
  
  @Test
  public void testEmptyJsonPointerConstructor() {
    JsonPointer emptyJsonPointer = new JsonPointer();
    assertTrue(emptyJsonPointer.matches());
    assertEquals("", emptyJsonPointer.getMatchingProperty());
    assertEquals(-1, emptyJsonPointer.getMatchingIndex());
    assertNull(emptyJsonPointer.matchProperty("test"));
    assertNull(emptyJsonPointer.matchElement(0));
    assertNull(emptyJsonPointer.tail());
  }

  @Test
  public void testNonEmptyJsonPointerConstructor() {
    JsonPointer nonEmptyJsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertFalse(nonEmptyJsonPointer.matches());
    assertEquals("segment", nonEmptyJsonPointer.getMatchingProperty());
    assertEquals(-1, nonEmptyJsonPointer.getMatchingIndex());
    assertNull(nonEmptyJsonPointer.matchProperty("test"));
    assertNull(nonEmptyJsonPointer.matchElement(0));
    assertNotNull(nonEmptyJsonPointer.tail());
  }

  @Test
  public void testCompileValidInput() {
    JsonPointer jsonPointer = JsonPointer.compile("/test/segment");
    assertFalse(jsonPointer.matches());
    assertEquals("test", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertNull(jsonPointer.matchProperty("invalid"));
    assertNull(jsonPointer.matchElement(0));
    assertNotNull(jsonPointer.tail());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompileInvalidInput() {
    JsonPointer jsonPointer = JsonPointer.compile("invalid");
  }

  @Test
  public void testValueOf() {
    JsonPointer jsonPointer = JsonPointer.valueOf("/test/segment");
    assertFalse(jsonPointer.matches());
    assertEquals("test", jsonPointer.getMatchingProperty());
    assertEquals(-1, jsonPointer.getMatchingIndex());
    assertNull(jsonPointer.matchProperty("invalid"));
    assertNull(jsonPointer.matchElement(0));
    assertNotNull(jsonPointer.tail());
  }

  @Test
  public void testMatches() {
    JsonPointer jsonPointer = new JsonPointer();
    assertTrue(jsonPointer.matches());
  }

  @Test
  public void testGetMatchingProperty() {
    JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertEquals("segment", jsonPointer.getMatchingProperty());
  }

  @Test
  public void testGetMatchingIndex() {
    JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertEquals(-1, jsonPointer.getMatchingIndex());
  }

  @Test
  public void testMayMatchProperty() {
    JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertTrue(jsonPointer.mayMatchProperty());
  }

  @Test
  public void testMayMatchElement() {
    JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertFalse(jsonPointer.mayMatchElement());
  }

  @Test
  public void testMatchProperty() {
    JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertNull(jsonPointer.matchProperty("invalid"));
  }

  @Test
  public void testMatchElement() {
    JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertNull(jsonPointer.matchElement(0));
  }

  @Test
  public void testTail() {
    JsonPointer jsonPointer = new JsonPointer("fullString", "segment", new JsonPointer());
    assertNotNull(jsonPointer.tail());
  }
}