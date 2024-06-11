import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

 private static final String VALID_JSON_POINTER = "/foo/0";
 private static final String EMPTY_JSON_POINTER = "/";
 private static final String INVALID_JSON_POINTER = "~/";

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
 Assert.assertTrue(emptyJsonPointer.matches());
 Assert.assertEquals("", emptyJsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, emptyJsonPointer.getMatchingIndex());
 Assert.assertEquals(EMPTY_JSON_POINTER, emptyJsonPointer.toString());
 }

 @Test
 public void testValidJsonPointer() {
 JsonPointer validJsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 Assert.assertFalse(validJsonPointer.matches());
 Assert.assertEquals("foo", validJsonPointer.getMatchingProperty());
 Assert.assertEquals(0, validJsonPointer.getMatchingIndex());
 Assert.assertEquals(VALID_JSON_POINTER, validJsonPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonPointer() {
 JsonPointer invalidJsonPointer = null;
 try {
 invalidJsonPointer = JsonPointer.compile(INVALID_JSON_POINTER);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 Assert.assertNull(invalidJsonPointer);
 }

 @Test
 public void testFromSegments() {
 // TODO: Implement fromSegment factory method and write the test case
 }

 @Test
 public void testMatchesProperty() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0", "foo", null);
 Assert.assertTrue(jsonPointer.matchesProperty("foo"));
 Assert.assertFalse(jsonPointer.matchesProperty("bar"));
 }

 @Test
 public void testMatchesElement() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0", "", new JsonPointer("/foo/1", "", null));
 Assert.assertTrue(jsonPointer.matchesElement(0));
 Assert.assertFalse(jsonPointer.matchesElement(1));
 Assert.assertFalse(jsonPointer.matchesElement(-1));
 }

 @Test
 public void testTail() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0", "", null);
 JsonPointer tail = jsonPointer.tail();
 Assert.assertEquals("/foo", tail.toString());
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0", "foo", null);
 Assert.assertEquals(VALID_JSON_POINTER, jsonPointer.toString());
 }

 @Test
 public void testHashCodeAndEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/foo/0", "foo", null);
 JsonPointer jsonPointer2 = new JsonPointer("/foo/0", "foo", null);
 JsonPointer jsonPointer3 = new JsonPointer("/foo/1", "foo", null);
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 Assert.assertTrue(jsonPointer1.equals(jsonPointer2));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer3));
 }
}