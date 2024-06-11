import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

 // Test case for constructor with empty input
 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("");
 Assert.assertEquals("", jsonPointer.toString());
 Assert.assertTrue(jsonPointer.matches());
 }

 // Test case for constructor with valid input
 @Test
 public void testValidJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/property");
 Assert.assertEquals("/test/property", jsonPointer.toString());
 Assert.assertFalse(jsonPointer.matches());
 }

 // Test case for factory method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 JsonPointer.compile(null);
 }

 // Test case for factory method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer.compile("test");
 }

 // Test case for matchProperty method with matching property
 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/property");
 JsonPointer matchingProperty = JsonPointer.compile("/test/property");
 JsonPointer result = jsonPointer.matchProperty("property");
 Assert.assertEquals(matchingProperty, result);
 }

 // Test case for matchProperty method with non-matching property
 @Test
 public void testNonMatchingProperty() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/property");
 JsonPointer nonMatchingProperty = JsonPointer.compile("/test/anotherProperty");
 JsonPointer result = jsonPointer.matchProperty("anotherProperty");
 Assert.assertNull(result);
 }

 // Test case for matchElement method with matching index
 @Test
 public void testMatchElementWithMatchingIndex() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/0/property");
 JsonPointer result = jsonPointer.matchElement(0);
 Assert.assertEquals(EMPTY, result);
 }

 // Test case for matchElement method with non-matching index
 @Test
 public void testMatchElementWithNonMatchingIndex() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/0/property");
 JsonPointer result = jsonPointer.matchElement(1);
 Assert.assertNull(result);
 }

 // Test case for matchElement method with negative index
 @Test
 public void testMatchElementWithNegativeIndex() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/1/property");
 JsonPointer result = jsonPointer.matchElement(-1);
 Assert.assertNull(result);
 }

 // Test case for tail method with matching tail
 @Test
 public void testTailWithMatchingTail() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/property/element");
 JsonPointer result = jsonPointer.tail();
 Assert.assertEquals("/property/element", result.toString());
 }

 // Test case for tail method with null tail
 @Test
 public void testTailWithNullTail() {
 JsonPointer jsonPointer = JsonPointer.compile("");
 JsonPointer result = jsonPointer.tail();
 Assert.assertNull(result);
 }

 // Test case for equals method with equal objects
 @Test
 public void testEqualsWithEqualObjects() {
 JsonPointer jsonPointer1 = JsonPointer.compile("/test/property");
 JsonPointer jsonPointer2 = JsonPointer.compile("/test/property");
 Assert.assertEquals(jsonPointer1, jsonPointer2);
 }

 // Test case for equals method with non-equal objects
 @Test
 public void testEqualsWithNonEqualObjects() {
 JsonPointer jsonPointer1 = JsonPointer.compile("/test/property");
 JsonPointer jsonPointer2 = JsonPointer.compile("/test/anotherProperty");
 Assert.assertNotEquals(jsonPointer1, jsonPointer2);
 }

 // Test case for equals method with null object
 @Test
 public void testEqualsWithNullObject() {
 JsonPointer jsonPointer = JsonPointer.compile("/test/property");
 Assert.assertNotEquals(jsonPointer, null);
 }
}