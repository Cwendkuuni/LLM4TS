import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testBasicPointer() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo/bar/baz", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "quux";
 JsonPointer pointer = JsonPointer.compile(input);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testMatchesProperty() {
 String input = "/foo/bar/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("bar", pointer.getMatchingProperty());
 }

 @Test
 public void testMatchesElement() {
 String input = "/foo/0/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test
 public void testTailPointer() {
 String input = "/foo/bar/0/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertNotNull(pointer.tail());
 assertEquals("/bar/0/baz", pointer.tail().toString());
 }

 @Test
 public void testQuotedTailPointer() {
 String input = "/foo/~1/bar/~0/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar/baz", pointer.toString());
 }
}