import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 // Tests for constructor JsonPointer()
 @Test
 public void testEmptyJsonPointer() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertTrue(emptyJsonPointer.matches());
 assertEquals("", emptyJsonPointer.getMatchingProperty());
 assertEquals(-1, emptyJsonPointer.getMatchingIndex());
 assertFalse(emptyJsonPointer.mayMatchProperty());
 assertFalse(emptyJsonPointer.mayMatchElement());
 assertNull(emptyJsonPointer.tail());
 }

 // Tests for constructor JsonPointer(String fullString, String segment, JsonPointer next)
 @Test
 public void testJsonPointerWithSegment() {
 String input = "/a/b/c";
 JsonPointer jsonPointer = new JsonPointer(input, "b", new JsonPointer("/a/c"));
 assertTrue(jsonPointer.matches());
 assertEquals("b", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertSame(new JsonPointer("/a/c"), jsonPointer.tail());
 }

 // Tests for factory method compile(String input)
 @Test
 public void testJsonPointerCompile() throws IllegalArgumentException {
 String input = "/a/b/c";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertTrue(jsonPointer.matches());
 assertEquals("b", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertSame(new JsonPointer("/c"), jsonPointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompileWithInvalidInput() throws IllegalArgumentException {
 String input = "abcd";
 JsonPointer.compile(input); // expected exception
 }

 // Test for factory method valueOf(String input)
 @Test
 public void testJsonPointerValueOf() throws IllegalArgumentException {
 String input = "/a/b/c";
 JsonPointer jsonPointer = JsonPointer.valueOf(input);
 assertTrue(jsonPointer.matches());
 assertEquals("b", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertSame(new JsonPointer("/c"), jsonPointer.tail());
 }

 // Test for factory method fromSegment(String... segments)
 @Test
 public void testJsonPointerFromSegment() {
 String[] segments = {"a", "b", "c"};
 JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
 assertTrue(jsonPointer.matches());
 assertEquals("b", jsonPointer.getMatchingProperty());
 assertEquals(-1, jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertSame(new JsonPointer("c"), jsonPointer.tail());
 }

 // Edge cases and exception handling tests
 @Test(expected = NullPointerException.class)
 public void testJsonPointerNullPointerException() {
 JsonPointer jsonPointer = new JsonPointer(null, "a", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerEmptySegmentException() {
 String input = "/a/";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertSame(JsonPointer.EMPTY, jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerEmptyInputException() {
 JsonPointer jsonPointer = JsonPointer.compile("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerLeadingSlashException() {
 String input = "/";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertSame(JsonPointer.EMPTY, jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerSingleSegmentException() {
 String input = "/a";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertSame(new JsonPointer("/a"), jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerMultipleSlashesException() {
 String input = "/a//b/c";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertSame(new JsonPointer("/b/c"), jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerDoubleQuotesException() {
 String input = "/a/" + "\"" + "/b/c";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertSame(new JsonPointer("/b/c"), jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerEscapedSlashException() {
 String input = "/a/~2f/b/c";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertSame(new JsonPointer("/b/c"), jsonPointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerEscapedDoubleQuotesException() {
 String input = "/a/~22/b/c";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 assertSame(new JsonPointer("/b/c"), jsonPointer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJsonPointerNegativeIndexException() {
 String input = "/a/0";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 JsonPointer expectedJsonPointer = new JsonPointer("/a", "", JsonPointer.EMPTY);
 assertEquals(expectedJsonPointer, jsonPointer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testJsonPointerNegativeIndexTailException() {
 String input = "/a/0";
 JsonPointer jsonPointer = new JsonPointer("/a/b", "b", JsonPointer.EMPTY);
 JsonPointer expectedJsonPointer = new JsonPointer("/a", "", JsonPointer.EMPTY);
 assertEquals(expectedJsonPointer, jsonPointer.tail());
 JsonPointer tailJsonPointer = jsonPointer.tail();
 assertEquals(-1, tailJsonPointer.getMatchingIndex());
 }

 @Test
 public void testJsonPointerLongIndex() {
 String input = "/a/999999999";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 JsonPointer expectedJsonPointer = new JsonPointer("/a", "", JsonPointer.EMPTY);
 assertEquals(expectedJsonPointer, jsonPointer);
 }

 @Test
 public void testJsonPointerLongIndexTail() {
 String input = "/a/999999999";
 JsonPointer jsonPointer = new JsonPointer("/a/b", "b", JsonPointer.EMPTY);
 JsonPointer expectedJsonPointer = new JsonPointer("/a", "", JsonPointer.EMPTY);
 assertEquals(expectedJsonPointer, jsonPointer.tail());
 JsonPointer tailJsonPointer = jsonPointer.tail();
 assertEquals(-1, tailJsonPointer.getMatchingIndex());
 }
}