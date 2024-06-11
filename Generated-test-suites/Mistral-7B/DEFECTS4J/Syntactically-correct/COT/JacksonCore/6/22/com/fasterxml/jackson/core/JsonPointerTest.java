import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private static JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Failed to create JsonPointer: " + e.getMessage());
 }
 return null;
 }

 @Test
 public void testEmptyConstructor() {
 JsonPointer emptyPointer = new JsonPointer();
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testConstructorWithStringSegmentAndNext() {
 String fullString = "/foo/bar";
 String segment = "foo";
 JsonPointer nextPointer = new JsonPointer("/baz");
 JsonPointer pointer = new JsonPointer(fullString, segment, nextPointer);
 assertEquals(fullString, pointer.toString());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 }

 @Test
 public void testMatchesMethod() {
 JsonPointer pointer = createJsonPointer("/foo/bar");
 assertTrue(pointer.matches());
 pointer = createJsonPointer("/foo");
 assertFalse(pointer.matches());
 }

 @Test
 public void testGetMatchingPropertyMethod() {
 JsonPointer pointer = createJsonPointer("/foo/bar/baz");
 assertEquals("bar", pointer.getMatchingProperty());
 pointer = createJsonPointer("/foo");
 assertNull(pointer.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndexMethod() {
 JsonPointer pointer = createJsonPointer("/foo/0");
 assertEquals(0, pointer.getMatchingIndex());
 pointer = createJsonPointer("/foo/123");
 assertEquals(123, pointer.getMatchingIndex());
 pointer = createJsonPointer("/foo");
 assertEquals(-1, pointer.getMatchingIndex());
 }

 @Test
 public void testMayMatchPropertyMethod() {
 JsonPointer pointer = createJsonPointer("/foo/bar");
 assertTrue(pointer.mayMatchProperty());
 pointer = createJsonPointer("/foo");
 assertFalse(pointer.mayMatchProperty());
 }

 @Test
 public void testMayMatchElementMethod() {
 JsonPointer pointer = createJsonPointer("/foo/0");
 assertTrue(pointer.mayMatchElement());
 pointer = createJsonPointer("/foo");
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testMatchPropertyMethod() {
 JsonPointer pointer = createJsonPointer("/foo/bar");
 JsonPointer matchedPointer = pointer.matchProperty("bar");
 assertEquals("/foo", matchedPointer.toString());
 matchedPointer = pointer.matchProperty("baz");
 assertNull(matchedPointer);
 }

 @Test
 public void testMatchElementMethod() {
 JsonPointer pointer = createJsonPointer("/foo/0");
 JsonPointer matchedPointer = pointer.matchElement(0);
 assertEquals("/foo", matchedPointer.toString());
 matchedPointer = pointer.matchElement(1);
 assertNull(matchedPointer);
 }

 @Test
 public void testTailMethod() {
 JsonPointer pointer = createJsonPointer("/foo/bar");
 JsonPointer tailPointer = pointer.tail();
 assertEquals("/bar", tailPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithEmptyString() {
 JsonPointer.compile("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithNoSlash() {
 JsonPointer.compile("foo");
 }

 @Test
 public void testParseTailMethod() {
 String input = "/foo/bar/baz~1/qux/quux~2/quuz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar/baz~1/qux/quux~2", pointer.toString());
 assertTrue(pointer.mayMatchElement());
 assertEquals(1, pointer.getMatchingIndex());
 pointer = pointer.tail();
 assertEquals("/qux/quux~2", pointer.toString());
 assertEquals(2, pointer.getMatchingIndex());
 pointer = pointer.tail();
 assertNull(pointer);
 }

}