import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 // Basic test case for empty constructor
 @Test
 public void testEmptyConstructor() {
 JsonPointer pointer = new JsonPointer();
 assertTrue(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertEquals(0, pointer.toString().length());
 }

 // Basic test case for constructor with segment and next pointer
 @Test
 public void testConstructorWithSegmentAndNextPointer() {
 String fullString = "/property/0";
 String segment = "property";
 JsonPointer nextPointer = new JsonPointer("/0");
 JsonPointer pointer = new JsonPointer(fullString, segment, nextPointer);
 assertTrue(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertEquals(fullString, pointer.toString());
 assertEquals(nextPointer, pointer.tail());
 }

 // Test for compile factory method with valid JSON Pointer
 @Test
 public void testCompileValidJsonPointer() {
 String jsonPointerString = "/property/0";
 JsonPointer pointer = JsonPointer.compile(jsonPointerString);
 assertTrue(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertEquals(jsonPointerString, pointer.toString());
 }

 // Test for compile factory method with invalid JSON Pointer
 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidJsonPointer() {
 String jsonPointerString = "property";
 JsonPointer.compile(jsonPointerString);
 }

 // Test for valueOf factory method
 @Test
 public void testValueOf() {
 String jsonPointerString = "/property/0";
 JsonPointer pointer = JsonPointer.valueOf(jsonPointerString);
 assertTrue(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertEquals(jsonPointerString, pointer.toString());
 }

 // Test for matches method
 @Test
 public void testMatches() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property/1");
 JsonPointer pointer3 = new JsonPointer("/property");
 JsonPointer pointer4 = new JsonPointer();

 assertTrue(pointer1.matches());
 assertFalse(pointer2.matches());
 assertFalse(pointer3.matches());
 assertTrue(pointer4.matches());
 }

 // Test for getMatchingProperty method
 @Test
 public void testGetMatchingProperty() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property/1");
 JsonPointer pointer3 = new JsonPointer("/property");
 JsonPointer pointer4 = new JsonPointer();

 assertEquals("property", pointer1.getMatchingProperty());
 assertEquals(null, pointer2.getMatchingProperty());
 assertEquals(null, pointer3.getMatchingProperty());
 assertEquals("", pointer4.getMatchingProperty());
 }

 // Test for getMatchingIndex method
 @Test
 public void testGetMatchingIndex() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property/1");
 JsonPointer pointer3 = new JsonPointer("/property");
 JsonPointer pointer4 = new JsonPointer("/array/1/2");

 assertEquals(-1, pointer1.getMatchingIndex());
 assertEquals(-1, pointer2.getMatchingIndex());
 assertEquals(-1, pointer3.getMatchingIndex());
 assertEquals(1, pointer4.getMatchingIndex());
 }

 // Test for mayMatchProperty method
 @Test
 public void testMayMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property/1");
 JsonPointer pointer3 = new JsonPointer("/property");
 JsonPointer pointer4 = new JsonPointer();

 assertTrue(pointer1.mayMatchProperty());
 assertFalse(pointer2.mayMatchProperty());
 assertFalse(pointer3.mayMatchProperty());
 assertFalse(pointer4.mayMatchProperty());
 }

 // Test for mayMatchElement method
 @Test
 public void testMayMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property/1");
 JsonPointer pointer3 = new JsonPointer("/property");
 JsonPointer pointer4 = new JsonPointer("/array/1/2");

 assertFalse(pointer1.mayMatchElement());
 assertFalse(pointer2.mayMatchElement());
 assertFalse(pointer3.mayMatchElement());
 assertTrue(pointer4.mayMatchElement());
 }

 // Test for matchProperty method
 @Test
 public void testMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property/1");
 JsonPointer pointer3 = new JsonPointer("/property");
 JsonPointer matchingPointer = new JsonPointer("/property");

 assertEquals(pointer1, pointer1.matchProperty("property"));
 assertEquals(null, pointer1.matchProperty("wrongProperty"));
 assertEquals(pointer2, pointer2.matchProperty("property"));
 assertEquals(null, pointer2.matchProperty("wrongProperty"));
 assertEquals(matchingPointer, pointer3.matchProperty("property"));
 assertEquals(null, pointer3.matchProperty("wrongProperty"));
 }

 // Test for matchElement method
 @Test
 public void testMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/array/0/0");
 JsonPointer pointer2 = new JsonPointer("/array/1/0");
 JsonPointer pointer3 = new JsonPointer("/array/0/1");
 JsonPointer pointer4 = new JsonPointer("/array/1/1");
 JsonPointer pointer5 = new JsonPointer("/array");

 assertEquals(pointer1, pointer1.matchElement(0));
 assertEquals(null, pointer1.matchElement(1));
 assertEquals(pointer2, pointer2.matchElement(0));
 assertEquals(null, pointer2.matchElement(1));
 assertEquals(pointer3, pointer3.matchElement(1));
 assertEquals(pointer4, pointer4.matchElement(1));
 assertEquals(null, pointer5.matchElement(0));
 assertEquals(null, pointer5.matchElement(1));
 }

 // Test for tail method
 @Test
 public void testTail() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property");

 assertEquals(null, pointer1.tail());
 assertEquals(pointer1, pointer2.tail());
 }

 // Test for toString method
 @Test
 public void testToString() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property");

 assertEquals("/property/0", pointer1.toString());
 assertEquals("/property", pointer2.toString());
 }

 // Test for hashCode method
 @Test
 public void testHashCode() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property");

 assertEquals(pointer1.hashCode(), pointer1.toString().hashCode());
 assertEquals(pointer2.hashCode(), pointer2.toString().hashCode());
 assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
 }

 // Test for equals method
 @Test
 public void testEquals() {
 JsonPointer pointer1 = new JsonPointer("/property/0");
 JsonPointer pointer2 = new JsonPointer("/property/0");
 JsonPointer pointer3 = new JsonPointer("/property/1");
 JsonPointer pointer4 = new JsonPointer("/property");

 assertTrue(pointer1.equals(pointer1));
 assertTrue(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 assertFalse(pointer1.equals(pointer4));
 }
}