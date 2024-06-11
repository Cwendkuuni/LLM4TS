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
 public void testConstructorWithEmptyStringAndNull() {
 JsonPointer pointer = new JsonPointer("");
 JsonPointer expected = JsonPointer.EMPTY;
 assertEquals(expected, pointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidInput() {
 String input = "invalidInput";
 JsonPointer pointer = new JsonPointer(input, "", null);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test
 public void testConstructorWithValidInput() {
 String input = "/a/b/c/d";
 JsonPointer pointer = new JsonPointer(input, "a", new JsonPointer("/b/c/d"));
 assertEquals("/a", pointer.toString());
 }

 @Test
 public void testMatches() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer3 = new JsonPointer("/a/b/e/d");
 assertTrue(pointer1.matches());
 assertFalse(pointer2.matches());
 assertFalse(pointer3.matches());
 }

 @Test
 public void testGetMatchingProperty() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/b/e/d");
 assertEquals("b", pointer1.getMatchingProperty());
 assertEquals(null, pointer2.getMatchingProperty());
 }

 @Test
 public void testGetMatchingIndex() {
 JsonPointer pointer1 = new JsonPointer("/a/0/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/1/c/d");
 JsonPointer pointer3 = new JsonPointer("/a/b/c/d");
 assertEquals(0, pointer1.getMatchingIndex());
 assertEquals(1, pointer2.getMatchingIndex());
 assertEquals(-1, pointer3.getMatchingIndex());
 }

 @Test
 public void testMayMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/b/e/d");
 assertTrue(pointer1.mayMatchProperty());
 assertFalse(pointer2.mayMatchProperty());
 }

 @Test
 public void testMayMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/a/0/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/1/c/d");
 JsonPointer pointer3 = new JsonPointer("/a/b/c/d");
 assertTrue(pointer1.mayMatchElement());
 assertTrue(pointer2.mayMatchElement());
 assertFalse(pointer3.mayMatchElement());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/b/e/d");
 JsonPointer pointer3 = new JsonPointer("/a/b/c/d");
 JsonPointer expected = new JsonPointer("/c/d");
 assertEquals(expected, pointer1.matchProperty("b"));
 assertNull(pointer2.matchProperty("b"));
 assertEquals(pointer3, pointer3.matchProperty("b"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/a/0/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/1/c/d");
 JsonPointer pointer3 = new JsonPointer("/a/b/c/d");
 JsonPointer expected = new JsonPointer("/c/d");
 assertEquals(expected, pointer1.matchElement(0));
 assertEquals(expected, pointer2.matchElement(1));
 assertNull(pointer3.matchElement(0));
 }

 @Test
 public void testTail() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 JsonPointer expected = new JsonPointer("/b/c/d");
 assertEquals(expected, pointer1.tail());
 }

 @Test
 public void testToString() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 String expected = "/a/b/c/d";
 assertEquals(expected, pointer1.toString());
 }

 @Test
 public void testHashCode() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer3 = new JsonPointer("/a/b/e/d");
 int expected1 = pointer1.hashCode();
 int expected2 = pointer1.hashCode();
 int expected3 = pointer3.hashCode();
 assertEquals(expected1, expected2);
 assertNotEquals(expected1, expected3);
 }

 @Test
 public void testEquals() {
 JsonPointer pointer1 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer2 = new JsonPointer("/a/b/c/d");
 JsonPointer pointer3 = new JsonPointer("/a/b/e/d");
 assertTrue(pointer1.equals(pointer2));
 assertFalse(pointer1.equals(pointer3));
 }
}