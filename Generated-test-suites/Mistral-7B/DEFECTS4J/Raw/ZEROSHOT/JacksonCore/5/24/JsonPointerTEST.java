import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 Assert.assertTrue(jsonPointer.matches());
 Assert.assertEquals("", jsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
 Assert.assertFalse(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testJsonPointerCreation() {
 String jsonPointerString = "/TestProperty/0";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("TestProperty", jsonPointer.getMatchingProperty());
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonPointerCreation() {
 String invalidJsonPointerString = "TestProperty";
 JsonPointer jsonPointer = null;
 try {
 jsonPointer = JsonPointer.compile(invalidJsonPointerString);
 } catch (IllegalArgumentException e) {
 jsonPointer = e.getCause();
 }
 Assert.assertNotNull(jsonPointer);
 Assert.assertEquals("Invalid input: JSON Pointer expression must start with '/': \"TestProperty\"", jsonPointer.getMessage());
 }

 @Test
 public void testJsonPointerTail() {
 JsonPointer jsonPointer = JsonPointer.compile("/TestProperty/0");
 JsonPointer jsonPointerTail = jsonPointer.tail();
 Assert.assertEquals("/TestProperty", jsonPointer.toString());
 Assert.assertEquals("", jsonPointerTail.toString());
 }

 @Test
 public void testJsonPointerMatchProperty() {
 JsonPointer jsonPointer = JsonPointer.compile("/TestProperty/0");
 JsonPointer matchingPointer = JsonPointer.compile("/TestObject/TestProperty/0");
 JsonPointer nonMatchingPointer = JsonPointer.compile("/TestObject/OtherProperty/0");
 Assert.assertNull(jsonPointer.matchProperty(null));
 Assert.assertEquals(jsonPointer, jsonPointer.matchProperty("TestProperty"));
 Assert.assertNull(jsonPointer.matchProperty("TestObject"));
 Assert.assertNull(jsonPointer.matchProperty("OtherProperty"));
 Assert.assertNull(matchingPointer.matchProperty("TestProperty"));
 Assert.assertEquals(matchingPointer, matchingPointer.matchProperty("TestObject/TestProperty"));
 Assert.assertNull(nonMatchingPointer.matchProperty("TestProperty"));
 }

 @Test
 public void testJsonPointerMatchElement() {
 JsonPointer jsonPointer = JsonPointer.compile("/TestObject/[0]");
 JsonPointer matchingPointer = JsonPointer.compile("/TestObject/[0]");
 JsonPointer nonMatchingPointer = JsonPointer.compile("/TestObject/[1]");
 JsonPointer arrayJsonPointer = JsonPointer.compile("/TestObject/[]");
 Assert.assertNull(jsonPointer.matchElement(-1));
 Assert.assertEquals(jsonPointer, jsonPointer.matchElement(0));
 Assert.assertNull(jsonPointer.matchElement(1));
 Assert.assertNull(matchingPointer.matchElement(0));
 Assert.assertNull(nonMatchingPointer.matchElement(0));
 Assert.assertNull(arrayJsonPointer.matchElement(0));
 Assert.assertNull(arrayJsonPointer.matchElement(1));
 }
}