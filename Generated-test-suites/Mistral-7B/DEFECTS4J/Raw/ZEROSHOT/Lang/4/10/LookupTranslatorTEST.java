import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private LookupTranslator translator;

 @Test
 public void testEmptyLookupTable() {
 translator = new LookupTranslator((CharSequence[][]) null);
 String input = "";
 int expected = 0;
 int actual = translator.translate(new String(input), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testSingleKeySingleValueLookupTable() {
 translator = new LookupTranslator(new CharSequence[][]{{"test", "result"}});
 String input = "test";
 int expected = 4;
 int actual = translator.translate(new String(input), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testSingleKeyMultipleValuesLookupTable() {
 translator = new LookupTranslator(new CharSequence[][]{{"test", "result1"}, {"test", "result2"}});
 String input = "test";
 int expected = 6;
 int actual = translator.translate(new String(input), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleKeysSingleValueLookupTable() {
 translator = new LookupTranslator(new CharSequence[][]{{"test1", "result"}, {"test2", "result"}});
 String input = "test1";
 String expected = "result";
 String actual = translator.translate(new String(input), 0, new StringBuilder()).toString();
 assertEquals(expected, actual);
 input = "test2";
 actual = translator.translate(new String(input), 0, new StringBuilder()).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleKeysMultipleValuesLookupTable() {
 translator = new LookupTranslator(new CharSequence[][]{{"test1", "result1"}, {"test2", "result2"}});
 String input = "test1";
 String expected = "result1";
 String actual = translator.translate(new String(input), 0, new StringBuilder()).toString();
 assertEquals(expected, actual);
 input = "test2";
 expected = "result2";
 actual = translator.translate(new String(input), 0, new StringBuilder()).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testKeyBeyondLookupTableBounds() {
 translator = new LookupTranslator(new CharSequence[][]{{"test", "result"}});
 String input = "test_longer";
 int expected = 0;
 int actual = translator.translate(new String(input), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testKeyTooShort() {
 translator = new LookupTranslator(new CharSequence[][]{{"test", "result"}});
 String input = "te";
 int expected = 0;
 int actual = translator.translate(new String(input), 0, null);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 translator = new LookupTranslator(new CharSequence[][]{{null, "result"}});
 String input = "test";
 int index = 0;
 try {
 translator.translate(new String(input), index, null);
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValue() {
 translator = new LookupTranslator(new CharSequence[][]{{"test", null}});
 String input = "test";
 int index = 0;
 try {
 translator.translate(new String(input), index, null);
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testLookupTableWithLongestKeyLengthGreaterThanInputSize() {
 translator = new LookupTranslator(new CharSequence[][]{{"test_longer_than_input", "result"}});
 String input = "test";
 int expected = 0;
 int actual = translator.translate(new String(input), 0, null);
 assertEquals(expected, actual);
 }

 @Test
 public void testLookupTableWithShortestKeyLengthLessThanInputMinusIndex() {
 translator = new LookupTranslator(new CharSequence[][]{{"tes", "result"}}); // shortest key length is 3
 String input = "testest";
 int index = 2;
 int expected = 3;
 int actual = translator.translate(new String(input), index, null);
 assertEquals(expected, actual);
 }
}