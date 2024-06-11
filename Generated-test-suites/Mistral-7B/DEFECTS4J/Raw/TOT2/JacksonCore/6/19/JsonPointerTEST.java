import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.JsonPointer.*;

import org.junit.Test;

public class JsonPointerTest {

 // Test constructor with valid input
 @Test
 public void testValidInput() {
 String input = "/foo/0";
 JsonPointer jsonPointer = compile(input);
 assertEquals("/foo/0", jsonPointer.toString());
 }

 // Test constructor with empty input
 @Test
 public void testEmptyInput() {
 JsonPointer jsonPointer = EMPTY;
 assertEquals("", jsonPointer.toString());
 assertTrue(jsonPointer.matches());
 }

 // Test matches method with null pointer
 @Test(expected = NullPointerException.class)
 public void testMatchesWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.matches();
 }

 // Test matches method with non-matching pointer
 @Test
 public void testNonMatchingPointer() {
 JsonPointer jsonPointer1 = compile("/foo/0");
 JsonPointer jsonPointer2 = compile("/bar/0");
 assertFalse(jsonPointer1.matches());
 assertFalse(jsonPointer2.matches());
 }

 // Test matches method with matching pointer
 @Test
 public void testMatchingPointer() {
 JsonPointer jsonPointer1 = compile("/foo/0");
 JsonPointer jsonPointer2 = compile("/foo/0");
 assertTrue(jsonPointer1.matches());
 assertTrue(jsonPointer2.matches());
 }

 // Test getMatchingProperty method with null pointer
 @Test(expected = NullPointerException.class)
 public void testGetMatchingPropertyWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.getMatchingProperty();
 }

 // Test getMatchingProperty method with empty pointer
 @Test
 public void testGetMatchingPropertyWithEmptyPointer() {
 JsonPointer jsonPointer = EMPTY;
 assertEquals("", jsonPointer.getMatchingProperty());
 }

 // Test getMatchingProperty method with matching property
 @Test
 public void testGetMatchingPropertyWithMatchingProperty() {
 JsonPointer jsonPointer = compile("/foo/bar");
 assertEquals("bar", jsonPointer.getMatchingProperty());
 }

 // Test getMatchingIndex method with null pointer
 @Test(expected = NullPointerException.class)
 public void testGetMatchingIndexWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.getMatchingIndex();
 }

 // Test getMatchingIndex method with empty pointer
 @Test
 public void testGetMatchingIndexWithEmptyPointer() {
 JsonPointer jsonPointer = EMPTY;
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 // Test getMatchingIndex method with matching index
 @Test
 public void testGetMatchingIndexWithMatchingIndex() {
 JsonPointer jsonPointer = compile("/foo/0");
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test getMatchingIndex method with non-matching index
 @Test
 public void testGetMatchingIndexWithNonMatchingIndex() {
 JsonPointer jsonPointer = compile("/foo/0");
 assertEquals(-1, jsonPointer.getMatchingIndex());
 }

 // Test matchProperty method with null pointer
 @Test(expected = NullPointerException.class)
 public void testMatchPropertyWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.matchProperty("");
 }

 // Test matchProperty method with empty pointer
 @Test
 public void testMatchPropertyWithEmptyPointer() {
 JsonPointer jsonPointer1 = EMPTY;
 JsonPointer jsonPointer2 = compile("/bar/0");
 assertNull(jsonPointer1.matchProperty(""));
 assertNull(jsonPointer2.matchProperty(""));
 }

 // Test matchProperty method with matching property and empty pointer
 @Test
 public void testMatchPropertyWithMatchingPropertyAndEmptyPointer() {
 JsonPointer jsonPointer1 = compile("/foo/bar");
 JsonPointer jsonPointer2 = EMPTY;
 assertEquals(jsonPointer1, jsonPointer2.matchProperty("bar"));
 }

 // Test matchProperty method with matching property and non-matching property
 @Test
 public void testMatchPropertyWithMatchingPropertyAndNonMatchingProperty() {
 JsonPointer jsonPointer1 = compile("/foo/bar");
 JsonPointer jsonPointer2 = compile("/foo/baz");
 assertNull(jsonPointer1.matchProperty("baz"));
 }

 // Test matchElement method with null pointer
 @Test(expected = NullPointerException.class)
 public void testMatchElementWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.matchElement(-1);
 }

 // Test matchElement method with empty pointer
 @Test
 public void testMatchElementWithEmptyPointer() {
 JsonPointer jsonPointer1 = EMPTY;
 JsonPointer jsonPointer2 = compile("/bar/0");
 assertNull(jsonPointer1.matchElement(-1));
 assertNull(jsonPointer2.matchElement(-1));
 }

 // Test matchElement method with matching element and empty pointer
 @Test
 public void testMatchElementWithMatchingElementAndEmptyPointer() {
 JsonPointer jsonPointer1 = compile("/foo/0");
 JsonPointer jsonPointer2 = EMPTY;
 assertEquals(jsonPointer1, jsonPointer2.matchElement(0));
 }

 // Test matchElement method with matching element and non-matching element
 @Test
 public void testMatchElementWithMatchingElementAndNonMatchingElement() {
 JsonPointer jsonPointer1 = compile("/foo/0");
 JsonPointer jsonPointer2 = compile("/foo/1");
 assertNull(jsonPointer1.matchElement(1));
 }

 // Test tail method with null pointer
 @Test(expected = NullPointerException.class)
 public void testTailWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.tail();
 }

 // Test tail method with empty pointer
 @Test
 public void testTailWithEmptyPointer() {
 JsonPointer jsonPointer = EMPTY;
 assertEquals(EMPTY, jsonPointer.tail());
 }

 // Test tail method with non-empty pointer
 @Test
 public void testTailWithNonEmptyPointer() {
 JsonPointer jsonPointer = compile("/foo/bar");
 assertEquals(compile("/bar"), jsonPointer.tail());
 }

 // Test toString method with null pointer
 @Test(expected = NullPointerException.class)
 public void testToStringWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.toString();
 }

 // Test toString method with empty pointer
 @Test
 public void testToStringWithEmptyPointer() {
 JsonPointer jsonPointer = EMPTY;
 assertEquals("", jsonPointer.toString());
 }

 // Test toString method with non-empty pointer
 @Test
 public void testToStringWithNonEmptyPointer() {
 JsonPointer jsonPointer = compile("/foo/bar");
 assertEquals("/foo/bar", jsonPointer.toString());
 }

 // Test hashCode method with null pointer
 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullPointer() {
 JsonPointer jsonPointer = null;
 jsonPointer.hashCode();
 }

 // Test hashCode method with empty pointer
 @Test
 public void testHashCodeWithEmptyPointer() {
 JsonPointer jsonPointer = EMPTY;
 assertEquals(0, jsonPointer.hashCode());
 }

 // Test hashCode method with non-empty pointer
 @Test
 public void testHashCodeWithNonEmptyPointer() {
 JsonPointer jsonPointer = compile("/foo/bar");
 int hashCode = jsonPointer.hashCode();
 JsonPointer jsonPointer2 = compile("/foo/bar");
 assertEquals(hashCode, jsonPointer2.hashCode());
 }

 // Test equals method with null pointer
 @Test
 public void testEqualsWithNullPointer() {
 JsonPointer jsonPointer = EMPTY;
 assertFalse(jsonPointer.equals(null));
 }

 // Test equals method with empty pointer
 @Test
 public void testEqualsWithEmptyPointer() {
 JsonPointer jsonPointer1 = EMPTY;
 JsonPointer jsonPointer2 = EMPTY;
 assertTrue(jsonPointer1.equals(jsonPointer2));
 }

 // Test equals method with non-empty pointers
 @Test
 public void testEqualsWithNonEmptyPointers() {
 JsonPointer jsonPointer1 = compile("/foo/bar");
 JsonPointer jsonPointer2 = compile("/foo/bar");
 assertTrue(jsonPointer1.equals(jsonPointer2));
 }

 // Test equals method with non-matching pointers
 @Test
 public void testEqualsWithNonMatchingPointers() {
 JsonPointer jsonPointer1 = compile("/foo/bar");
 JsonPointer jsonPointer2 = compile("/foo/baz");
 assertFalse(jsonPointer1.equals(jsonPointer2));
 }

 // Test parseIndex method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testParseIndexWithNullInput() {
 JsonPointer.parseIndex(null);
 }

 // Test parseIndex method with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testParseIndexWithEmptyInput() {
 JsonPointer.parseIndex("");
 }

 // Test parseIndex method with valid index
 @Test
 public void testParseIndexWithValidIndex() {
 assertEquals(0, JsonPointer.parseIndex("/foo/0"));
 }

 // Test parseIndex method with invalid index
 @Test(expected = IllegalArgumentException.class)
 public void testParseIndexWithInvalidIndex() {
 JsonPointer.compile("/foo/xyz");
 }

 // Test parseTail method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testParseTailWithNullInput() {
 JsonPointer.parseTail(null);
 }

 // Test parseTail method with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testParseTailWithEmptyInput() {
 JsonPointer.parseTail("");
 }

 // Test parseTail method with valid JSON Pointer
 @Test
 public void testParseTailWithValidJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo/bar/baz");
 JsonPointer expected = JsonPointer.compile("/bar/baz");
 JsonPointer actual = JsonPointer.parseTail(jsonPointer.toString().substring(1));
 assertEquals(expected, actual);
 }

 // Test parseTail method with invalid JSON Pointer
 @Test(expected = IllegalArgumentException.class)
 public void testParseTailWithInvalidJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo");
 JsonPointer.parseTail(jsonPointer.toString());
 }

 // Test parseQuotedTail method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testParseQuotedTailWithNullInput() {
 JsonPointer.parseQuotedTail(null, 0);
 }

 // Test parseQuotedTail method with empty input
 @Test(expected = IllegalArgumentException.class)
 public void testParseQuotedTailWithEmptyInput() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo");
 JsonPointer.parseQuotedTail(jsonPointer.toString(), 0);
 }

 // Test parseQuotedTail method with valid JSON Pointer
 @Test
 public void testParseQuotedTailWithValidJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo/~1/~0/~2");
 JsonPointer expected = JsonPointer.compile("/1/0");
 JsonPointer actual = JsonPointer.parseQuotedTail(jsonPointer.toString(), 1);
 assertEquals(expected, actual);
 }

 // Test parseQuotedTail method with invalid JSON Pointer
 @Test(expected = IllegalArgumentException.class)
 public void testParseQuotedTailWithInvalidJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("/foo");
 JsonPointer.parseQuotedTail(jsonPointer.toString(), 1);
 }

}