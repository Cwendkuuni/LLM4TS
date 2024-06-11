import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());

 pointer = pointer.matchProperty("foo");
 assertNotNull(pointer);
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testPropertyPointer() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals(input, pointer.toString());

 pointer = pointer.matchProperty("foo");
 assertNotNull(pointer);
 assertEquals("/foo", pointer.toString());
 pointer = pointer.matchProperty("bar");
 assertNotNull(pointer);
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "bar";
 // this should throw an exception
 JsonPointer.compile(input);
 }

 @Test
 public void testIndexPointer() {
 String input = "/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNotNull(pointer.getMatchingIndex());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());

 pointer = pointer.matchElement(0);
 assertNotNull(pointer);
 assertEquals("/0", pointer.toString());
 }

 @Test
 public void testMultiIndexPointer() {
 String input = "/1/2";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNotNull(pointer.getMatchingIndex());
 assertEquals(2, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());

 pointer = pointer.matchElement(1);
 assertNotNull(pointer);
 assertEquals("/1", pointer.toString());
 pointer = pointer.matchElement(2);
 assertNotNull(pointer);
 assertEquals("/1/2", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndex() {
 String input = "/-1";
 JsonPointer.compile(input);
 }

 @Test
 public void testLongPointer() {
 String input = "/foo/bar/baz/qux/quux/corge";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals(input, pointer.toString());

 pointer = pointer.matchProperty("foo");
 assertNotNull(pointer);
 assertEquals("/foo", pointer.toString());
 pointer = pointer.matchProperty("bar");
 assertNotNull(pointer);
 assertEquals("/foo/bar", pointer.toString());
 pointer = pointer.matchProperty("baz");
 assertNotNull(pointer);
 assertEquals("/foo/bar/baz", pointer.toString());
 pointer = pointer.matchProperty("qux");
 assertNotNull(pointer);
 assertEquals("/foo/bar/baz/qux", pointer.toString());
 pointer = pointer.matchProperty("quux");
 assertNotNull(pointer);
 assertEquals("/foo/bar/baz/qux/quux", pointer.toString());
 pointer = pointer.matchProperty("corge");
 assertNotNull(pointer);
 assertEquals("/foo/bar/baz/qux/quux/corge", pointer.toString());
 }

 @Test
 public void testCompoundPointer() {
 String input = "/foo/[0]/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals(input, pointer.toString());

 pointer = pointer.matchProperty("foo");
 assertNotNull(pointer);
 assertEquals("/foo", pointer.toString());
 pointer = pointer.matchElement(0);
 assertNotNull(pointer);
 assertEquals("/foo/0", pointer.toString());
 pointer = pointer.matchProperty("bar");
 assertNotNull(pointer);
 assertEquals("/foo/0/bar", pointer.toString());
 }

 @Test
 public void testEscapedPointer() {
 String input = "/foo/bar~1/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals(input, pointer.toString());

 pointer = pointer.matchProperty("foo");
 assertNotNull(pointer);
 assertEquals("/foo", pointer.toString());
 pointer = pointer.matchProperty("bar~1");
 assertNotNull(pointer);
 assertEquals("/foo/bar~1", pointer.toString());
 pointer = pointer.matchProperty("baz");
 assertNotNull(pointer);
 assertEquals("/foo/bar~1/baz", pointer.toString());
 }
}