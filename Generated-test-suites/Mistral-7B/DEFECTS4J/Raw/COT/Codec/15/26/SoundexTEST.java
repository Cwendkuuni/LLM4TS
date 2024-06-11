import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

 private Soundex usEnglishSoundex = Soundex.US_ENGLISH;

 @Test
 public void testSoundexUSEnglish() {
 String input = "Apple";
 String expected = "A120";
 String actual = usEnglishSoundex.soundex(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testSoundexEmptyString() {
 String input = "";
 String expected = "";
 String actual = usEnglishSoundex.soundex(input);
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testSoundexNullInput() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String input = null;
 String expected = null;
 String actual = soundex.soundex(input);
 Assert.assertNull(actual);
 Failure exception = assertThrows(EncoderException.class, () -> soundex.soundex(input));
 String errorMessage = exception.getMessage();
 assertEquals("Parameter supplied to Soundex encode is not of type java.lang.String", errorMessage);
 }

 @Test
 public void testGetMappingCodeHwRule() {
 String input = "What's up";
 char expectedFirstCode = getExpectedMappingCode('W', input);
 char actualFirstCode = usEnglishSoundex.getMappingCode(input, 0);
 assertEquals(expectedFirstCode, actualFirstCode);
 char expectedLastCode = getExpectedMappingCode('p', input);
 char actualLastCode = usEnglishSoundex.getMappingCode(input, input.length() - 1);
 assertEquals(expectedLastCode, actualLastCode);
 }

 private char getExpectedMappingCode(char ch, String input) {
 char[] soundexMapping = Soundex.US_ENGLISH_MAPPING;
 int index = ch - 'A';
 char mappedChar = soundexMapping[index];
 if (index > 1 && 'H' == input.charAt(index - 2) || 'W' == input.charAt(index - 2)) {
 if ('H' == input.charAt(index - 3) || 'W' == input.charAt(index - 3)) {
 return soundexMapping[index - 3];
 }
 }
 return mappedChar;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeInvalidCharacter() {
 char invalidChar = '\u0001';
 String input = "Apple";
 String errorMessage = "";
 try {
 usEnglishSoundex.getMappingCode(input, 0);
 } catch (IllegalArgumentException e) {
 errorMessage = e.getMessage();
 }
 assertTrue(errorMessage.contains("The character is not mapped: " + invalidChar));
 }

}