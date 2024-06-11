import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 private static JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Failed to create JsonPointer with input: " + input);
 }
 return null;
 }

 @Test
 public void testEmptyConstruction() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.getAsString());
 }

 @Test
 public void testNonEmptyConstruction() {
 String input = "/a/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertFalse(jsonPointer.matches());
 assertEquals("/a/0", jsonPointer.getAsString());
 assertEquals("a", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "ab";
 createJsonPointer(input); // this line will throw an IllegalArgumentException
 }

 @Test
 public void testMatchProperty() {
 String input = "/a/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 JsonPointer matchedJsonPointer = jsonPointer.matchProperty("a");
 assertEquals(input.substring(0, input.lastIndexOf("/")), matchedJsonPointer.getAsString());
 }

 @Test
 public void testMatchElement() {
 String input = "/a/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 JsonPointer matchedJsonPointer = jsonPointer.matchElement(0);
 assertEquals(input.substring(0, input.lastIndexOf("/")), matchedJsonPointer.getAsString());
 }

 @Test
 public void testTail() {
 String input = "/a/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 JsonPointer tailJsonPointer = jsonPointer.tail();
 assertEquals("/a", tailJsonPointer.getAsString());
 }

 @Test
 public void testToString() {
 String input = "/a/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertEquals(input, jsonPointer.toString());
 }

 @Test
 public void testHashCodeAndEquals() {
 String input1 = "/a/0";
 String input2 = "/a/0";
 JsonPointer jsonPointer1 = createJsonPointer(input1);
 JsonPointer jsonPointer2 = createJsonPointer(input2);
 assertEquals(jsonPointer1, jsonPointer2);
 assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

 @Test
 public void testInvalidInputWithSlash() {
 String input = "/";
 createJsonPointer(input); // this line will not throw an IllegalArgumentException
 fail("Expected IllegalArgumentException with input: " + input);
 }

 @Test
 public void testEmptyInput() {
 String input = "";
 JsonPointer jsonPointer = createJsonPointer(input);
 assertTrue(jsonPointer.matches());
 assertEquals("", jsonPointer.getAsString());
 }

}