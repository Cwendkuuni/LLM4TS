import org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private JsonPointer emptyPointer;
 private JsonPointer simplePointer;
 private JsonPointer tailPointer;

 @Before
 public void setup() {
 emptyPointer = JsonPointer.EMPTY;
 simplePointer = new JsonPointer("/test/property");
 tailPointer = new JsonPointer("/test/property/0");
 }

 @Test
 public void testEmptyPointer() {
 JsonPointer empty = JsonPointer.EMPTY;

 assertEquals(empty, emptyPointer);
 assertTrue(empty.matches());
 assertNull(empty.getMatchingProperty());
 assertFalse(empty.mayMatchProperty());
 assertFalse(empty.mayMatchElement());
 assertNull(empty.tail());
 assertEquals("", empty.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = simplePointer;

 assertEquals(simplePointer, pointer);
 assertFalse(pointer.matches());
 assertEquals("test", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/test/property", pointer.toString());
 }

 @Test
 public void testTailPointer() {
 JsonPointer pointer = tailPointer;

 assertEquals(tailPointer, pointer);
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertTrue(pointer.mayMatchIndex(0));
 assertEquals(0, pointer.getMatchingIndex());
 assertEquals("/test/property/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = JsonPointer.compile("test");
 fail("Expected IllegalArgumentException for invalid input");
 }

 @Test
 public void testMatchProperty() {
 JsonPointer matchingPointer = new JsonPointer("/test/property/property");
 JsonPointer pointer = simplePointer;

 JsonPointer result = pointer.matchProperty("property");

 assertEquals(matchingPointer, result);
 }

 @Test
 public void testMatchElement() {
 JsonPointer matchingPointer = new JsonPointer("/test/property/0");
 JsonPointer pointer = tailPointer;

 JsonPointer result = pointer.matchElement(0);

 assertEquals(matchingPointer, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointer() {
 JsonPointer pointer = new JsonPointer(null, null, null);
 fail("Expected NullPointerException for null input");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNegativeIndex() {
 JsonPointer pointer = new JsonPointer("/test/property/-1");
 fail("Expected IndexOutOfBoundsException for negative index");
 }

 @Test
 public void testValidInput() {
 JsonPointer pointer = JsonPointer.compile("/test/property/0");

 assertEquals(pointer, JsonPointer.valueOf("/test/property/0"));
 assertEquals("/test/property/0", pointer.toString());
assertEquals(3, pointer.size());

 JsonPointer subPointer = pointer.matchProperty("property");
 JsonPointer subSubPointer = subPointer.matchElement(0);
 assertEquals(subSubPointer, pointer.match("/property/0"));
}

 @Test
 public void testParseTail() {
 JsonPointer pointer = JsonPointer._parseTail("/test/property/~1~2~3");

 assertEquals("/test/property/1/2/3", pointer.toString());
 }

 @Test
 public void testParseQuotedTail() {
 JsonPointer pointer = JsonPointer._parseQuotedTail("/test/property/~/1/2/3", 2);

 assertEquals("/test/property/1/2/3", pointer.toString());
 }
}