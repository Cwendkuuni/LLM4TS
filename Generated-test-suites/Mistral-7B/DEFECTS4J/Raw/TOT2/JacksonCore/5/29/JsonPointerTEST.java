import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 // Test for constructor with empty string
 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertTrue(jsonPointer.matches());
 assertNull(jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 // Test for constructor with valid JSON Pointer
 @Test
 public void testValidJsonPointer() throws IllegalArgumentException {
 String jsonPointerString = "/foo/bar/baz";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 assertEquals(jsonPointerString, jsonPointer.toString());
 assertTrue(jsonPointer.mayMatchProperty());
 assertTrue(jsonPointer.mayMatchElement());
 assertFalse(jsonPointer.matches());
 }

 // Test for constructor with invalid JSON Pointer
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonPointer() {
 String jsonPointerString = "foo";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 }

 // Test for matchProperty method with matching property
 @Test
 public void testMatchProperty() {
 String jsonPointerString1 = "/foo/bar";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 String jsonPointerString2 = "/foo/bar";
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 JsonPointer result = jsonPointer1.matchProperty(jsonPointer2.getMatchingProperty());
 assertEquals(jsonPointer2, result);
 }

 // Test for matchProperty method with non-matching property
 @Test
 public void testMatchPropertyNonMatching() {
 String jsonPointerString1 = "/foo/bar";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 String jsonPointerString2 = "/foo/baz";
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 JsonPointer result = jsonPointer1.matchProperty(jsonPointer2.getMatchingProperty());
 assertNull(result);
 }

 // Test for matchElement method with matching index
 @Test
 public void testMatchElement() {
 String jsonPointerString1 = "/foo/0";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 String jsonPointerString2 = "/foo/0";
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 JsonPointer result = jsonPointer1.matchElement(jsonPointer2.getMatchingIndex());
 assertEquals(jsonPointer2, result);
 }

 // Test for matchElement method with non-matching index
 @Test
 public void testMatchElementNonMatching() {
 String jsonPointerString1 = "/foo/0";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 String jsonPointerString2 = "/foo/1";
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 JsonPointer result = jsonPointer1.matchElement(jsonPointer2.getMatchingIndex());
 assertNull(result);
 }

 // Test for tail method with matching tail
 @Test
 public void testTailMatching() {
 String jsonPointerString1 = "/foo/bar/baz";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 String jsonPointerString2 = "/foo/bar";
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 JsonPointer result = jsonPointer2.tail();
 assertEquals(jsonPointer1.tail(), result);
 }

 // Test for tail method with non-matching tail
 @Test
 public void testTailNonMatching() {
 String jsonPointerString1 = "/foo/bar/baz";
 JsonPointer jsonPointer1 = JsonPointer.compile(jsonPointerString1);
 String jsonPointerString2 = "/foo/qux";
 JsonPointer jsonPointer2 = JsonPointer.compile(jsonPointerString2);
 JsonPointer result = jsonPointer2.tail();
 assertNull(result);
 }
}