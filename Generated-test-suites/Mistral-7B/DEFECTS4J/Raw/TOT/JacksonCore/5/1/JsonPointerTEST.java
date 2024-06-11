import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertNull(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertNull(emptyPointer.getMatchingIndex());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 String pointerString = "/test/property";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertNotNull(pointer);
 assertNull(pointer.matches());
 assertNotNull(pointer.getMatchingProperty());
 assertEquals("test", pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertEquals(pointerString, pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 String pointerString = "/test/0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertNotNull(pointer);
 assertNull(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertEquals(pointerString, pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String invalidPointerString = "test/property";
 JsonPointer.compile(invalidPointerString);
 }

 @Test
 public void testMatchingPointer() {
 JsonPointer testPointer = new JsonPointer("/test/0/property", "/test/0/property", JsonPointer.EMPTY);
 assertNull(testPointer.matches());
 assertNotNull(testPointer.getMatchingProperty());
 assertEquals("property", testPointer.getMatchingProperty());
 assertNull(testPointer.getMatchingIndex());
 assertEquals("/test/0/property", testPointer.toString());
 }

 @Test
 public void testMatches() {
 JsonPointer pointer1 = new JsonPointer("/test/0/property", "/test/0/property", JsonPointer.EMPTY);
 JsonPointer pointer2 = new JsonPointer("/test/property", "/test/0/property", JsonPointer.EMPTY);
 JsonPointer pointer3 = new JsonPointer("/test/0/property", "/test/property", JsonPointer.EMPTY);
 JsonPointer pointer4 = new JsonPointer("/test/0/property", "/test/0/property2", JsonPointer.EMPTY);

 assertFalse(pointer1.matches());
 assertFalse(pointer2.matches());
 assertTrue(pointer3.matches());
 assertFalse(pointer4.matches());
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer = new JsonPointer("/test/0/property", "/test/0/property", JsonPointer.EMPTY);
 assertNull(pointer.matchProperty(null));
 assertNull(pointer.matchProperty(""));
 assertNull(pointer.matchProperty("test"));
 assertNull(pointer.matchProperty("test/property2"));
 assertNotNull(pointer.matchProperty("test/property"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer = new JsonPointer("/test/0/property/0", "/test/0/property/0", JsonPointer.EMPTY);
 assertNull(pointer.matchElement(-1));
 assertNull(pointer.matchElement(1));
 assertNotNull(pointer.matchElement(0));
 }

 @Test
 public void testTail() {
 JsonPointer pointer = new JsonPointer("/test/0/property/0", "/test/0/property", JsonPointer.EMPTY);
 assertNotNull(pointer.tail());
 }
}