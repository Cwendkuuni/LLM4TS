import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertEquals("", emptyPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = JsonPointer.compile("invalidInput");
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/example");
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/example", pointer.toString());
 }

 @Test
 public void testPointerWithPropertyMatch() {
 JsonPointer pointer = JsonPointer.compile("/example/property");
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchElement());
 assertTrue(pointer.mayMatchProperty());
 assertEquals("/example/property", pointer.toString());
 }

 @Test
 public void testPointerWithElementMatch() {
 JsonPointer pointer = JsonPointer.compile("/example/array/0");
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals("/example/array/0", pointer.toString());
 }

 @Test
 public void testPointerWithMatchingPropertyAndElement() {
 JsonPointer pointer = JsonPointer.compile("/example/array/0/property");
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchElement());
 assertTrue(pointer.mayMatchProperty());
 assertEquals("/example/array/0/property", pointer.toString());
 }

 @Test
 public void testPointerWithTail() {
 JsonPointer pointer = JsonPointer.compile("/example/array/0");
 JsonPointer tailPointer = pointer.tail();
 assertFalse(tailPointer.matches());
 assertNull(tailPointer.getMatchingProperty());
 assertEquals(-1, tailPointer.getMatchingIndex());
 assertFalse(tailPointer.mayMatchProperty());
 assertFalse(tailPointer.mayMatchElement());
 assertEquals("/example/array", tailPointer.toString());
 }

 @Test
 public void testPointerWithMultipleSegments() {
 JsonPointer pointer = JsonPointer.compile("/example/array/0/property/subArray/1");
 assertFalse(pointer.matches());
 assertEquals("subArray", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/example/array/0/property/subArray/1", pointer.toString());
 }
}