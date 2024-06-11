import org.junit.Assert;import org.junit.Test;import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertFalse(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/foo");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertTrue(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile("/foo/0");
 } catch (IllegalArgumentException e) {
 fail("Expected valid pointer for /foo/0");
 }
 assertTrue(pointer.matches());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertEquals("/foo/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("~invalid");
 fail("Expected IllegalArgumentException for invalid input");
 }

 @Test
 public void testPointerWithMultipleSegments() {
 JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
 assertTrue(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo/0/bar", pointer.toString());
 }

 @Test
 public void testPointerTail() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 JsonPointer tail = pointer.tail();
 assertNotNull(tail);
 assertFalse(tail.matches());
 assertEquals("", tail.getMatchingProperty());
 assertFalse(tail.mayMatchProperty());
 assertFalse(tail.mayMatchElement());
 assertEquals("/", tail.toString());
 }

 @Test
 public void testPointerMatchesProperty() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 String property = "foo";
 JsonPointer matchedPointer = pointer.matchProperty(property);
 assertNotNull(matchedPointer);
 assertEquals(property, matchedPointer.getMatchingProperty());
 assertEquals("/foo", matchedPointer.toString());
 }

 @Test
 public void testPointerMatchesElementWithIndex() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 int index = 0;
 JsonPointer matchedPointer = pointer.matchElement(index);
 assertNotNull(matchedPointer);
 assertEquals(index, matchedPointer.getMatchingIndex());
 assertEquals("/foo", matchedPointer.toString());
 }

 @Test
 public void testPointerMatchesElementWithWrongIndex() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 int index = 1;
 JsonPointer matchedPointer = pointer.matchElement(index);
 assertNull(matchedPointer);
 }
}