import org.junit.Test;
import static org.junit.Assert.*;
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
 public void testConstructNoSegments() {
 String input = "";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(JsonPointer.EMPTY, pointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputNoSlash() {
 String input = "test";
 JsonPointer.compile(input);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testConstructSingleSegment() {
 String input = "/test";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/test", pointer.toString());
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testConstructMultipleSegments() {
 String input = "/test/property";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/test/property", pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testConstructMultipleSegmentsWithIndex() {
 String input = "/test/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/test/0", pointer.toString());
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test
 public void testConstructMultipleSegmentsWithInvalidIndex() {
 String input = "/test/abc";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/test/abc", pointer.toString());
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer = new JsonPointer("/test/property", "property", JsonPointer.EMPTY);
 JsonPointer matchingPointer = new JsonPointer("/test/property");
 assertTrue(matchingPointer.matches());
 assertTrue(matchingPointer.matchProperty(pointer.getMatchingProperty()).matches());
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer = new JsonPointer("/test/0", "", JsonPointer.EMPTY);
 JsonPointer matchingPointer = new JsonPointer("/test");
 assertTrue(matchingPointer.matches());
 assertTrue(matchingPointer.matchElement(pointer.getMatchingIndex()).matches());
 }

 @Test
 public void testTail() {
 JsonPointer pointer = new JsonPointer("/test/property", "property", JsonPointer.EMPTY);
 JsonPointer tailPointer = pointer.tail();
 assertEquals("/test", tailPointer.toString());
 }

 @Test
 public void testToString() {
 JsonPointer pointer = new JsonPointer("/test/property", "property", JsonPointer.EMPTY);
 assertEquals("/test/property", pointer.toString());
 }

 @Test
 public void testHashCodeAndEquals() {
 JsonPointer pointer1 = new JsonPointer("/test/property", "property", JsonPointer.EMPTY);
 JsonPointer pointer2 = new JsonPointer("/test/property", "property", JsonPointer.EMPTY);
 JsonPointer pointer3 = new JsonPointer("/test/element/0", "element", JsonPointer.EMPTY);
 assertEquals(hashCode(pointer1), hashCode(pointer2));
 assertTrue(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 }

 private int hashCode(JsonPointer pointer) {
 return pointer.toString().hashCode();
 }

}