import org.apache.commons.codec.encoder.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

 private Soundex usEnglishSoundex = Soundex.US_ENGLISH;

 @Test
 public void testUSEnglishSoundex() {
 String input = "Apple";
 String expected = "A111";
 String result = usEnglishSoundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testUSEnglishSoundexEmptyString() {
 String input = "";
 String expected = "";
 String result = usEnglishSoundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testUSEnglishSoundexNullString() {
 String input = null;
 usEnglishSoundex.encode(input);
 }

 @Test
 public void testUSEnglishSoundexWithDigits() {
 String input = "Apple123";
 String expected = "A111";
 String result = usEnglishSoundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testUSEnglishSoundexWithMultipleWords() {
 String input = "Apple Sauce";
 String expected = "A111 S111";
 String result = usEnglishSoundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testUSEnglishSoundexWithMultipleCodes() {
 String input = "Apple Sauce Apple";
 String expected = "A111 S111 A111";
 String result = usEnglishSoundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCustomSoundexMapping() {
 char[] customMapping = new char[]{'0', '1', '1', '2', '1', '2', '2', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
 Soundex customSoundex = new Soundex(customMapping);
 String input = "Apple";
 String expected = "A111";
 String result = customSoundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCustomSoundexMappingWithInvalidCharacter() {
 char[] customMapping = new char[]{'0', '1', '1', '2', '1', '2', '2', '1', '1', '1', '1', '1', '1', '1', '1', 'z'}; // z is not a valid character
 Soundex customSoundex = new Soundex(customMapping);
 String input = "Apple";
 String result = customSoundex.encode(input);
 }

}