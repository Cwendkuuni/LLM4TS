import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 private static JsonPointer createJsonPointer(String input) {
 try {
 return JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // handle error cases
 return null;
 }
 }

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 Assert.assertTrue(emptyPointer.matches());
 Assert.assertNull(emptyPointer.getMatchingProperty());
 Assert.assertEquals(-1, emptyPointer.getMatchingIndex());
 Assert.assertFalse(emptyPointer.mayMatchProperty());
 Assert.assertFalse(emptyPointer.mayMatchElement());
 Assert.assertNull(emptyPointer.tail());
 }

 @Test
 public void testSimplePointer() {
 String input = "/test/property";
 JsonPointer pointer = createJsonPointer(input);
 Assert.assertFalse(pointer.matches());
 Assert.assertEquals("test", pointer.getMatchingProperty());
 Assert.assertEquals(-1, pointer.getMatchingIndex());
 Assert.assertTrue(pointer.mayMatchProperty());
 Assert.assertFalse(pointer.mayMatchElement());
 JsonPointer tail = pointer.tail();
 Assert.assertNotNull(tail);
 Assert.assertEquals("/test", tail.toString());
 }

 @Test
 public void testPointerWithIndex() {
 String input = "/test/0";
 JsonPointer pointer = createJsonPointer(input);
 Assert.assertFalse(pointer.matches());
 Assert.assertEquals("test", pointer.getMatchingProperty());
 Assert.assertEquals(0, pointer.getMatchingIndex());
 Assert.assertTrue(pointer.mayMatchProperty());
 Assert.assertTrue(pointer.mayMatchElement());
 JsonPointer tail = pointer.tail();
 Assert.assertNull(tail);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerInput() {
 String input = "test";
 JsonPointer pointer = createJsonPointer(input);
 Assert.fail("IllegalArgumentException should be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPointerWithNonStartingSlash() {
 String input = "test";
 JsonPointer pointer = createJsonPointer(input);
 Assert.fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testParseTailRecursive() {
 String input = "/test/property/element/~2/element/~1/element";
 JsonPointer pointer = createJsonPointer(input);
 Assert.assertFalse(pointer.matches());
 Assert.assertEquals("test/property/element", pointer.getMatchingProperty());
 Assert.assertEquals(-1, pointer.getMatchingIndex());
 Assert.assertTrue(pointer.mayMatchProperty());
 Assert.assertFalse(pointer.mayMatchElement());
 JsonPointer tail = pointer.tail();
 Assert.assertEquals("/property/element", tail.toString());
 JsonPointer tail2 = tail.tail();
 Assert.assertEquals("/element", tail2.toString());
 JsonPointer tail3 = tail2.tail();
 Assert.assertEquals(EMPTY, tail3);
 }

}