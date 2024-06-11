import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private Soundex soundex = new Soundex();

 @Test
 public void testEncodeString() {
 String input = "Apple";
 String expected = "A113";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String expected = "";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeNullString() {
 String input = null;
 try {
 String result = soundex.encode(input);
 fail("Expected EncoderException for null input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testEncodeUpperCaseString() {
 String input = "Apple";
 String expected = "A113";
 String result = soundex.encode(input.toUpperCase());
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDigits() {
 String input = "Apple123";
 String expected = "A113";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithSpecialCharacters() {
 String input = "Apple#$%&";
 String expected = "A113";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithMultipleSameLettersInARow() {
 String input = "AppleApple";
 String expected = "A113A113";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDoubleLetterAtStart() {
 String input = "Appple";
 String expected = "A113";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDoubleLetterInMiddle() {
 String input = "Apppple";
 String expected = "A113";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDoubleLetterAtEnd() {
 String input = "AppleApp";
 String expected = "A113";
 String result = soundex.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithMultipleCodes() {
 String input1 = "Apple";
 String input2 = "Banana";
 String expected1 = "A113";
 String expected2 = "B161";
 String result1 = soundex.encode(input1);
 String result2 = soundex.encode(input2);
 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeStringWithUnknownCharacter() {
 String input = "Apple\u0304";
 String result = soundex.encode(input);
 fail("Expected EncoderException for unknown character");
 }

 @Test
 public void testDifference() {
 String str1 = "Apple";
 String str2 = "Apple";
 int difference = soundex.difference(str1, str2);
 assertEquals(4, difference);

 String str3 = "Apple";
 String str4 = "Banana";
 difference = soundex.difference(str3, str4);
 assertEquals(0, difference);
 }
}