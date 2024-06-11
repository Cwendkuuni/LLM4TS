import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private static final String VALID_JSON_POINTER_1 = "/a/0";
 private static final String VALID_JSON_POINTER_2 = "/a/b/c/1";
 private static final String VALID_JSON_POINTER_3 = "/";
 private static final String VALID_JSON_POINTER_4 = "~1/a/b/c/1";
 private static final String VALID_JSON_POINTER_5 = "~1/a/b/c/12";
 private static final String VALID_JSON_POINTER_6 = "~1/a/b~1/c/d/~2";
 private static final String VALID_JSON_POINTER_7 = "~1/a/b/c/123"; // exceeding int limit

 private static final String INVALID_JSON_POINTER_1 = "/a/b/";
 private static final String INVALID_JSON_POINTER_2 = "/a/b/c/1/";
 private static final String INVALID_JSON_POINTER_3 = "/a/b/c/1/1";
 private static final String INVALID_JSON_POINTER_4 = "/a/b//c/1";
 private static final String INVALID_JSON_POINTER_5 = "/a/b/c/1/~";
 private static final String INVALID_JSON_POINTER_6 = "/a/b/c/1/~1";
 private static final String INVALID_JSON_POINTER_7 = "/a/b/c/1/~1/";
 private static final String INVALID_JSON_POINTER_8 = "/a/b/c/1/~1/~";
 private static final String INVALID_JSON_POINTER_9 = "/a/b/c/1/~1/~1";

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
 assertTrue(emptyJsonPointer.matches());
 assertNull(emptyJsonPointer.getMatchingProperty());
 assertFalse(emptyJsonPointer.mayMatchProperty());
 assertFalse(emptyJsonPointer.mayMatchElement());
 }

 @Test
 public void testJsonPointerConstructionWithEmptyString() {
 JsonPointer jsonPointer = new JsonPointer();
 assertTrue(jsonPointer.matches());
 assertNull(jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertEquals("", jsonPointer.toString());
 }

 @Test
 public void testJsonPointerConstructionWithValidSegmentsAndNextJsonPointer() {
 JsonPointer jsonPointer = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 assertFalse(jsonPointer.matches());
 assertEquals("b", jsonPointer.getMatchingProperty());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertEquals("/a/b/c/1", jsonPointer.toString());
 }

 @Test
 public void testJsonPointerCompilationWithValidJsonPointers() {
 for (String validJsonPointer : new String[] { VALID_JSON_POINTER_1, VALID_JSON_POINTER_2, VALID_JSON_POINTER_3, VALID_JSON_POINTER_4, VALID_JSON_POINTER_5, VALID_JSON_POINTER_6, VALID_JSON_POINTER_7 }) {
 JsonPointer jsonPointer = JsonPointer.compile(validJsonPointer);
 assertFalse(jsonPointer.matches());
 assertEquals(validJsonPointer.substring(1), jsonPointer.getMatchingProperty());
 assertTrue(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 assertEquals(validJsonPointer, jsonPointer.toString());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer() {
 JsonPointer.compile(INVALID_JSON_POINTER_1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer2() {
 JsonPointer.compile(INVALID_JSON_POINTER_2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer3() {
 JsonPointer.compile(INVALID_JSON_POINTER_3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer4() {
 JsonPointer.compile(INVALID_JSON_POINTER_4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer5() {
 JsonPointer.compile(INVALID_JSON_POINTER_5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer6() {
 JsonPointer.compile(INVALID_JSON_POINTER_6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer7() {
 JsonPointer.compile(INVALID_JSON_POINTER_7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer8() {
 JsonPointer.compile(INVALID_JSON_POINTER_8);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJsonPointerCompilationWithInvalidJsonPointer9() {
 JsonPointer.compile(INVALID_JSON_POINTER_9);
 }

 @Test
 public void testJsonPointerMatchProperty() {
 JsonPointer jsonPointer = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer matchingJsonPointer = jsonPointer.matchProperty("b");
 assertTrue(matchingJsonPointer != null);
 assertEquals(jsonPointer, matchingJsonPointer);
 }

 @Test
 public void testJsonPointerMatchElement() {
 JsonPointer jsonPointer = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer matchingJsonPointer = jsonPointer.matchElement(0);
 assertTrue(matchingJsonPointer != null);
 assertEquals(jsonPointer, matchingJsonPointer);
 }

 @Test
 public void testJsonPointerTail() {
 JsonPointer jsonPointer = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer tailJsonPointer = jsonPointer.tail();
 assertEquals("/b/c/1", tailJsonPointer.toString());
 }

 @Test
 public void testJsonPointerEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer jsonPointer3 = new JsonPointer("/a/b/c/2", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer jsonPointer4 = new JsonPointer("/a/b/c/1", "/b/c/2", JsonPointer.EMPTY);
 JsonPointer jsonPointer5 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.COMPILE("/d/e/f"));
 JsonPointer jsonPointer6 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.COMPILE("/a/b/c/1"));

 assertTrue(jsonPointer1.equals(jsonPointer1));
 assertTrue(jsonPointer1.equals(jsonPointer2));
 assertFalse(jsonPointer1.equals(jsonPointer3));
 assertFalse(jsonPointer1.equals(jsonPointer4));
 assertFalse(jsonPointer1.equals(jsonPointer5));
 assertFalse(jsonPointer1.equals(jsonPointer6));
 }

 @Test
 public void testJsonPointerHashCode() {
 JsonPointer jsonPointer1 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer jsonPointer2 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer jsonPointer3 = new JsonPointer("/a/b/c/2", "/b/c/1", JsonPointer.EMPTY);
 JsonPointer jsonPointer4 = new JsonPointer("/a/b/c/1", "/b/c/2", JsonPointer.EMPTY);
 JsonPointer jsonPointer5 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.COMPILE("/d/e/f"));
 JsonPointer jsonPointer6 = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.COMPILE("/a/b/c/1"));

 int jsonPointer1HashCode = jsonPointer1.hashCode();
 int jsonPointer2HashCode = jsonPointer2.hashCode();
 int jsonPointer3HashCode = jsonPointer3.hashCode();
 int jsonPointer4HashCode = jsonPointer4.hashCode();
 int jsonPointer5HashCode = jsonPointer5.hashCode();
 int jsonPointer6HashCode = jsonPointer6.hashCode();

 assertTrue(jsonPointer1.hashCode() == jsonPointer2.hashCode());
 assertFalse(jsonPointer1.hashCode() == jsonPointer3.hashCode());
 assertFalse(jsonPointer1.hashCode() == jsonPointer4.hashCode());
 assertFalse(jsonPointer1.hashCode() == jsonPointer5.hashCode());
 assertFalse(jsonPointer1.hashCode() == jsonPointer6.hashCode());
 }

 @Test
 public void testJsonPointerToString() {
 JsonPointer jsonPointer = new JsonPointer("/a/b/c/1", "/b/c/1", JsonPointer.EMPTY);
 assertEquals("/a/b/c/1", jsonPointer.toString());
 }
}