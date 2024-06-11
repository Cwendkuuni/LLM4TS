import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 // Basic test cases for constructor methods
 @Test
 public void testEmptyConstructor() {
 JsonPointer emptyPointer = new JsonPointer();
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertEquals(EMPTY, emptyPointer.tail());
 }

 @Test
 public void testFullConstructor() {
 String fullString = "/a/b/c";
 String segment = "b";
 JsonPointer nextPointer = new JsonPointer("/d");
 JsonPointer pointer = new JsonPointer(fullString, segment, nextPointer);
 assertEquals(fullString, pointer.toString());
 assertEquals(nextPointer, pointer.tail());
 assertEquals("b", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 }

 // Testing factory method compile
 @Test
 public void testCompileValidJsonPointer() throws IllegalArgumentException {
 String jsonPointerString = "/a/b/c";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 assertEquals(jsonPointerString, jsonPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidJsonPointer() throws IllegalArgumentException {
 String jsonPointerString = "ab";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileEmptyJsonPointer() throws IllegalArgumentException {
 String jsonPointerString = "";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 fail("IllegalArgumentException expected but not thrown");
 }

 // Testing factory method fromSegment
 /* TODO: Implement this test case once fromSegment method is implemented in the JsonPointer class */

 // Testing public API methods
 @Test
 public void testMatches() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c");
 JsonPointer pointer2 = new JsonPointer("/a/b");
 JsonPointer pointer3 = new JsonPointer("/a/b/c");
 assertTrue(pointer1.matches());
 assertFalse(pointer2.matches());
 assertTrue(pointer3.matches());
 }

 @Test
 public void testGetMatchingProperty() {
 JsonPointer pointer = new JsonPointer("/a/b/c");
 assertEquals("b", pointer.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndex() {
 JsonPointer pointer = new JsonPointer("/a/b/0");
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test
 public void testMayMatchProperty() {
 JsonPointer pointer = new JsonPointer("/a/b/c");
 assertTrue(pointer.mayMatchProperty());
 JsonPointer pointer2 = new JsonPointer("/a/b");
 assertFalse(pointer2.mayMatchProperty());
 }

 @Test
 public void testMayMatchElement() {
 JsonPointer pointer = new JsonPointer("/a/b/0");
 assertTrue(pointer.mayMatchElement());
 JsonPointer pointer2 = new JsonPointer("/a/b");
 assertFalse(pointer2.mayMatchElement());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c");
 JsonPointer pointer2 = new JsonPointer("/a/b");
 JsonPointer pointer3 = new JsonPointer("/a/b/c");
 JsonPointer matchingPropertyPointer = new JsonPointer("b");
 assertTrue(pointer1.matches());
 assertTrue(pointer1.mayMatchProperty());
 assertTrue(pointer1.matchProperty("b") == pointer3);
 assertNull(pointer2.matchProperty("b"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/a/b/0");
 JsonPointer pointer2 = new JsonPointer("/a/b");
 JsonPointer matchingElementPointer = new JsonPointer("/a/b/0");
 assertTrue(pointer1.matches());
 assertTrue(pointer1.mayMatchElement());
 assertTrue(pointer1.matchElement(0) == matchingElementPointer);
 assertNull(pointer2.matchElement(0));
 }

 // Testing internal methods
 @Test
 public void testParseIndex() {
 assertEquals(-1, JsonPointer._parseIndex(""));
 assertEquals(-1, JsonPointer._parseIndex("x"));
 assertEquals(-1, JsonPointer._parseIndex("10"));
 assertEquals(-1, JsonPointer._parseIndex("123456789"));
 assertEquals(-1, JsonPointer._parseIndex("1234567890"));
 assertEquals(0, JsonPointer._parseIndex("0"));
 assertEquals(1, JsonPointer._parseIndex("1"));
 assertEquals(2, JsonPointer._parseIndex("2"));
 assertEquals(9, JsonPointer._parseIndex("9"));
 }

 @Test
 public void testParseTail() {
 String jsonPointerString = "/a/b/c/d~1/e~2";
 JsonPointer expectedPointer = new JsonPointer("/a/b/c/d/e");
 JsonPointer actualPointer = JsonPointer._parseTail(jsonPointerString);
 assertEquals(expectedPointer.toString(), actualPointer.toString());
 }

 @Test
 public void testParseQuotedTail() {
 String jsonPointerString = "/a/b/c/d~1/e~2/";
 StringBuilder expectedSb = new StringBuilder("/a/b/c/d/e");
 _appendEscape(expectedSb, '~');
 _appendEscape(expectedSb, '1');
 _appendEscape(expectedSb, '/');
 _appendEscape(expectedSb, '~');
 _appendEscape(expectedSb, '2');
 StringBuilder actualSb = new StringBuilder();
 JsonPointer actualPointer = JsonPointer._parseQuotedTail(jsonPointerString, 1);
 assertEquals(expectedSb.toString(), actualSb.toString());
 }

}