import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

 @Test
 public void testUSEnglishSoundex() {
 Soundex soundex = Soundex.US_ENGLISH;
 String testString = "Apple, Application, Apple Sauce, Application Software";
 String[] expectedSoundexCodes = {"A120", "A112", "A123", "A112"};
 for (String s : testString.split(",")) {
 String soundexCode = soundex.soundex(s);
 assertEquals("Unexpected soundex code for input: " + s, expectedSoundexCodes[testString.split(",").length - 1 - Arrays.asList(testString.split(",")).indexOf(s)], soundexCode);
 }
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInputSoundex() {
 Soundex soundex = new Soundex("InvalidMapping");
 soundex.soundex("Test");
 }

 @Test
 public void testEmptyStringSoundex() {
 Soundex soundex = Soundex.US_ENGLISH;
 String emptyString = "";
 String soundexCode = soundex.soundex(emptyString);
 assertNull(soundexCode);
 }

 @Test
 public void testNullStringSoundex() {
 Soundex soundex = Soundex.US_ENGLISH;
 String nullString = null;
 String soundexCode = soundex.soundex(nullString);
 assertNull(soundexCode);
 }

 @Test
 public void testSoundexDifference() {
 Soundex soundex = Soundex.US_ENGLISH;
 String string1 = "Apple";
 String string2 = "Application";
 int difference = soundex.difference(string1, string2);
 assertEquals("Unexpected difference between soundex codes Apple and Application", 1, difference);
 }

 @Test
 public void testSoundexDifferenceSameString() {
 Soundex soundex = Soundex.US_ENGLISH;
 String string1 = "Apple";
 int difference = soundex.difference(string1, string1);
 assertEquals("Unexpected difference between soundex codes Apple and Apple", 4, difference);
 }

}