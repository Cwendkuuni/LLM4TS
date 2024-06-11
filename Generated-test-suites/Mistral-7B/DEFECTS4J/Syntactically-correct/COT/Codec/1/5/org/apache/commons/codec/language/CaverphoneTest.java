import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

 private Caverphone caverphoneInstance;

 @Before
 public void setUp() {
 caverphoneInstance = new Caverphone();
 }

 /**
 * Test method for {@link Caverphone#caverphone(String)}
 */
 @Test
 public void testCaverphone() {
 // Test positive case
 String input = "HelloWorld";
 String expectedOutput = "H2oW2rld";
 String actualOutput = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, actualOutput);

 // Test null input
 String nullInput = null;
 String expectedNullOutput = "1111111111";
 String actualNullOutput = caverphoneInstance.caverphone(nullInput);
 assertEquals(expectedNullOutput, actualNullOutput);

 // Test empty input
 String emptyInput = "";
 String expectedEmptyOutput = "1111111111";
 String actualEmptyOutput = caverphoneInstance.caverphone(emptyInput);
 assertEquals(expectedEmptyOutput, actualEmptyOutput);
 }

 /**
 * Test method for {@link Caverphone#encode(Object)}
 */
 @Test(expected = EncoderException.class)
 public void testEncodeWithInvalidObjectType() {
 Object invalidObject = new Object();
 caverphoneInstance.encode(invalidObject);
 }

 @Test
 public void testEncodeWithValidString() {
 String input = "HelloWorld";
 String expectedOutput = "H2oW2rld";
 String actualOutput = (String) caverphoneInstance.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 /**
 * Test method for {@link Caverphone#isCaverphoneEqual(String, String)}
 */
 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld";
 assertTrue(caverphoneInstance.isCaverphoneEqual(str1, str2));

 String str3 = "HelloWorld";
 String str4 = "HelloWorlD";
 assertFalse(caverphoneInstance.isCaverphoneEqual(str3, str4));
 }

 /**
 * Test edge cases
 */
 @Test
 public void testEdgeCases() {
 // Test lowercase string
 String lowercaseInput = "helloWorld";
 String lowercaseExpectedOutput = "h2oW2rld";
 String lowercaseActualOutput = caverphoneInstance.caverphone(lowercaseInput);
 assertEquals(lowercaseExpectedOutput, lowercaseActualOutput);

 // Test string with only digits
 String digitsInput = "1234567890";
 String digitsExpectedOutput = "1111110";
 String digitsActualOutput = caverphoneInstance.caverphone(digitsInput);
 assertEquals(digitsExpectedOutput, digitsActualOutput);

 // Test string with only vowels
 String vowelsInput = "aeiou";
 String vowelsExpectedOutput = "A11111";
 String vowelsActualOutput = caverphoneInstance.caverphone(vowelsInput);
 assertEquals(vowelsExpectedOutput, vowelsActualOutput);

 // Test string with only consonants
 String consonantsInput = "bcdfghjklmnpqrstvwxyz";
 String consonantsExpectedOutput = "B2C2F2G2H2J2K2L2M2N2P2Q2R2S2T2V2W2X2Y2Z2";
 String consonantsActualOutput = caverphoneInstance.caverphone(consonantsInput);
 assertEquals(consonantsExpectedOutput, consonantsActualOutput);

 // Test string with no vowels at the start
 String noVowelsStartInput = "bcdefghijklmnopqrstuvwxyz";
 String noVowelsStartExpectedOutput = "B2C2F2G2H2I2J2K2L2M2N2O2P2Q2R2S2T2U2V2W2X2Y2Z2";
 String noVowelsStartActualOutput = caverphoneInstance.caverphone(noVowelsStartInput);
 assertEquals(noVowelsStartExpectedOutput, noVowelsStartActualOutput);

 // Test string with only one character
 String oneCharacterInput = "a";
 String oneCharacterExpectedOutput = "A";
 String oneCharacterActualOutput = caverphoneInstance.caverphone(oneCharacterInput);
 assertEquals(oneCharacterExpectedOutput, oneCharacterActualOutput);

 // Test string with only whitespaces
 String whitespacesInput = " ";
 String whitespacesExpectedOutput = "A";
 String whitespacesActualOutput = caverphoneInstance.caverphone(whitespacesInput);
 assertEquals(whitespacesExpectedOutput, whitespacesActualOutput);

 // Test string with only one letter and one digit
 String oneLetterOneDigitInput = "a1";
 String oneLetterOneDigitExpectedOutput = "A1";
 String oneLetterOneDigitActualOutput = caverphoneInstance.caverphone(oneLetterOneDigitInput);
 assertEquals(oneLetterOneDigitExpectedOutput, oneLetterOneDigitActualOutput);
 }

 /**
 * Test exceptions
 */
 @Test(expected = EncoderException.class)
 public void testEncodeException() {
 String nullInput = null;
 caverphoneInstance.encode(nullInput);
 }
}