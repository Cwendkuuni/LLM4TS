import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private static final String VALID_JSON_POINTER = "/a/0/b/1";
 private static final String VALID_EMPTY_JSON_POINTER = "/";
 private static final String INVALID_JSON_POINTER = "~/a/0";
 private static final String INVALID_EMPTY_JSON_POINTER = "";

 @Test
 public void testEmptyJsonPointerConstruction() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 Assert.assertTrue(emptyJsonPointer.matches());
 Assert.assertEquals("", emptyJsonPointer.getAsString());
 }

 @Test
 public void testJsonPointerConstruction() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals(VALID_JSON_POINTER, jsonPointer.getAsString());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidJsonPointerConstruction() {
 JsonPointer jsonPointer = new JsonPointer(INVALID_JSON_POINTER, "", null);
 }

 @Test
 public void testCompileJsonPointerFactoryMethod() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals(VALID_JSON_POINTER, jsonPointer.getAsString());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testValueOfJsonPointerFactoryMethod() {
 JsonPointer jsonPointer = JsonPointer.valueOf(VALID_JSON_POINTER);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals(VALID_JSON_POINTER, jsonPointer.getAsString());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testTailJsonPointerMethod() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 JsonPointer tailJsonPointer = jsonPointer.tail();
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals(VALID_JSON_POINTER.substring(3), tailJsonPointer.getAsString());
 }

 @Test
 public void testMatchesJsonPointerMethod() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 Assert.assertFalse(jsonPointer.matches());
 }

 @Test
 public void testGetMatchingPropertyJsonPointerMethod() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndexJsonPointerMethod() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a/0", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testMatchPropertyJsonPointerMethod() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 JsonPointer matchingPropertyJsonPointer = jsonPointer.matchProperty("a");
 Assert.assertEquals(jsonPointer, matchingPropertyJsonPointer);
 }

 @Test
 public void testMatchElementJsonPointerMethod() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a/0", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 JsonPointer matchingElementJsonPointer = jsonPointer.matchElement(0);
 Assert.assertEquals(jsonPointer, matchingElementJsonPointer);
 }

 @Test
 public void testEmptyJsonPointerHashCodeEquals() {
 JsonPointer emptyJsonPointer1 = new JsonPointer();
 JsonPointer emptyJsonPointer2 = new JsonPointer();
 Assert.assertEquals(emptyJsonPointer1, emptyJsonPointer2);
 }

 @Test
 public void testJsonPointerHashCodeEquals() {
 JsonPointer jsonPointer1 = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 JsonPointer jsonPointer2 = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 Assert.assertEquals(jsonPointer1, jsonPointer2);
 }

 @Test
 public void testInvalidJsonPointerCompileMethod() {
 try {
 JsonPointer jsonPointer = JsonPointer.compile(INVALID_JSON_POINTER);
 Assert.fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidEmptyJsonPointerConstruction() {
 JsonPointer jsonPointer = new JsonPointer(INVALID_EMPTY_JSON_POINTER);
 Assert.assertNull(jsonPointer);
 }

 @Test
 public void testEmptyJsonPointerToString() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 Assert.assertEquals("", emptyJsonPointer.toString());
 }

 @Test
 public void testJsonPointerToString() {
 JsonPointer jsonPointer = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 Assert.assertEquals(VALID_JSON_POINTER, jsonPointer.toString());
 }

 @Test
 public void testJsonPointerHashCode() {
 JsonPointer jsonPointer1 = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 JsonPointer jsonPointer2 = new JsonPointer(VALID_JSON_POINTER, "a", new JsonPointer(VALID_JSON_POINTER.substring(3)));
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }
}