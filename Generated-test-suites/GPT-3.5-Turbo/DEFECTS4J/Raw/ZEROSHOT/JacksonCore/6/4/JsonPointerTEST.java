import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

  @Test
  public void testEmptyConstructor() {
    JsonPointer jsonPointer = new JsonPointer();
    assertNotNull(jsonPointer);
    assertEquals("", jsonPointer._asString);
    assertNull(jsonPointer._nextSegment);
    assertEquals("", jsonPointer._matchingPropertyName);
    assertEquals(-1, jsonPointer._matchingElementIndex);
  }

  @Test
  public void testConstructorWithParameters() {
    String fullString = "fullString";
    String segment = "segment";
    JsonPointer next = new JsonPointer();
    JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
    assertNotNull(jsonPointer);
    assertEquals(fullString, jsonPointer._asString);
    assertEquals(next, jsonPointer._nextSegment);
    assertEquals(segment, jsonPointer._matchingPropertyName);
    assertEquals(-1, jsonPointer._matchingElementIndex);
  }

  @Test
  public void testCompileWithValidInput() {
    String input = "/validInput";
    JsonPointer jsonPointer = JsonPointer.compile(input);
    assertNotNull(jsonPointer);
    assertEquals(input, jsonPointer._asString);
    assertNull(jsonPointer._nextSegment);
    assertEquals("validInput", jsonPointer._matchingPropertyName);
    assertEquals(-1, jsonPointer._matchingElementIndex);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompileWithInvalidInput() {
    String input = "invalidInput";
    JsonPointer.compile(input);
  }

  @Test
  public void testValueOf() {
    String input = "/validInput";
    JsonPointer jsonPointer = JsonPointer.valueOf(input);
    assertNotNull(jsonPointer);
    assertEquals(input, jsonPointer._asString);
    assertNull(jsonPointer._nextSegment);
    assertEquals("validInput", jsonPointer._matchingPropertyName);
    assertEquals(-1, jsonPointer._matchingElementIndex);
  }

  @Test
  public void testMatches() {
    JsonPointer jsonPointer = new JsonPointer();
    assertTrue(jsonPointer.matches());
  }

  @Test
  public void testGetMatchingProperty() {
    JsonPointer jsonPointer = new JsonPointer();
    assertEquals("", jsonPointer.getMatchingProperty());
  }

  @Test
  public void testGetMatchingIndex() {
    JsonPointer jsonPointer = new JsonPointer();
    assertEquals(-1, jsonPointer.getMatchingIndex());
  }

  @Test
  public void testMayMatchProperty() {
    JsonPointer jsonPointer = new JsonPointer();
    assertTrue(jsonPointer.mayMatchProperty());
  }

  @Test
  public void testMayMatchElement() {
    JsonPointer jsonPointer = new JsonPointer();
    assertFalse(jsonPointer.mayMatchElement());
  }

  @Test
  public void testMatchPropertyWhenNextSegmentIsNull() {
    JsonPointer jsonPointer = new JsonPointer();
    assertNull(jsonPointer.matchProperty("name"));
  }

  @Test
  public void testMatchPropertyWhenMatchingPropertyNameMatches() {
    String segment = "name";
    JsonPointer next = new JsonPointer();
    JsonPointer jsonPointer = new JsonPointer("", segment, next);
    JsonPointer result = jsonPointer.matchProperty(segment);
    assertEquals(next, result);
  }

  @Test
  public void testMatchPropertyWhenMatchingPropertyNameDoesNotMatch() {
    String segment = "name";
    JsonPointer next = new JsonPointer();
    JsonPointer jsonPointer = new JsonPointer("", segment, next);
    JsonPointer result = jsonPointer.matchProperty("otherName");
    assertNull(result);
  }

  @Test
  public void testMatchElementWhenIndexDoesNotMatch() {
    int index = 1;
    JsonPointer next = new JsonPointer();
    JsonPointer jsonPointer = new JsonPointer("", "name", next);
    JsonPointer result = jsonPointer.matchElement(index);
    assertNull(result);
  }

  @Test
  public void testMatchElementWhenIndexMatches() {
    int index = 1;
    JsonPointer next = new JsonPointer();
    JsonPointer jsonPointer = new JsonPointer("", String.valueOf(index), next);
    JsonPointer result = jsonPointer.matchElement(index);
    assertEquals(next, result);
  }

  @Test
  public void testTail() {
    JsonPointer next = new JsonPointer();
    JsonPointer jsonPointer = new JsonPointer("", "name", next);
    JsonPointer result = jsonPointer.tail();
    assertEquals(next, result);
  }

  @Test
  public void testToString() {
    JsonPointer jsonPointer = new JsonPointer();
    assertEquals("", jsonPointer.toString());
  }

  @Test
  public void testHashCode() {
    JsonPointer jsonPointer = new JsonPointer();
    assertEquals(jsonPointer._asString.hashCode(), jsonPointer.hashCode());
  }

  @Test
  public void testEqualsWhenObjectIsSame() {
    JsonPointer jsonPointer = new JsonPointer();
    assertTrue(jsonPointer.equals(jsonPointer));
  }

  @Test
  public void testEqualsWhenObjectIsNull() {
    JsonPointer jsonPointer = new JsonPointer();
    assertFalse(jsonPointer.equals(null));
  }

  @Test
  public void testEqualsWhenObjectIsNotInstanceofJsonPointer() {
    JsonPointer jsonPointer = new JsonPointer();
    assertFalse(jsonPointer.equals(new Object()));
  }

  @Test
  public void testEqualsWhenAsStringIsNotEqual() {
    JsonPointer jsonPointer1 = new JsonPointer();
    JsonPointer jsonPointer2 = new JsonPointer("asString", "", null);
    assertFalse(jsonPointer1.equals(jsonPointer2));
  }

  @Test
  public void testEqualsWhenObjectsAreEqual() {
    JsonPointer jsonPointer1 = new JsonPointer();
    JsonPointer jsonPointer2 = new JsonPointer();
    assertTrue(jsonPointer1.equals(jsonPointer2));
  }

  @Test
  public void test_parseIndexWhenStringLengthIsZero() {
    int result = JsonPointer._parseIndex("");
    assertEquals(-1, result);
  }

  @Test
  public void test_parseIndexWhenStringLengthIsZero() {
    int result = JsonPointer._parseIndex("12345678901");
    assertEquals(-1, result);
  }

  @Test
  public void test_parseIndexWhenStringLengthIsTenAndLessThanIntegerMAXValue() {
    int result = JsonPointer._parseIndex("1234567890");
    assertTrue(result > 0);
  }

  @Test
  public void test_parseIndexWhenStringLengthIsTenAndGreaterThanIntegerMAXValue() {
    int result = JsonPointer._parseIndex("1111111111");
    assertEquals(-1, result);
  }
  
  @Test
  public void test_parseTailWhenEndIs1() {
    String input = "/segment";
    JsonPointer jsonPointer = JsonPointer._parseTail(input);
    assertNotNull(jsonPointer);
    assertEquals(input, jsonPointer._asString);
    assertNull(jsonPointer._nextSegment);
    assertEquals("segment", jsonPointer._matchingPropertyName);
    assertEquals(-1, jsonPointer._matchingElementIndex);
  }

  @Test
  public void test_parseTailWhenQuotedCharactersArePresent() {
    String input = "/~0~1segment";
    JsonPointer jsonPointer = JsonPointer._parseTail(input);
    assertNotNull(jsonPointer);
    assertEquals(input, jsonPointer._asString);
    assertNull(jsonPointer._nextSegment);
    assertEquals("~0/segment", jsonPointer._matchingPropertyName);
    assertEquals(-1, jsonPointer._matchingElementIndex);
  }

  @Test
  public void test_parseQuotedTail() {
    String input = "/~0~1segment";
    int i = 1;
    JsonPointer jsonPointer = JsonPointer._parseQuotedTail(input, i);
    assertNotNull(jsonPointer);
    assertEquals(input, jsonPointer._asString);
    assertNull(jsonPointer._nextSegment);
    assertEquals("~0/segment", jsonPointer._matchingPropertyName);
    assertEquals(-1, jsonPointer._matchingElementIndex);
  }

  @Test
  public void test_appendEscapeWhenCIs0() {
    StringBuilder sb = new StringBuilder();
    JsonPointer._appendEscape(sb, '0');
    assertEquals("~0", sb.toString());
  }

  @Test
  public void test_appendEscapeWhenCIs1() {
    StringBuilder sb = new StringBuilder();
    JsonPointer._appendEscape(sb, '1');
    assertEquals("~1", sb.toString());
  }

  @Test
  public void test_appendEscapeWhenCIsOtherThan0And1() {
    StringBuilder sb = new StringBuilder();
    JsonPointer._appendEscape(sb, 'c');
    assertEquals("~c", sb.toString());
  }

}