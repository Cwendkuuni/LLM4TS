import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private static final String JSON_POINTER_1 = "/test/property1";
 private static final String JSON_POINTER_2 = "/test/property1/property2";
 private static final String JSON_POINTER_3 = "/test/property1/0";
 private static final String JSON_POINTER_4 = "/test/property1/1";
 private static final String JSON_POINTER_5 = "/test/property1/2";
 private static final String JSON_POINTER_6 = "/test/property2";
 private static final String JSON_POINTER_7 = "/test/property2/0";
 private static final String JSON_POINTER_8 = "/test/property2/1";
 private static final String JSON_POINTER_9 = "/test/property2/2";

 @Test
 public void testEmptyConstructor() {
 JsonPointer emptyJsonPointer = new JsonPointer();
 assertTrue(emptyJsonPointer.matches());
 assertNull(emptyJsonPointer.getMatchingProperty());
 assertNull(emptyJsonPointer.getMatchingIndex());
 assertFalse(emptyJsonPointer.mayMatchProperty());
 assertFalse(emptyJsonPointer.mayMatchElement());
 }

 @Test
 public void testConstructorWithFullStringAndSegment() {
 JsonPointer jsonPointer = new JsonPointer(JSON_POINTER_1, "property1", null);
 assertEquals(JSON_POINTER_1, jsonPointer.toString());
 assertTrue(jsonPointer.matches());
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @Test
 public void testConstructorWithFullStringAndNextSegment() {
 JsonPointer jsonPointer = new JsonPointer(JSON_POINTER_2, "property1", new JsonPointer(JSON_POINTER_2, "property2", null));
 assertEquals(JSON_POINTER_2, jsonPointer.toString());
 assertFalse(jsonPointer.matches());
 assertEquals("property1", jsonPointer.getMatchingProperty());
 assertNull(jsonPointer.getMatchingIndex());
 assertTrue(jsonPointer.mayMatchProperty());
 assertTrue(jsonPointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer.compile("invalidInput");
 }

 @Test
 public void testCompile() {
 JsonPointer jsonPointer1 = JsonPointer.compile(JSON_POINTER_1);
 assertEquals(JSON_POINTER_1, jsonPointer1.toString());
 JsonPointer jsonPointer2 = JsonPointer.compile(JSON_POINTER_2);
 assertEquals(JSON_POINTER_2, jsonPointer2.toString());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer = new JsonPointer(JSON_POINTER_1, "property1", null);
 JsonPointer matchingProperty = new JsonPointer("/test/property1", "property1", null);
 assertSame(matchingProperty, jsonPointer.matchProperty("property1"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer jsonPointer = new JsonPointer(JSON_POINTER_3, "property1", null);
 JsonPointer matchingElement = new JsonPointer("/test/property1/0", "property1/0", null);
 assertSame(matchingElement, jsonPointer.matchElement(0));
 }

 @Test
 public void testTail() {
 JsonPointer jsonPointer = new JsonPointer(JSON_POINTER_2, "property1", new JsonPointer(JSON_POINTER_2, "property2", null));
 assertEquals("/test/property2", jsonPointer.tail().toString());
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer1 = new JsonPointer(JSON_POINTER_1, "property1", null);
 JsonPointer jsonPointer2 = new JsonPointer(JSON_POINTER_2, "property1", new JsonPointer(JSON_POINTER_2, "property2", null));
 assertEquals(JSON_POINTER_1, jsonPointer1.toString());
 assertEquals(JSON_POINTER_2, jsonPointer2.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer jsonPointer1 = new JsonPointer(JSON_POINTER_1, "property1", null);
 JsonPointer jsonPointer2 = new JsonPointer(JSON_POINTER_1, "property1", null);
 JsonPointer jsonPointer3 = new JsonPointer(JSON_POINTER_1, "property2", null);
 JsonPointer jsonPointer4 = new JsonPointer(JSON_POINTER_2, "property1", new JsonPointer(JSON_POINTER_2, "property2", null));
 assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
 assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
 assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
 assertNotEquals(jsonPointer1.hashCode(), jsonPointer4.hashCode());
 }

 @Test
 public void testEquals() {
 JsonPointer jsonPointer1 = new JsonPointer(JSON_POINTER_1, "property1", null);
 JsonPointer jsonPointer2 = new JsonPointer(JSON_POINTER_1, "property1", null);
 JsonPointer jsonPointer3 = new JsonPointer(JSON_POINTER_1, "property2", null);
 JsonPointer jsonPointer4 = new JsonPointer(JSON_POINTER_2, "property1", new JsonPointer(JSON_POINTER_2, "property2", null));
 assertTrue(jsonPointer1.equals(jsonPointer1));
 assertTrue(jsonPointer1.equals(jsonPointer2));
 assertFalse(jsonPointer1.equals(jsonPointer3));
 assertFalse(jsonPointer1.equals(jsonPointer4));
 }

 @Test
 public void testParseIndex() {
 assertEquals(0, JsonPointer._parseIndex(""));
 assertEquals(0, JsonPointer._parseIndex("0"));
 assertEquals(2, JsonPointer._parseIndex("2"));
 assertEquals(-1, JsonPointer._parseIndex("test"));
 assertEquals(-1, JsonPointer._parseIndex("test/"));
 assertEquals(-1, JsonPointer._parseIndex("test/property1"));
 assertEquals(-1, JsonPointer._parseIndex("test/property1/"));
 assertEquals(-1, JsonPointer._parseIndex("test/property1/2345678901234567890"));
 }

 @Test
 public void testParseTail() {
 String jsonPointerString1 = "/test/property1";
 String jsonPointerString2 = "/test/property1/property2";
 String jsonPointerString3 = "/test/property1/0";
 String jsonPointerString4 = "/test/property1/1";
 String jsonPointerString5 = "/test/property1/2";
 String jsonPointerString6 = "/test/property2";
 String jsonPointerString7 = "/test/property2/0";
 String jsonPointerString8 = "/test/property2/1";
 String jsonPointerString9 = "/test/property2/2";
 JsonPointer jsonPointer1 = JsonPointer._parseTail(jsonPointerString1);
 JsonPointer jsonPointer2 = JsonPointer._parseTail(jsonPointerString2);
 JsonPointer jsonPointer3 = JsonPointer._parseTail(jsonPointerString3);
 JsonPointer jsonPointer4 = JsonPointer._parseTail(jsonPointerString4);
 JsonPointer jsonPointer5 = JsonPointer._parseTail(jsonPointerString5);
 JsonPointer jsonPointer6 = JsonPointer._parseTail(jsonPointerString6);
 JsonPointer jsonPointer7 = JsonPointer._parseTail(jsonPointerString7);
 JsonPointer jsonPointer8 = JsonPointer._parseTail(jsonPointerString8);
 JsonPointer jsonPointer9 = JsonPointer._parseTail(jsonPointerString9);
 assertEquals("/test/property1", jsonPointer1.toString());
 assertEquals("/test/property1/property2", jsonPointer2.toString());
 assertEquals("/test/property1/0", jsonPointer3.toString());
 assertEquals("/test/property1/1", jsonPointer4.toString());
 assertEquals("/test/property1/2", jsonPointer5.toString());
 assertEquals("/test/property2", jsonPointer6.toString());
 assertEquals("/test/property2/0", jsonPointer7.toString());
 assertEquals("/test/property2/1", jsonPointer8.toString());
 assertEquals("/test/property2/2", jsonPointer9.toString());
 }

}