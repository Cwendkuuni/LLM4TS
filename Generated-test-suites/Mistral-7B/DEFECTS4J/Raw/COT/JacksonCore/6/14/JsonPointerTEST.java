import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonPointerTest {

 private JsonPointer emptyPointer;
 private JsonPointer simplePointer;
 private JsonPointer complexPointer;

 @Before
 public void setup() {
 emptyPointer = JsonPointer.EMPTY;
 simplePointer = new JsonPointer("/test/property");
 complexPointer = new JsonPointer("/test/0/property/1/property");
 }

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = emptyPointer;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = simplePointer;
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testComplexPointer() {
 JsonPointer pointer = complexPointer;
 assertFalse(pointer.matches());
 assertEquals("property", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertTrue(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = JsonPointer.compile("test");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParseQuotedTail() throws JsonProcessingException {
 String input = "~/test1/~0~1/test2";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/test1/test2", pointer.toString());
 }

 @Test
 public void testTailRecursion() {
 JsonPointer pointer = new JsonPointer("/test/0/property/1/property");
 JsonPointer expected = new JsonPointer("/test/property");
 JsonPointer actual = pointer.tail();
 while (!actual.matches()) {
 assertEquals(expected, actual);
 expected = expected.tail();
 actual = actual.tail();
 }
 assertEquals(expected, actual);
 }

}