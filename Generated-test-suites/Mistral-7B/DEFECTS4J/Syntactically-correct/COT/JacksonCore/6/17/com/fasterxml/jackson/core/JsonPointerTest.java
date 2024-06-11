import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private static final String VALID_JSON_POINTER_1 = "/foo/bar/baz";
 private static final String VALID_JSON_POINTER_2 = "/0/1/2";
 private static final String VALID_JSON_POINTER_3 = "~1/quux";
 private static final String VALID_JSON_POINTER_4 = "/~1/quux";
 private static final String VALID_JSON_POINTER_5 = "/a/0";
 private static final String VALID_JSON_POINTER_6 = "/a/-1";
 private static final String VALID_JSON_POINTER_7 = "/a/[1]";
 private static final String VALID_JSON_POINTER_8 = "/a/[1,2]";
 private static final String VALID_JSON_POINTER_9 = "/a/[1,2,3]";
 private static final String VALID_JSON_POINTER_10 = "/a/[1,2,3]/b";
 private static final String INVALID_JSON_POINTER = "/a/[a]";

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.EMPTY;
 Assert.assertTrue(jsonPointer.matches());
 Assert.assertEquals("", jsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
 Assert.assertFalse(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile("invalid-json-pointer");
 }

 @Test
 public void testCompileValidJsonPointer1() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_1);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("foo", jsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer2() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_2);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("", jsonPointer.getMatchingProperty());
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer3() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_3);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("quux", jsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer4() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_4);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("quux", jsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer5() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_5);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(0, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer6() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_6);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer7() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_7);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer8() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_8);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer9() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_9);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertTrue(jsonPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer10() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_10);
 Assert.assertFalse(jsonPointer.matches());
 Assert.assertEquals("a", jsonPointer.getMatchingProperty());
 Assert.assertEquals(1, jsonPointer.getMatchingIndex());
 Assert.assertTrue(jsonPointer.mayMatchProperty());
 Assert.assertTrue(jsonPointer.mayMatchElement());
 Assert.assertFalse(jsonPointer.tail().matches());
 Assert.assertEquals("b", jsonPointer.tail().getMatchingProperty());
 Assert.assertEquals(-1, jsonPointer.tail().getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidJsonPointer() {
 JsonPointer jsonPointer = JsonPointer.compile(INVALID_JSON_POINTER);
 }

 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_1);
 JsonPointer matchPropertyJsonPointer = jsonPointer.matchProperty("foo");
 JsonPointer matchPropertyJsonPointer2 = jsonPointer.matchProperty("bar");
 JsonPointer matchPropertyJsonPointer3 = jsonPointer.matchProperty("baz");
 Assert.assertEquals(jsonPointer, matchPropertyJsonPointer);
 Assert.assertEquals(jsonPointer, matchPropertyJsonPointer2);
 Assert.assertEquals(jsonPointer, matchPropertyJsonPointer3);
 Assert.assertNull(jsonPointer.matchProperty("quux"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer jsonPointer = JsonPointer.compile(VALID_JSON_POINTER_2);
 JsonPointer matchElementJsonPointer = jsonPointer.matchElement(0);
 JsonPointer matchElementJsonPointer2 = jsonPointer.matchElement(1);
 JsonPointer matchElementJsonPointer3 = jsonPointer.matchElement(2);
 Assert.assertEquals(jsonPointer, matchElementJsonPointer);
 Assert.assertEquals(jsonPointer, matchElementJsonPointer2);
 Assert.assertEquals(jsonPointer, matchElementJsonPointer3);
 Assert.assertNull(jsonPointer.matchElement(-1));
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer1 = JsonPointer.compile(VALID_JSON_POINTER_1);
 JsonPointer jsonPointer2 = JsonPointer.compile(VALID_JSON_POINTER_2);
 JsonPointer jsonPointer3 = JsonPointer.compile(VALID_JSON_POINTER_3);
 JsonPointer jsonPointer4 = JsonPointer.compile(VALID_JSON_POINTER_4);
 JsonPointer jsonPointer5 = JsonPointer.compile(VALID_JSON_POINTER_5);
 JsonPointer jsonPointer6 = JsonPointer.compile(VALID_JSON_POINTER_6);
 JsonPointer jsonPointer7 = JsonPointer.compile(VALID_JSON_POINTER_7);
 JsonPointer jsonPointer8 = JsonPointer.compile(VALID_JSON_POINTER_8);
 JsonPointer jsonPointer9 = JsonPointer.compile(VALID_JSON_POINTER_9);
 JsonPointer jsonPointer10 = JsonPointer.compile(VALID_JSON_POINTER_10);
 Assert.assertEquals(VALID_JSON_POINTER_1, jsonPointer1.toString());
 Assert.assertEquals(VALID_JSON_POINTER_2, jsonPointer2.toString());
 Assert.assertEquals(VALID_JSON_POINTER_3, jsonPointer3.toString());
 Assert.assertEquals(VALID_JSON_POINTER_4, jsonPointer4.toString());
 Assert.assertEquals(VALID_JSON_POINTER_5, jsonPointer5.toString());
 Assert.assertEquals(VALID_JSON_POINTER_6, jsonPointer6.toString());
 Assert.assertEquals(VALID_JSON_POINTER_7, jsonPointer7.toString());
 Assert.assertEquals(VALID_JSON_POINTER_8, jsonPointer8.toString());
 Assert.assertEquals(VALID_JSON_POINTER_9, jsonPointer9.toString());
 Assert.assertEquals(VALID_JSON_POINTER_10, jsonPointer10.toString());
 }

 @Test
 public void testHashCodeEquals() {
 JsonPointer jsonPointer1 = JsonPointer.compile(VALID_JSON_POINTER_1);
 JsonPointer jsonPointer2 = JsonPointer.compile(VALID_JSON_POINTER_1);
 JsonPointer jsonPointer3 = JsonPointer.compile(VALID_JSON_POINTER_2);
 JsonPointer jsonPointer4 = JsonPointer.compile(VALID_JSON_POINTER_3);
 JsonPointer jsonPointer5 = JsonPointer.compile(VALID_JSON_POINTER_4);
 JsonPointer jsonPointer6 = JsonPointer.compile(VALID_JSON_POINTER_5);
 JsonPointer jsonPointer7 = JsonPointer.compile(VALID_JSON_POINTER_6);
 JsonPointer jsonPointer8 = JsonPointer.compile(VALID_JSON_POINTER_7);
 JsonPointer jsonPointer9 = JsonPointer.compile(VALID_JSON_POINTER_8);
 JsonPointer jsonPointer10 = JsonPointer.compile(VALID_JSON_POINTER_9);
 JsonPointer jsonPointer11 = JsonPointer.compile(VALID_JSON_POINTER_10);
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
 Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer4.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer5.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer6.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer7.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer8.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer9.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer10.hashCode());
 Assert.assertNotEquals(jsonPointer1.hashCode(), jsonPointer11.hashCode());
 Assert.assertTrue(jsonPointer1.equals(jsonPointer1));
 Assert.assertTrue(jsonPointer1.equals(jsonPointer2));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer3));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer4));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer5));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer6));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer7));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer8));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer9));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer10));
 Assert.assertFalse(jsonPointer1.equals(jsonPointer11));
 Assert.assertFalse(jsonPointer1.equals(new Object()));
 }
}