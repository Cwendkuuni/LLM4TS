import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

 private Soundex soundex = Soundex.US_ENGLISH;

 @Test
 public void testDifference() throws EncoderException {
 String s1 = "Apple";
 String s2 = "Orange";
 int difference = soundex.difference(s1, s2);
 Assert.assertEquals(0, difference);
 }

 @Test
 public void testEncode() throws EncoderException {
 String input = "Apple";
 String expectedOutput = "A120";
 String output = soundex.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testGetMaxLength() {
 // @Deprecated method, not testing
 }

 @Test
 public void testGetSoundexMapping() {
 char[] expectedSoundexMapping = {'0', '1', '2', '3', '0', '1', '2', '0', '2', '2', '4', '5', '0', '1', '2', '6', '2', '3', '0', '1'};
 char[] soundexMapping = soundex.getSoundexMapping();
 Assert.assertArrayEquals(expectedSoundexMapping, soundexMapping);
 }

 @Test
 public void testMap() {
 char ch = 'A';
 char expectedMappingCode = '1';
 char mappingCode = soundex.map(ch);
 Assert.assertEquals(expectedMappingCode, mappingCode);
 }

 @Test
 public void testSoundex() throws EncoderException {
 String input = "Apple";
 String expectedOutput = "A120";
 String output = soundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testSoundexWithMultipleSameLetters() throws EncoderException {
 String input = "BaBa";
 String expectedOutput = "B120";
 String output = soundex.soundex(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testSoundexWithMultipleSameLettersAndDifferentLastLetter() throws EncoderException {
 String input1 = "BaBa";
 String input2 = "BaBz";
 int difference = soundex.difference(input1, input2);
 Assert.assertEquals(2, difference);
 }

 @Test(expected = EncoderException.class)
 public void testSoundexWithNonStringObject() throws EncoderException {
 Object obj = 123;
 soundex.encode(obj);
 }

 @Test
 public void testSoundexWithEmptyString() {
 String input = "";
 String output = soundex.soundex(input);
 Assert.assertEquals(input, output);
 }

}