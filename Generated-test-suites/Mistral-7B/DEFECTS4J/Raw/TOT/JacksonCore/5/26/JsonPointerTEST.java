import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertEquals("", pointer.toString());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.tail());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo", pointer.toString());
 assertNull(pointer.tail());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 }

 @Test
 public void testPointerWithProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar", pointer.toString());
 assertEquals("bar", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertSame(input.substring(0, input.lastIndexOf("/")), tail.toString());
 }

 @Test
 public void testPointerWithIndex() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/0", pointer.toString());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithoutSlash() {
 JsonPointer.compile("foo");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithNonEscapedSpecialCharacter() {
 JsonPointer.compile("/foo~");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithTwoSlashes() {
 JsonPointer.compile("/foo//bar");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithEscapedCharacterWithoutTilde() {
 JsonPointer.compile("/foo/~1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithInvalidIndex() {
 JsonPointer.compile("/foo/-1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithLongIndex() {
 JsonPointer.compile("/foo/12345678901234567890");
 }

 @Test
 public void testMatchProperties() {
 String input = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input);
 String propertyName = "bar";
 JsonPointer pointer2 = new JsonPointer(input, propertyName, JsonPointer.EMPTY);
 assertEquals(pointer1, pointer2);
 }

 @Test
 public void testMatchIndexes() {
 String input = "/foo/0";
 JsonPointer pointer1 = JsonPointer.compile(input);
 int index = 0;
 JsonPointer pointer2 = new JsonPointer(input, "", JsonPointer.EMPTY);
 pointer2.setMatchingIndex(index);
 assertEquals(pointer1, pointer2);
 }

 @Test
 public void testMatchEmptyAndNull() {
 JsonPointer pointer1 = JsonPointer.EMPTY;
 JsonPointer pointer2 = null;
 assertEquals(pointer1, pointer2);
 }

 @Test
 public void testMatchDifferentStrings() {
 JsonPointer pointer1 = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
 JsonPointer pointer2 = new JsonPointer("/baz", "qux", JsonPointer.EMPTY);
 assertNotSame(pointer1, pointer2);
 }

 @Test
 public void testMatchDifferentNextSegments() {
 JsonPointer pointer1 = new JsonPointer("/foo/bar", "", JsonPointer.compile("/qux"));
 JsonPointer pointer2 = new JsonPointer("/foo/qux", "", JsonPointer.compile("/bar"));
 assertNotSame(pointer1, pointer2);
 }
}