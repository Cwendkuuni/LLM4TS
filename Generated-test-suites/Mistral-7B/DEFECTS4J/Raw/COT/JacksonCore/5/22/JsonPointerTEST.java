import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

 private static JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 Assert.fail("Failed to create JsonPointer instance: " + e.getMessage());
 return null;
 }
 }

 @Test
 public void testEmptyConstructor() {
 JsonPointer jsonPointer = new JsonPointer();
 Assert.assertNull(jsonPointer._nextSegment);
 Assert.assertEquals("", jsonPointer._asString);
 }

 @Test
 public void testConstructorWithArguments() {
 String fullString = "/test/property1/0";
 String segment = "property1";
 JsonPointer next = new JsonPointer("/test/property2");
 JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
 Assert.assertEquals(fullString, jsonPointer._asString);
 Assert.assertEquals(segment, jsonPointer._matchingPropertyName);
 Assert.assertEquals(next, jsonPointer._nextSegment);
 }

 @Test
 public void testCompileMethod() {
 String input = "/test/property1/0";
 JsonPointer jsonPointer = createJsonPointer(input);
 Assert.assertEquals(input, jsonPointer._asString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileMethodWithInvalidInput() {
 String input = "testProperty1";
 JsonPointer jsonPointer = createJsonPointer(input);
 Assert.fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testToStringMethod() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 Assert.assertEquals("/test/property1/0", jsonPointer.toString());
 }

 @Test
 public void testHashCodeMethod() {
 JsonPointer jsonPointer1 = createJsonPointer("/test/property1/0");
 JsonPointer jsonPointer2 = createJsonPointer("/test/property1/0");
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 JsonPointer jsonPointer1 = createJsonPointer("/test/property1/0");
 JsonPointer jsonPointer2 = createJsonPointer("/test/property1/0");
 JsonPointer jsonPointer3 = createJsonPointer("/test/property2/1");
 Assert.assertEquals(jsonPointer1, jsonPointer2);
 Assert.assertNotEquals(jsonPointer1, jsonPointer3);
 }

 @Test
 public void testMatchesMethod() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 Assert.assertTrue(jsonPointer.matches());
 }

 @Test
 public void testGetMatchingPropertyMethod() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 Assert.assertEquals("property1", jsonPointer.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndexMethod() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testMayMatchPropertyMethod() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 }

 @Test
 public void testMayMatchElementMethod() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test
 public void testMatchPropertyMethod() {
 String propertyName = "property1";
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 JsonPointer expectedJsonPointer = createJsonPointer("/test/property1");
 Assert.assertEquals(expectedJsonPointer, jsonPointer.matchProperty(propertyName));
 }

 @Test
 public void testMatchElementMethod() {
 int expectedIndex = 0;
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 Assert.assertEquals(createJsonPointer("/test/property1/0"), jsonPointer.matchElement(expectedIndex));
 }

 @Test
 public void testTailMethod() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 JsonPointer expectedJsonPointer = createJsonPointer("/test/property1");
 Assert.assertEquals(expectedJsonPointer, jsonPointer.tail());
 }

 @Test(expected = NullPointerException.class)
 public void testMatchesPropertyMethodWithNullPointer() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 String propertyName = null;
 jsonPointer.matchProperty(propertyName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMatchPropertyMethodWithInvalidPropertyName() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/0");
 String propertyName = "invalid";
 jsonPointer.matchProperty(propertyName);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testMatchElementMethodWithNegativeIndex() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/3");
 jsonPointer.matchElement(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testMatchElementMethodWithInvalidIndex() {
 JsonPointer jsonPointer = createJsonPointer("/test/property1/3");
 jsonPointer.matchElement(4);
 }
}