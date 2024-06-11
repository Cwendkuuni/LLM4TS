import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.util.NumberInput;

public class JsonPointerTest {

 private JsonPointer emptyPointer;
 private JsonPointer simplePointer;
 private JsonPointer invalidPointer;

 @Before
 public void setup() {
 emptyPointer = JsonPointer.EMPTY;
 simplePointer = new JsonPointer("/path/to/property");
 invalidPointer = new JsonPointer("/path/to/invalid");
 }

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = emptyPointer;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = simplePointer;
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals("/path/to/property", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer pointer = JsonPointer.compile("invalid");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseTail() {
 String input = "/path/to/property/~1/~2/~3";
 JsonPointer pointer = JsonPointer._parseTail(input);
 assertEquals("property/~1/~2/~3", pointer.toString());
 }

 @Test
 public void testParseQuotedTail() {
 String input = "/path/to/property/~/~0~1~2";
 JsonPointer pointer = JsonPointer._parseQuotedTail(input, 0);
 assertEquals("property/~0~1~2", pointer.toString());
 }

 @Test
 public void testHashCodeAndEquals() {
 JsonPointer pointer1 = new JsonPointer("/path/to/property");
 JsonPointer pointer2 = new JsonPointer("/path/to/property");
 JsonPointer pointer3 = new JsonPointer("/path/to/element");
 assertEquals(pointer1, pointer2);
 assertNotEquals(pointer1, pointer3);
 }
}