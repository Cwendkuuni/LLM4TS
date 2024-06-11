import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.encoder.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private final StringEncoder caverphoneEncoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String emptyString = "";
 String expectedResult = "1111111111";
 String actualResult = caverphoneEncoder.encode(emptyString);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testNullString() {
 String nullString = null;
 try {
 String actualResult = caverphoneEncoder.encode(nullString);
 } catch (EncoderException e) {
 String expectedErrorMessage = "Parameter supplied to Caverphone encode is null";
 Assert.assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testSingleLetterString() {
 String singleLetterString = "a";
 String expectedResult = "A";
 String actualResult = caverphoneEncoder.encode(singleLetterString);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testMultipleLettersString() {
 String multipleLettersString = "hello";
 String expectedResult = "H2L2O";
 String actualResult = caverphoneEncoder.encode(multipleLettersString);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testMultipleLettersWithDigitsString() {
 String multipleLettersWithDigitsString = "hello123";
 String expectedResult = "H2L2O123";
 String actualResult = caverphoneEncoder.encode(multipleLettersWithDigitsString);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String stringWithSpecialCharacters = "!@#$%&*()_+=-[{]};:<>?";
 String expectedErrorMessage = "Parameter supplied to Caverphone encode contains invalid characters";
 try {
 String actualResult = caverphoneEncoder.encode(stringWithSpecialCharacters);
 } catch (EncoderException e) {
 Assert.assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testStringWithUppercaseLetters() {
 String stringWithUppercaseLetters = "HeLLo";
 String expectedResult = "H2L2O";
 String actualResult = caverphoneEncoder.encode(stringWithUppercaseLetters);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testStringsWithSameCaverphoneCode() {
 String string1 = "hello";
 String string2 = "world";
 String expectedResult = "H2L2OWORLD";
 String actualResult = caverphoneEncoder.isCaverphoneEqual(string1, string2) ? caverphoneEncoder.encode(string1) : "";
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testStringsWithDifferentCaverphoneCode() {
 String string1 = "hello";
 String string2 = "world123";
 String actualResult = caverphoneEncoder.isCaverphoneEqual(string1, string2);
 Assert.assertFalse(actualResult);
 }
}