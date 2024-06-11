import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

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
 public void testBasicPointer() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testPropertyMatching() {
 String input = "/foo/bar/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertNull(pointer.tail());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test
 public void testElementMatching() {
 String input = "/foo/0/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertNull(pointer.tail());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "foo";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 fail(); // should not reach here
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"foo\"", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSegment() {
 String input = "/foo/bar/quux";
 JsonPointer pointer = JsonPointer.compile(input);
 assertNotNull(pointer.tail());
 }

 @Test
 public void testTailMethod() {
 String input = "/foo/bar/0";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer tail = pointer.tail();
 assertEquals("bar/0", tail.toString());
 }

 @Test
 public void testCompoundPointer() {
 String input = "/foo/bar/0/baz/quux";
 JsonPointer pointer = JsonPointer.compile(input);
 assertNull(pointer.tail());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 }

 @Test
 public void testQuotedSegment() {
 String input = "/foo/bar/~1/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("bar/1", pointer.toString());
 }

 @Test
 public void testQuotedQuotedSegment() {
 String input = "/foo/bar/~1/~1/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("bar/1", pointer.toString());
 }

 @Test
 public void testQuotedDoubleSegment() {
 String input = "/foo/bar/~1~1/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("bar/1", pointer.toString());
 }
}