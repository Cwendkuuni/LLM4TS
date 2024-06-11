import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.CharSequence;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private LookupTranslator lookupTranslator;

 @Test
 public void testLookupTranslatorConstructor() {
 CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple pie"}, {"banana", "banana bread"}};
 lookupTranslator = new LookupTranslator(lookup);
 }

 @Test
 public void testTranslateMethodWithValidInput() throws Exception {
 String input = "applebanana";
 int expected = 7;
 int actual = lookupTranslator.translate(new String(input), 0, new StringWriter());
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateMethodWithEmptyInput() throws Exception {
 String input = "";
 int expected = 0;
 int actual = lookupTranslator.translate(new String(input), 0, new StringWriter());
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMethodWithNullInput() throws Exception {
 String input = null;
 lookupTranslator.translate(input, 0, new StringWriter());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateMethodWithNegativeIndex() throws Exception {
 String input = "applebanana";
 lookupTranslator.translate(new String(input), -1, new StringWriter());
 }

 @Test(expected = IOException.class)
 public void testTranslateMethodWithNullWriter() throws Exception {
 String input = "applebanana";
 lookupTranslator.translate(new String(input), 0, null);
 }

 @Test(expected = IOException.class)
 public void testTranslateMethodWithNullWriter2() throws Exception {
 Writer writer = null;
 lookupTranslator.translate(new String("apple"), 0, writer);
 }

 @Test
 public void testTranslateMethodWithInputBiggerThanLookupTable() throws Exception {
 String input = "applebananapear";
 int expected = 7; // applebanana
 int actual = lookupTranslator.translate(new String(input), 0, new StringWriter());
 assertEquals(expected, actual);

 String input2 = "applebananapearkiwi";
 int indexOfexpected = 11; // pearkiwi
 int actualIndex = lookupTranslator.translate(new String(input2), 0, new StringWriter()) + 7;
 assertEquals(indexOfexpected, actualIndex);
 }

 @Test
 public void testTranslateMethodWithInputSmallerThanLookupTable() throws Exception {
 String input = "apple";
 int expected = 5; // apple
 int actual = lookupTranslator.translate(new String(input), 0, new StringWriter());
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateMethodWithInputSubsequenceMatchedInLookupTable() throws Exception {
 String input = "banana";
 int expected = 6; // banana bread
 int actual = lookupTranslator.translate(new String(input), 0, new StringWriter());
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateMethodWithInputSubsequenceNotMatchedInLookupTable() throws Exception {
 String input = "cherry";
 int expected = 0;
 int actual = lookupTranslator.translate(new String(input), 0, new StringWriter());
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateMethodWithInputSubsequenceMatchedInMiddleOfLookupTable() throws Exception {
 String input = "banberry";
 int expected = 7; // banberry
 int actual = lookupTranslator.translate(new String(input), 0, new StringWriter());
 assertEquals(expected, actual);
 }
}