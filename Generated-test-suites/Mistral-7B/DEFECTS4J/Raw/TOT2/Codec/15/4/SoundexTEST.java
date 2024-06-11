import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private final Soundex soundexUSEnglish = new Soundex();
 private final String[] inputStrings = {
 "apple", "approximation", "banana", "cocoon", "cookie", "daisy", "elephant", "fighter", "giraffe", "happiness",
 "indiana", "jumping", "kangaroo", "lion", "mango", "nest", "orange", "penguin", "quilt", "rabbit", "socks",
 "tiger", "uniform", "victory", "watermelon", "xylophone", "yellow", "zebra"
 };

 @Test
 public void testSoundexUSEnglish() {
 for (String input : inputStrings) {
 String expected = SoundexUtils.expectedSoundex(input);
 String actual = soundexUSEnglish.soundex(input);
 assertEquals(expected, actual);
 }
 }

 @Test(expected = EncoderException.class)
 public void testSoundexNullInput() {
 try {
 soundexUSEnglish.soundex(null);
 fail("Should throw EncoderException for null input");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test(expected = EncoderException.class)
 public void testSoundexEmptyInput() {
 try {
 soundexUSEnglish.soundex("");
 fail("Should throw EncoderException for empty input");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSoundexInvalidCharacter() {
 try {
 soundexUSEnglish.soundex("äpple");
 fail("Should throw IllegalArgumentException for invalid character");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSoundexDifference() {
 String str1 = "kangaroo";
 String str2 = "leopard";
 int difference = soundexUSEnglish.difference(str1, str2);
 assertEquals(2, difference);
 }

 @Test
 public void testSoundexSameString() {
 String str1 = "apple";
 String str2 = "apple";
 int difference = soundexUSEnglish.difference(str1, str2);
 assertEquals(4, difference);
 }

 @Test
 public void testSoundexDifferenceWithEmptyString() {
 String str1 = "apple";
 String str2 = "";
 int difference = soundexUSEnglish.difference(str1, str2);
 assertEquals(0, difference);
 }

 @Test
 public void testSoundexDifferenceWithNullString() {
 String str1 = "apple";
 String str2 = null;
 int difference = soundexUSEnglish.difference(str1, str2);
 assertEquals(0, difference);
 }

}