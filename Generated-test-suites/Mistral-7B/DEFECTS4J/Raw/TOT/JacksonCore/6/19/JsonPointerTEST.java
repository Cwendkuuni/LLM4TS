import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testBasicPointer() {
 JsonPointer pointer = JsonPointer.compile("/test/property");
 assertTrue(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/test/property", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer pointer = JsonPointer.compile("/test[1]/property");
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals(1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("/test/[1]/property", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("testproperty");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPointerWithSlashes() {
 JsonPointer pointer = JsonPointer.compile("/test/property1/property2");
 assertTrue(pointer.matches());
 assertEquals("property1", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/test/property1/property2", pointer.toString());
 }

 @Test
 public void testPointerWithQuotes() {
 JsonPointer pointer = JsonPointer.compile("/test/property~1/\'property2\'");
 assertTrue(pointer.matches());
 assertEquals("property1", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/test/property~1/\'property2\'", pointer.toString());
 }

 @Test
 public void testPointerWithTail() {
 JsonPointer pointer = JsonPointer.compile("/test/property/element");
 assertTrue(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 JsonPointer tail = pointer.tail();
 assertFalse(tail.matches());
 assertEquals("element", tail.getMatchingProperty());
 assertTrue(tail.mayMatchElement());
 assertEquals(0, tail.getMatchingIndex());
 assertNotNull(tail.tail());
 assertEquals("/test/property", pointer.toString());
 assertEquals("/test/property/element", pointer.toString());
 }
}