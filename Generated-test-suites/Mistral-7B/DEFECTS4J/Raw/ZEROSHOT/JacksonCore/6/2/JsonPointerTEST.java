import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @org.junit.Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
assertTrue(emptyPointer.matches());
assertNull(emptyPointer.getMatchingProperty());
assertFalse(emptyPointer.mayMatchProperty());
assertFalse(emptyPointer.mayMatchElement());
assertEquals("", emptyPointer.toString());
 }

 @org.junit.Test
 public void testConstructorWithEmptyInput() {
 JsonPointer jsonPointer = new JsonPointer("");
 assertEquals("", jsonPointer.toString());
 assertEquals(JsonPointer.EMPTY, jsonPointer);
 }

 @org.junit.Test
 public void testConstructorWithValidInput() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals("/a/b/0", jsonPointer.toString());
 assertEquals("b", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidInput() {
 String input = "a";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 fail("Exception expected: IllegalArgumentException");
 }

 @org.junit.Test
 public void testTailMethod() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 JsonPointer tail = jsonPointer.tail();
 assertEquals("/b/0", tail.toString());
 }

 @org.junit.Test
 public void testMatchesMethod() {
 String input1 = "/a/b/0";
 JsonPointer jsonPointer1 = JsonPointer.compile(input1);
 String input2 = "/a/b/1";
 JsonPointer jsonPointer2 = JsonPointer.compile(input2);
 assertTrue(jsonPointer1.matches());
 assertFalse(jsonPointer2.matches());
 }

 @org.junit.Test
 public void testGetMatchingPropertyMethod() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals("b", jsonPointer.getMatchingProperty());
 }

 @org.junit.Test
 public void testGetMatchingIndexMethod() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(0, jsonPointer.getMatchingIndex());
 }

 @org.junit.Test
 public void testMayMatchPropertyMethod() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertTrue(jsonPointer.mayMatchProperty());
 }

 @org.junit.Test
 public void testMayMatchElementMethod() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertFalse(jsonPointer.mayMatchElement());
 }

 @org.junit.Test
 public void testMatchPropertyMethod() {
 String input1 = "/a/b/0";
 JsonPointer jsonPointer1 = JsonPointer.compile(input1);
 String input2 = "/a/b";
 JsonPointer jsonPointer2 = JsonPointer.compile(input2);
 JsonPointer result = jsonPointer1.matchProperty(input2);
 assertNull(result);
 }

 @org.junit.Test
 public void testMatchElementMethod() {
 String input1 = "/a/b/0";
 JsonPointer jsonPointer1 = JsonPointer.compile(input1);
 int index = 0;
 JsonPointer result = jsonPointer1.matchElement(index);
 assertEquals(jsonPointer1, result);
 }

 @org.junit.Test
 public void testMatchElementMethodWithInvalidIndex() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 int index = 1;
 JsonPointer result = jsonPointer.matchElement(index);
 assertNull(result);
 }

 @org.junit.Test
 public void testToStringMethod() {
 String input = "/a/b/0";
 JsonPointer jsonPointer = JsonPointer.compile(input);
 assertEquals(input, jsonPointer.toString());
 }

 @org.junit.Test
 public void testHashCodeMethod() {
 String input1 = "/a/b/0";
 JsonPointer jsonPointer1 = JsonPointer.compile(input1);String input2 = "/a/b/1"; JsonPointer jsonPointer2 = JsonPointer.compile(input2); JsonPointer jsonPointer3 = JsonPointer.EMPTY; int hashCode1 = jsonPointer1.hashCode(); int hashCode2 = jsonPointer2.hashCode(); int hashCode3 = jsonPointer3.hashCode(); assertNotEquals(hashCode1, hashCode2); assertEquals(hashCode1, jsonPointer1.hashCode()); assertEquals(hashCode2, jsonPointer2.hashCode()); assertEquals(hashCode3, jsonPointer3.hashCode()); }

 @org.junit.Test
 public void testEqualsMethod() {
 String input1 = "/a/b/0"; JsonPointer jsonPointer1 = JsonPointer.compile(input1); String input2 = "/a/b/0"; JsonPointer jsonPointer2 = JsonPointer.compile(input2); String input3 = "/a/b/1"; JsonPointer jsonPointer3 = JsonPointer.compile(input3); JsonPointer jsonPointer4 = JsonPointer.EMPTY; JsonPointer jsonPointer5 = new JsonPointer(); assertEquals(jsonPointer1, jsonPointer1); assertEquals(jsonPointer1, jsonPointer2); assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode()); assertNotEquals(jsonPointer1, jsonPointer3); assertNotEquals(jsonPointer1, jsonPointer4); assertNotEquals(jsonPointer1, jsonPointer5); }
}