package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private Soundex soundex = new Soundex();

 @Test
 public void testEncodeString() throws EncoderException {
 String original = "apple";
 String encoded = soundex.encode(original);
 assertEquals("a111", encoded);
 }

 @Test
 public void testEncodeEmptyString() {
 String encoded = soundex.encode("");
 assertNull(encoded);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNullString() {
 soundex.encode(null);
 }

 @Test
 public void testEncodeMultipleStrings() throws EncoderException {
 String original1 = "apple";
 String original2 = "banana";
 String encoded1 = soundex.encode(original1);
 String encoded2 = soundex.encode(original2);
 String encodedBoth = soundex.difference(encoded1, encoded2);
 assertEquals(1, encodedBoth.length());
 }

 @Test
 public void testEncodeEdgeCases() throws EncoderException {
 String lowerCase = soundex.encode("ApPlE");
 assertEquals("a111", lowerCase);

 String specialChars = soundex.encode("Apple!@#$%^&*");
 assertEquals("a111", specialChars);

 String longString = soundex.encode("This is a very long string to test the Soundex encoding with maximum length");
 assertEquals("t326", longString);
 }

 @Test
 public void testEncodeDifferentMappings() {
 Soundex usEnglish = new Soundex();
 Soundex international = new Soundex(new char[]{'1', '2', '2', '2'});
 String usSoundex = usEnglish.encode("Apple");
 String intlSoundex = international.encode("Apple");
 assertNotEquals(usSoundex, intlSoundex);
 }

}