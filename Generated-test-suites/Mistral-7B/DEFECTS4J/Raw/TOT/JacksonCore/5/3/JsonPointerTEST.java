import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertEquals("", pointer.toString());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.getMatchingProperty());
 assertEquals(JsonPointer.EMPTY, pointer.tail());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar", pointer.toString());
 assertEquals("bar", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertEquals(JsonPointer.EMPTY, pointer.tail());
 }

 @Test
 public void testPointerWithIndex() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/0", pointer.toString());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.getMatchingProperty());
 assertEquals(JsonPointer.EMPTY, pointer.tail());
 }

 @Test
 public void testPointerWithProperty() {
 String input = "/foo/0/prop";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/0/prop", pointer.toString());
 assertEquals("prop", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertEquals(JsonPointer.EMPTY, pointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 // should throw an IllegalArgumentException
 String input = "foo";
 JsonPointer pointer = JsonPointer.compile(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer2() {
 // should throw an IllegalArgumentException
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 }

 @Test
 public void testMatchesProperty() {
 String input = "/foo/bar/prop";
 JsonPointer pointer = JsonPointer.compile(input);
 String property = "prop";
 JsonPointer matchingPointer = pointer.matchProperty(property);
 assertEquals("/foo/bar", matchingPointer.toString());
 }

 @Test
 public void testMatchesElement() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 int index = 0;
 JsonPointer matchingPointer = pointer.matchElement(index);
 assertEquals("/foo", matchingPointer.toString());
 }

 @Test
 public void testMatchesNothing() {
 String input = "/foo/bar/prop";
 JsonPointer pointer = JsonPointer.compile(input);
 String property = "qux";
 JsonPointer matchingPointer = pointer.matchProperty(property);
 assertNull(matchingPointer);
 }

 @Test
 public void testMatchesNothing2() {
 String input = "/foo/bar/prop";
 JsonPointer pointer = JsonPointer.compile(input);
 int index = 5;
 JsonPointer matchingPointer = pointer.matchElement(index);
 assertNull(matchingPointer);
 }
}