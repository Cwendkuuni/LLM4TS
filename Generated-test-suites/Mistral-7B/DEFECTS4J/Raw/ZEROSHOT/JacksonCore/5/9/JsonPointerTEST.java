import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testCreatePointer() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertFalse(pointer.matches());
 assertEquals("/test", pointer.toString().substring(0, pointer.toString().lastIndexOf("/")));
 assertEquals("property", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertTrue(pointer.mayMatchProperty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String pointerString = "test";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(pointerString);
 } catch (IllegalArgumentException e) {
 pointer = e.getCause();
 }
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"test\"", pointer.getMessage());
 }

 @Test
 public void testMatchProperty() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 String propertyName = "property";
 JsonPointer matchedPointer = pointer.matchProperty(propertyName);
 assertEquals(pointerString.substring(pointerString.lastIndexOf("/")+1), propertyName);
 assertEquals(pointer, matchedPointer.tail());
 }

 @Test
 public void testMatchElement() {
 String pointerString = "/test/0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 JsonPointer matchedPointer = pointer.matchElement(0);
 assertEquals(pointerString.substring(pointerString.lastIndexOf("/")+1), "0");
 assertEquals(pointer, matchedPointer.tail());
 }

 @Test
 public void testMatchNone() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertNull(pointer.matchElement(-1));
 assertNull(pointer.matchProperty("invalidProperty"));
 }
}