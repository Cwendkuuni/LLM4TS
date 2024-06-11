import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.CharSequenceUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private LookupTranslator lookupTranslator;

 @Test
 public void testLookupTranslatorWithEmptyLookupTable() {
 lookupTranslator = new LookupTranslator(new CharSequence[][]{});
 String input = "This is an empty lookup table test.";
 CharSequence expectedOutput = "This is an empty lookup table test.";
 String actualOutput = translateAndGetOutput(input);
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test
 public void testLookupTranslatorWithValidLookupTable() {
 CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
 lookupTranslator = new LookupTranslator(lookup);
 String input = "hello world";
 CharSequence expectedOutput = "hi planet";
 String actualOutput = translateAndGetOutput(input);
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLookupTranslatorWithNullInput() {
 CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
 lookupTranslator = new LookupTranslator(lookup);
 String input = null;
 try {
 translateAndGetOutput(input);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLookupTranslatorWithIndexOutOfBounds() {
 CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
 lookupTranslator = new LookupTranslator(lookup);
 String input = "helloworld";
 try {
 translateAndGetOutput(input);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testLookupTranslatorWithShortestSequence() {
 CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
 lookupTranslator = new LookupTranslator(lookup);
 String input = "hello";
 CharSequence expectedOutput = "hi";
 String actualOutput = translateAndGetOutput(input);
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test
 public void testLookupTranslatorWithLongestSequence() {
 CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}};
 lookupTranslator = new LookupTranslator(lookup);
 String input = "this is a very very very long input string with world in it";
 CharSequence expectedOutput = "this is a very very very long input string with planet in it";
 String actualOutput = translateAndGetOutput(input);
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 @Test
 public void testLookupTranslatorWithMultipleSequencesInInput() {
 CharSequence[][] lookup = {{"hello", "hi"}, {"world", "planet"}, {"java", "javaFan"}};
 lookupTranslator = new LookupTranslator(lookup);
 String input = "hello world java";
 CharSequence expectedOutput = "hi planet javaFan";
 String actualOutput = translateAndGetOutput(input);
 assertEquals(expectedOutput.toString(), actualOutput);
 }

 private String translateAndGetOutput(String input) {
 StringBuilder output = new StringBuilder();
 try {
 lookupTranslator.translate(CharSequence.class.cast(input), 0, new StringBuilder(output));
 } catch (IOException e) {
 fail("Unexpected IOException");
 }
 return output.toString();
 }

}