import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 return null;
 }
 }

 @Test
 public void testEmptyConstructor() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testNonEmptyConstructor() {
 String input = "/foo/0";
 JsonPointer jsonPointer = new JsonPointer(input, "foo", null);
 assertFalse(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "bar";
 JsonPointer jsonPointer = createJsonPointer(input);
 fail("Expected IllegalArgumentException but got: " + jsonPointer);
 }

 @Test
 public void testCompile() {
 String input = "/foo/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertEquals(input, jsonPointer.toString());
 }

 @Test
 public void testValueOf() {
 String input = "/foo/0";
 JsonPointer jsonPointer = JsonPointer.valueOf(input);
 assertEquals(input, jsonPointer.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testFromSegmentNullSegments() {
 JsonPointer jsonPointer = JsonPointer.fromSegment(null);
 fail("Expected NullPointerException but got: " + jsonPointer);
 }

 @Test
 public void testFromSegment() {
 String[] segments = new String[]{"foo", "1"};
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 assertEquals(segments[0] + "/" + segments[1], jsonPointer.toString());
 }

 @Test
 public void testMatches() {
 JsonPointer jsonPointer = createJsonPointer("/foo/1");
 assertTrue(jsonPointer.matches());
 jsonPointer = createJsonPointer("/foo/2");
 assertFalse(jsonPointer.matches());
 jsonPointer = createJsonPointer("/foo");
 assertTrue(jsonPointer.matches());
 }

 @Test
 public void testGetMatchingProperty() {
 JsonPointer jsonPointer = createJsonPointer("/foo/1");
 assertEquals("foo", jsonPointer.getMatchingProperty());
 jsonPointer = createJsonPointer("/foo/2");
 assertNull(jsonPointer.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndex() {
 JsonPointer jsonPointer = createJsonPointer("/foo/1");
 assertEquals(1, jsonPointer.getMatchingIndex());
 jsonPointer = createJsonPointer("/foo/2");
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testMayMatchProperty() {
 JsonPointer jsonPointer = createJsonPointer("/foo/1");
 assertTrue(jsonPointer.mayMatchProperty());
 jsonPointer = createJsonPointer("/foo");
 assertTrue(jsonPointer.mayMatchProperty());
 jsonPointer = createJsonPointer("/");
 assertFalse(jsonPointer.mayMatchProperty());
 }

 @Test
 public void testMayMatchElement() {
 JsonPointer jsonPointer = createJsonPointer("/foo/1");
 assertTrue(jsonPointer.mayMatchElement());
 jsonPointer = createJsonPointer("/foo");
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer = createJsonPointer("/foo/0");
 JsonPointer matchingProperty = jsonPointer.matchProperty("foo");
 assertEquals("/foo", matchingProperty.toString());
 matchingProperty = jsonPointer.matchProperty("bar");
 assertNull(matchingProperty);
 }

 @Test
 public void testMatchElement() {
 JsonPointer jsonPointer = createJsonPointer("/foo/1");
 JsonPointer matchingElement = jsonPointer.matchElement(1);
 assertEquals("/foo", matchingElement.toString());
 matchingElement = jsonPointer.matchElement(2);
 assertNull(matchingElement);
 }

 @Test
 public void testTail() {
 JsonPointer jsonPointer = createJsonPointer("/foo/1");
 JsonPointer tail = jsonPointer.tail();
 assertEquals("/foo", jsonPointer.toString());
 assertEquals("/1", tail.toString());
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer = new JsonPointer();
 assertEquals("", jsonPointer.toString());
 jsonPointer = createJsonPointer("/foo/1");
 assertEquals("/foo/1", jsonPointer.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer jsonPointer1 = createJsonPointer("/foo/1");
 JsonPointer jsonPointer2 = createJsonPointer("/foo/1");
 assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 jsonPointer1 = createJsonPointer("/foo/2");
 jsonPointer2 = createJsonPointer("/foo/3");
 assertNotEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 JsonPointer jsonPointer1 = createJsonPointer("/foo/1");
 JsonPointer jsonPointer2 = createJsonPointer("/foo/1");
 assertTrue(jsonPointer1.equals(jsonPointer2));
 jsonPointer1 = createJsonPointer("/foo/2");
 jsonPointer2 = createJsonPointer("/foo/3");
 assertFalse(jsonPointer1.equals(jsonPointer2));
 }

 @Test
 public void testParseIndex() {
 assertEquals(0, JsonPointer._parseIndex(""));
 assertEquals(-1, JsonPointer._parseIndex("x"));
 assertEquals(1, JsonPointer._parseIndex("1"));
 assertEquals(19, JsonPointer._parseIndex("19"));
 assertEquals(257, JsonPointer._parseIndex("257"));
 assertEquals(-1, JsonPointer._parseIndex("258"));
 }

 @Test
 public void testParseTail() {
 String input = "/foo/1/bar~1/baz~2/quux~3";
 JsonPointer jsonPointer = JsonPointer._parseTail(input);
 assertEquals("/bar/1/baz/2/quux", jsonPointer.toString());
 }
}