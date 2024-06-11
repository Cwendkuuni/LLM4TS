import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer empty = JsonPointer.EMPTY;
 assertEquals("", empty.toString());
 assertEquals(empty, empty.tail());
 assertNull(empty.matches());
 assertNull(empty.getMatchingProperty());
 assertNull(empty.getMatchingIndex());
 assertFalse(empty.mayMatchProperty());
 assertFalse(empty.mayMatchElement());
 }

 @Test
 public void testSimplePointer() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals(pointerString, pointer.toString());
 assertEquals(pointer, pointer.tail());
 assertTrue(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testPointerWithElement() {
 String pointerString = "/list/0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals(pointerString, pointer.toString());
 assertEquals(pointer, pointer.tail());
 assertTrue(pointer.matches());
 assertFalse(pointer.mayMatchProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String pointerString = "invalid";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPointerWithQuoted() {
 String pointerString = "/test/property/~1~2/element";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals(pointerString, pointer.toString());
 assertEquals(pointer, pointer.tail());
 assertTrue(pointer.matches());
 assertEquals("test/property/element", pointer.getMatchingProperty());
 assertEquals(2, pointer.getMatchingIndex());
 }

 @Test
 public void testPathsWithSameSegments() {
 String pointer1String = "/test/property";
 String pointer2String = "/test/element/property";
 JsonPointer pointer1 = JsonPointer.compile(pointer1String);
 JsonPointer pointer2 = JsonPointer.compile(pointer2String);
 JsonPointer common = pointer1.matchProperty("/property");
 assertEquals(pointer2, common.tail());
 }

 @Test
 public void testPointerWithInvalidIndex() {
 String pointerString = "/list/abcd";
 try {
 JsonPointer pointer = JsonPointer.compile(pointerString);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}