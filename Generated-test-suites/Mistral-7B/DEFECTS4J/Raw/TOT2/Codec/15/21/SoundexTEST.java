import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private final Soundex soundexInstance = new Soundex();

 @Test
 public void testEncodeString() {
 String originalString = "Apple";
 String expectedSoundexCode = "A133";
 String actualSoundexCode = soundexInstance.encode(originalString);
 assertEquals(expectedSoundexCode, actualSoundexCode);
 }

 @Test
 public void testEncodeEmptyString() {
 String actualSoundexCode = soundexInstance.encode("");
 assertEquals("", actualSoundexCode);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNullString() {
 soundexInstance.encode(null);
 }

 @Test
 public void testEncodeMultipleStringsWithSameCharacter() {
 String str1 = "Apple";
 String str2 = "Orange";
 String str3 = "AppleApple";
 String str4 = "AppleAppleApple";
 String expectedSoundexCode1 = "A133";
 String expectedSoundexCode2 = "O250";
 String expectedSoundexCode3 = "A133A133";
 String expectedSoundexCode4 = "A133A133A133";
 String actualSoundexCode1 = soundexInstance.encode(str1);
 String actualSoundexCode2 = soundexInstance.encode(str2);
 String actualSoundexCode3 = soundexInstance.encode(str3);
 String actualSoundexCode4 = soundexInstance.encode(str4);
 assertEquals(expectedSoundexCode1, actualSoundexCode1);
 assertEquals(expectedSoundexCode2, actualSoundexCode2);
 assertEquals(expectedSoundexCode3, actualSoundexCode3);
 assertEquals(expectedSoundexCode4, actualSoundexCode4);
 }

 @Test
 public void testEncodeMultipleStringsWithSameSoundexCode() {
 String str1 = "Banana";
 String str2 = "Apple";
 String expectedSoundexCode = "B161"; // or A133 in this case since they have the same soundex code
 String actualSoundexCode1 = soundexInstance.encode(str1);
 String actualSoundexCode2 = soundexInstance.encode(str2);
 assertEquals(expectedSoundexCode, actualSoundexCode1);
 assertEquals(expectedSoundexCode, actualSoundexCode2);
 }

 @Test
 public void testEncodeStringWithDigits() {
 String originalString = "Apple123";
 String expectedSoundexCode = "A133";
 String actualSoundexCode = soundexInstance.encode(originalString);
 assertEquals(expectedSoundexCode, actualSoundexCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeStringWithNonAlphabets() {
 char nonAlphabetChar = '!';
 String originalString = Character.toString(nonAlphabetChar);
 soundexInstance.encode(originalString);
 }
}