import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private LookupTranslator translator;

 @Before
 public void setup() {
 CharSequence[][] lookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "success"}};
 translator = new LookupTranslator(lookup);
 }

 @Test
 public void testTranslationWithShortSequence() {
 CharSequence input = "hello";
 String expectedOutput = "hi";
 try {
 Writer out = new StringWriter();
 translator.translate(input, 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput);
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testTranslationWithLongestSequence() {
 CharSequence input = "world";
 String expectedOutput = "greetings";
 try {
 Writer out = new StringWriter();
 translator.translate(input, 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput);
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testTranslationWithNonExistentSequence() {
 CharSequence input = "test2";
 try {
 Writer out = new StringWriter();
 translator.translate(input, 0, out);
 fail("Expected translation failure, but translation succeeded.");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslationWithNullInput() {
 CharSequence input = null;
 try {
 Writer out = new StringWriter();
 translator.translate(input, 0, out);
 fail("Expected exception: null input.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslationWithInputBeyondSequenceLength() {
 CharSequence input = "hello world";
 try {
 Writer out = new StringWriter();
 translator.translate(input, 6, out);
 fail("Expected index out of bounds exception, but translation succeeded.");
 } catch (IOException e) {
 // expected
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testTranslationWithEmptyInput() {
 CharSequence input = "";
 String expectedOutput = "";
 try {
 Writer out = new StringWriter();
 translator.translate(input, 0, out);
 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput);
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}