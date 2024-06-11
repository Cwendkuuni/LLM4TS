import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

 private JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 return null;
 }
 }

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
 public void testBasicJsonPointerConstruction() {
 String input = "/a/0/b";
 JsonPointer jsonPointer = createJsonPointer(input);

 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "a";
 JsonPointer jsonPointer = createJsonPointer(input);
 }

 @Test
 public void testParseIndex() {
 int index = JsonPointer._parseIndex("/a/1/b/2");

 Assert.assertEquals(2, index);
 }

 @Test
 public void testParseTail() {
 String input = "/a/1/b/2";
 JsonPointer jsonPointer = _parseTail(input);

 Assert.assertEquals("/a/b/2", jsonPointer.toString());
 }

 @Test
 public void testParseQuotedTail() {
 String input = "/a/~0/b/~1/c";
 JsonPointer jsonPointer = _parseQuotedTail(input, 3);

 Assert.assertEquals("/a/b/c", jsonPointer.toString());
 }

 @Test
 public void testEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/1/b", "/a/1/b", null);
 JsonPointer jsonPointer2 = new JsonPointer("/a/1/b", "/a/1/b", null);
 JsonPointer jsonPointer3 = new JsonPointer("/a/1/b", "/a/2/b", null);

 Assert.assertEquals(jsonPointer1, jsonPointer2);
 Assert.assertNotEquals(jsonPointer1, jsonPointer3);
 }

 @Test
 public void testHashCode() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/1/b", "/a/1/b", null);
 JsonPointer jsonPointer2 = new JsonPointer("/a/1/b", "/a/1/b", null);
 JsonPointer jsonPointer3 = new JsonPointer("/a/1/b", "/a/2/b", null);

 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer = new JsonPointer("/a/1/b", "/a/1/b", null);

 Assert.assertEquals("/a/1/b", jsonPointer.toString());
 }

 private JsonPointer _parseTail(String input) {
 return JsonPointer._parseTail(input);
 }

 private JsonPointer _parseQuotedTail(String input, int i) {
 return JsonPointer._parseQuotedTail(input, i);
 }

}