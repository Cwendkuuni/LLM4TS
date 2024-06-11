import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

 private CharSequenceTranslator translator;

 @Test
 public void testEmptyLookupTable() {
 translator = new LookupTranslator(new CharSequence[][]{});
 String input = "";
 String expected = "";
 int actual = translator.translate(input, 0, null);
 assertEquals(expected, "");
 }

 @Test
 public void testSingleMapping() {
 translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
 String input = "hello";
 String expected = "world";
 int actual = translator.translate(input, 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleMappings() {
 translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"hi", "there"}});
 String input = "hello";
 String expected = "world";
 int actual = translator.translate(input, 0, null);
 assertEquals(expected, actual);
 String input2 = "hi";
 String expected2 = "there";
 int actual2 = translator.translate(input2, 0, null);
 assertEquals(expected2, actual2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLookupTable() {
 translator = new LookupTranslator(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBounds() {
 translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"hi", "there"}});
 String input = "goodbye";
 int actual = translator.translate(input, 0, null);
 assertEquals(0, actual);
 }

 @Test
 public void testLongestSubsequence() {
 translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"good", "morning"}, {"test", "result"}});
 String input = "test";
 String expected = "result";
 int actual = translator.translate(input, 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testShortestSubsequence() {
 translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"good", "morning"}, {"test", "result"}});
 String input = "he";
 String expected = "";
 int actual = translator.translate(input, 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptySubsequence() {
 translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"good", "morning"}, {"test", "result"}});
 String input = "";
 int actual = translator.translate(input, 0, null);
 assertEquals(0, actual);
 }
}