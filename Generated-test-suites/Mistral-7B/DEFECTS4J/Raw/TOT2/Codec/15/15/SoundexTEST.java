import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private static final Soundex US_ENGLISH = Soundex.US_ENGLISH;

 @Test
 public void testEmptyString() {
 String encoded = US_ENGLISH.encode("");
 assertEquals("", encoded);
 }

 @Test
 public void testSingleCharacter() throws EncoderException {
 for (char c = 'A'; c <= 'Z'; c++) {
 String encoded = US_ENGLISH.encode(Character.toString(c));
 String expected = Character.toString(US_ENGLISH_MAPPING[c - 'A']);
 assertEquals(expected, encoded);
 }
 }

 @Test
 public void testMultipleCharacters() throws EncoderException {
 String str = "Apple";
 String encoded = US_ENGLISH.encode(str);
 String expected = "A111";
 assertEquals(expected, encoded);
 }

 @Test
 public void testMultipleWords() throws EncoderException {
 String str = "The quick brown fox jumps over the lazy dog";
 String encoded = US_ENGLISH.encode(str);
 String expected = "T222 K222 F222 X155 J222 M222 P166 V166 O222 T222";
 assertEquals(expected, encoded);
 }

 @Test(expected = EncoderException.class)
 public void testNonAlphabeticCharacter() throws EncoderException {
 String str = "Apple1";
 String encoded = US_ENGLISH.encode(str);
 fail("Expected EncoderException but it was not thrown");
 }

 @Test
 public void testCapitalizedLetters() throws EncoderException {
 String str = "AppLe";
 String encoded = US_ENGLISH.encode(str);
 String expected = "A111";
 assertEquals(expected, encoded);
 }

 @Test
 public void testMultipleInstancesOfSameLetter() throws EncoderException {
 String str = "AAbbBccC";
 String encoded = US_ENGLISH.encode(str);
 String expected = "A111B111C111";
 assertEquals(expected, encoded);
 }

 @Test
 public void testHWRule() throws EncoderException {
 String str = "What's up Hawwain";
 String encoded = US_ENGLISH.encode(str);
 String expected = "W000 T222 H000 W000 T222";
 assertEquals(expected, encoded);
 }

 @Test
 public void testUS_ENGLISH_MAPPING() throws EncoderException {
 char[] mapping = US_ENGLISH_MAPPING;
 Soundex soundex = new Soundex(mapping);
 for (char c = 'A'; c <= 'Z'; c++) {
 String encoded = soundex.encode(Character.toString(c));
 String expected = Character.toString(mapping[c - 'A']);
 assertEquals(expected, encoded);
 }
 }

}