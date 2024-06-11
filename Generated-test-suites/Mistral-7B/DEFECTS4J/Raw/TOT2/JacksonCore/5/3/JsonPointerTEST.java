import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 // Test constructor with empty input
 @Test
 public void testEmptyConstructor() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.getAsString());
 }

 // Test constructor with valid JSON Pointer input
 @Test
 public void testValidConstructor() {
 String input = "/foo/0/bar";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertFalse(jsonPointer.matches());
 assertEquals("/foo/0/bar", jsonPointer.getAsString());
 assertEquals("bar", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test constructor with invalid JSON Pointer input
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 String input = "~invalid";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 }

 // Test matching methods with empty pointer
 @Test
 public void testMatchingMethodsWithEmptyPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertFalse(jsonPointer.matches());
 assertNull(jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 // Test matching methods with non-empty pointer
 @Test
 public void testMatchingMethodsWithNonEmptyPointer() {
 String input = "/foo/0/bar";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertFalse(jsonPointer.matches());
 assertEquals("/foo/0/bar", jsonPointer.getAsString());
 assertEquals("bar", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 // Test tail method with empty pointer
 @Test
 public void testTailMethodWithEmptyPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
 }

 // Test tail method with non-empty pointer
 @Test
 public void testTailMethodWithNonEmptyPointer() {
 String input = "/foo/0/bar";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 JsonPointer expected = JsonPointer.compile("/foo");
 assertEquals(expected, jsonPointer.tail());
 }

 // Test equality with same pointer
 @Test
 public void testEqualityWithSamePointer() {
 String input = "/foo/0/bar";
 JsonPointer jsonPointer1 = JsonPointer.compile(input);
 JsonPointer jsonPointer2 = JsonPointer.compile(input);
 assertTrue(jsonPointer1.equals(jsonPointer2));
 }

 // Test equality with different pointers
 @Test
 public void testEqualityWithDifferentPointers() {
 String input1 = "/foo/0/bar";
 String input2 = "/baz/1/qux";
 JsonPointer jsonPointer1 = JsonPointer.compile(input1);
 JsonPointer jsonPointer2 = JsonPointer.compile(input2);
 assertFalse(jsonPointer1.equals(jsonPointer2));
 }

 // Test hashCode method with same pointer
 @Test
 public void testHashCodeWithSamePointer() {
 String input = "/foo/0/bar";
 JsonPointer jsonPointer1 = JsonPointer.compile(input);
 JsonPointer jsonPointer2 = JsonPointer.compile(input);
 assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

 // Test hashCode method with different pointers
 @Test
 public void testHashCodeWithDifferentPointers() {
 String input1 = "/foo/0/bar";
 String input2 = "/baz/1/qux";
 JsonPointer jsonPointer1 = JsonPointer.compile(input1);
 JsonPointer jsonPointer2 = JsonPointer.compile(input2);
 assertNotEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

}