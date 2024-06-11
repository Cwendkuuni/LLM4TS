import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testConstructWithString() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 JsonPointer tail = pointer.tail();
 assertEquals("/bar/baz", tail.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "foo";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 pointer = e;
 }
 assertTrue(pointer != null);
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"foo\"", pointer.getMessage());
 }

 @Test
 public void testMatchProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo", pointer.matchProperty("bar").toString());
 assertNull(pointer.matchProperty("baz"));
 }

 @Test
 public void testMatchElement() {
 String input = "/foo/0/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar", pointer.matchElement(0).toString());
 assertNull(pointer.matchElement(-1));
 assertNull(pointer.matchElement(1));
 }

 @Test
 public void testTail() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/bar/baz", pointer.tail().toString());
 }

 @Test
 public void testEquals() {
 String input1 = "/foo/bar";
 String input2 = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 JsonPointer pointer2 = JsonPointer.compile(input2);
 assertEquals(pointer1, pointer2);
 }

 @Test
 public void testHashCode() {
 String input1 = "/foo/bar";
 String input2 = "/foo/quux";
 JsonPointer pointer1 = JsonPointer.compile(input1);
 JsonPointer pointer2 = JsonPointer.compile(input2);
 int hashCode1 = pointer1.hashCode();
 int hashCode2 = pointer2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }
}