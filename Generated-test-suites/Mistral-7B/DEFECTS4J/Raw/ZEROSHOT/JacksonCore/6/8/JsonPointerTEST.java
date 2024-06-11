import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testBasicConstruction() {
 JsonPointer pointer = new JsonPointer("/foo/0/bar");
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/0/bar", pointer.tail().toString());
 assertEquals("/foo/0/bar", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile("baz");
 } catch (IllegalArgumentException e) {
 pointer = e.getCause();
 }
 assertNotNull(pointer);
 assertEquals("Invalid input: JSON Pointer expression must start with '/': \"baz\"", pointer.getMessage());
 }

 @Test
 public void testParseTail() {
 JsonPointer pointer = JsonPointer.compile("/foo/quux/~1/bar");
 assertTrue(pointer.matches());
 assertEquals("quux", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/bar", pointer.tail().toString());
 }

 @Test
 public void testParseQuotedTail() {
 JsonPointer pointer = JsonPointer.compile("/foo/quux/~1/baz~1/quux");
 assertTrue(pointer.matches());
 assertEquals("quux", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/baz~1/quux", pointer.tail().toString());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer = new JsonPointer("/foo/0/bar");
 JsonPointer matchingPropertyPointer = new JsonPointer("/foo/bar");
 JsonPointer matchingElementPointer = new JsonPointer("/foo/0/quux");
 assertEquals(matchingPropertyPointer, pointer.matchProperty("bar"));
 assertNull(pointer.matchProperty("quux"));
 assertNull(pointer.matchProperty("baz"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer = new JsonPointer("/foo/0/bar");
 JsonPointer matchingElementPointer = new JsonPointer("/foo/1/bar");
 JsonPointer matchingElementPointer2 = new JsonPointer("/foo/0/quux");
 assertEquals(matchingElementPointer2, pointer.matchElement(1));
 assertNull(pointer.matchElement(2));
 assertNull(pointer.matchElement(-1));
 }
}