import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

 @Test
 public void testUS_ENGLISH_MAPPING() {
 char[] usEnglishMapping = Soundex.US_ENGLISH_MAPPING;
 assertEquals('0', usEnglishMapping['A']);
 assertEquals('1', usEnglishMapping['B']);
 assertEquals('0', usEnglishMapping['C']);
 // ... continue for all letters in the alphabet
 }

 @Test
 public void testDefaultSoundex() {
 Soundex soundex = Soundex.US_ENGLISH;
 String str1 = "Apple";
 String str2 = "Orange";
 String soundexCode1 = soundex.soundex(str1);
 String soundexCode2 = soundex.soundex(str2);
 int difference = soundex.difference(str1, str2);

 assertEquals("A120", soundexCode1);
 assertEquals("A123", soundexCode2);
 assertEquals(3, difference);
 }

 @Test
 public void testCustomSoundexMapping() {
 char[] customMapping = {'1', '2', '0', '2', '1', '0', '1', '2', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1'};
 Soundex customSoundex = new Soundex(customMapping);
 String str1 = "Apple";
 String str2 = "Orange";
 String soundexCode1 = customSoundex.soundex(str1);
 String soundexCode2 = customSoundex.soundex(str2);
 int difference = customSoundex.difference(str1, str2);

 assertEquals("A111111111111111111111111111111", soundexCode1);
 assertEquals("A111111111111111111111111111111", soundexCode2);
 assertEquals(4, difference);
 }
}