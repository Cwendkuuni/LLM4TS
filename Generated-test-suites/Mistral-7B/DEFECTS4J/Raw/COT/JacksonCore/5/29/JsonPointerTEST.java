import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private String input;

 @Test
 public void testEmptyConstructor() {
 JsonPointer jsonPointer = new JsonPointer();
 Assert.assertNull(jsonPointer._nextSegment);
 Assert.assertEquals("", jsonPointer._asString);
 Assert.assertEquals("", jsonPointer._matchingPropertyName);
 Assert.assertEquals(-1, jsonPointer._matchingElementIndex);
 }

 @Test
 public void testStringConstructor() throws IllegalArgumentException {
 input = "/foo/0/bar";
 JsonPointer jsonPointer = new JsonPointer(input, "foo", new JsonPointer("/0/bar", "", null));
 Assert.assertEquals(input, jsonPointer._asString);
 Assert.assertEquals("foo", jsonPointer._matchingPropertyName);
 Assert.assertEquals(0, jsonPointer._matchingElementIndex);
 Assert.assertEquals("/0/bar", jsonPointer._nextSegment._asString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputConstructor() {
 input = "foo";
 JsonPointer jsonPointer = new JsonPointer(input, "", null);
 }

 @Test
 public void testMatches() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 Assert.assertTrue(jsonPointer.matches());
 }

 @Test
 public void testGetMatchingProperty() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 Assert.assertEquals("foo", jsonPointer.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndex() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @Test
 public void testMayMatchProperty() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 }

 @Test
 public void testMayMatchElement() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 JsonPointer expectedJsonPointer = new JsonPointer("/bar");
 JsonPointer resultJsonPointer = jsonPointer.matchProperty("foo");
 Assert.assertEquals(expectedJsonPointer, resultJsonPointer);
 }

 @Test
 public void testMatchElement() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 JsonPointer expectedJsonPointer = new JsonPointer("/bar");
 JsonPointer resultJsonPointer = jsonPointer.matchElement(0);
 Assert.assertEquals(expectedJsonPointer, resultJsonPointer);
 }

 @Test
 public void testTail() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 JsonPointer expectedJsonPointer = new JsonPointer("/0/bar");
 JsonPointer resultJsonPointer = jsonPointer.tail();
 Assert.assertEquals(expectedJsonPointer, resultJsonPointer);
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer = new JsonPointer("/foo/0/bar");
 Assert.assertEquals("/foo/0/bar", jsonPointer.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer jsonPointer1 = new JsonPointer("/foo/0/bar");
 JsonPointer jsonPointer2 = new JsonPointer("/foo/0/bar");
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/foo/0/bar");
 JsonPointer jsonPointer2 = new JsonPointer("/foo/0/bar");
 JsonPointer jsonPointer3 = new JsonPointer("/foo/1/bar");
 Assert.assertTrue(jsonPointer1.equals(jsonPointer2));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer3));
 }

 @Test
 public void testParseIndex() {
 String input = "0";
 Assert.assertEquals(0, JsonPointer._parseIndex(input));
 input = "1234567890";
 Assert.assertEquals(1234567890, JsonPointer._parseIndex(input));
 input = "12345678901234567890";
 Assert.assertEquals(-1, JsonPointer._parseIndex(input));
 input = "1234567890x";
 Assert.assertEquals(-1, JsonPointer._parseIndex(input));
 }

 @Test
 public void testParseTail() {
 input = "/foo/0/bar/~1/baz~2";
 JsonPointer jsonPointer = JsonPointer._parseTail(input);
 Assert.assertEquals("/baz", jsonPointer._nextSegment._asString);
 }

 @Test
 public void testParseQuotedTail() {
 input = "/foo/0/bar/~1/baz~2/~0";
 JsonPointer jsonPointer = JsonPointer._parseTail(input);
 Assert.assertEquals("/baz", jsonPointer._nextSegment._asString);
 }

}