import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

 private final Soundex usEnglishSoundex = Soundex.US_ENGLISH;

 @Test
 public void testSoundexUSEnglishEncoding() {
 String input = "Apple";
 String expectedOutput = "A111";
 String output = usEnglishSoundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testSoundexEncodingWithInvalidInput() {
 Soundex soundex = new Soundex(new char[]{'A'});
 String input = "Apple";
 String output = soundex.soundex(input);
 }

 @Test
 public void testDifferenceMethod() {
 String str1 = "Apple";
 String str2 = "Appaloosa";
 int difference = usEnglishSoundex.difference(str1, str2);
 Assert.assertEquals(2, difference);
 }

 @Test
 public void testGetMappingCodeMethod() {
 String input = "Apple";
 char expectedMappingCode = 'P';
 int index = 2;
 char mappingCode = usEnglishSoundex.getMappingCode(input, index);
 Assert.assertEquals(expectedMappingCode, mappingCode);
 }

 @Test
 public void testMapMethod() {
 char input = 'P';
 char expectedMappingCode = 'P';
 char mappingCode = usEnglishSoundex.map(input);
 Assert.assertEquals(expectedMappingCode, mappingCode);
 }

 @Test
 public void testSoundexWithCustomMapping() {
 Soundex customSoundex = new Soundex(new char[]{'A', '0', 'P', '1'});
 String input = "Apple";
 String expectedOutput = "A11";
 String output = customSoundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);
 }

}