import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private static JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // Handle the case when input is an invalid JSON Pointer
 Assert.fail("Error while creating JsonPointer: " + e.getMessage());
 return null;
 }
 }

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer();
 Assert.assertTrue(jsonPointer.matches());
 Assert.assertEquals("", jsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
 Assert.assertEquals(jsonPointer, jsonPointer.tail());
 Assert.assertEquals("", jsonPointer.toString());
 }

 @Test
 public void testJsonPointerConstruction() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = createJsonPointer(input);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("foo", jsonPointer.getMatchingProperty());
 Assert.assertEquals(1, jsonPointer.getMatchingIndex());
 Assert.assertEquals("/foo/bar", jsonPointer.tail().toString());
 Assert.assertEquals(input, jsonPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonPointerInput() {
 String input = "foo";
 createJsonPointer(input); // This will throw an IllegalArgumentException
 }

 @Test
 public void testJsonPointerTailWithNullNextSegment() {
 JsonPointer jsonPointer = new JsonPointer();
 Assert.assertEquals(jsonPointer, jsonPointer.tail());
 }

 @Test
 public void testJsonPointerMatchesProperty() {
 String input = "/foo/bar";
 JsonPointer jsonPointer = createJsonPointer(input);
 JsonPointer propertyJsonPointer = new JsonPointer("/foo");
 Assert.assertNotNull(jsonPointer.matchProperty("bar"));
 Assert.assertEquals(propertyJsonPointer, jsonPointer.matchProperty("foo"));
 Assert.assertNull(jsonPointer.matchProperty("baz"));
 }

 @Test
 public void testJsonPointerMatchesElement() {
 String input = "/foo/1";
 JsonPointer jsonPointer = createJsonPointer(input);
 JsonPointer elementJsonPointer = new JsonPointer("/foo/0");
 Assert.assertNotNull(jsonPointer.matchElement(1));
 Assert.assertEquals(elementJsonPointer, jsonPointer.matchElement(0));
 Assert.assertNull(jsonPointer.matchElement(-1));
 }

 @Test
 public void testJsonPointerToString() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer = createJsonPointer(input);
 Assert.assertEquals(input, jsonPointer.toString());
 }

 @Test
 public void testJsonPointerEquals() {
 String input1 = "/foo/bar/1";
 String input2 = "/foo/bar/1";
 JsonPointer jsonPointer1 = createJsonPointer(input1);
 JsonPointer jsonPointer2 = createJsonPointer(input2);
 Assert.assertEquals(jsonPointer1, jsonPointer2);
 }

 @Test
 public void testJsonPointerHashCode() {
 String input = "/foo/bar/1";
 JsonPointer jsonPointer1 = createJsonPointer(input);
 String input2 = "/foo/bar/2";
 JsonPointer jsonPointer2 = createJsonPointer(input2);
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

}