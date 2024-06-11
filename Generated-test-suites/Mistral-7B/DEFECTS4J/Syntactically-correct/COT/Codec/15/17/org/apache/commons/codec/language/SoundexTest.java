package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

 private final Soundex usEnglish = Soundex.US_ENGLISH;

 /**
  * Test method for {@link Soundex#difference(String, String)}
  */
 @Test
 public void testDifference() throws EncoderException {
 String str1 = "Apple";
 String str2 = "Orange";
 int result = usEnglish.difference(str1, str2);
 assertEquals(0, result);

 str1 = "Apple";
 str2 = "Apricot";
 result = usEnglish.difference(str1, str2);
 assertEquals(2, result);

 str1 = "Application";
 str2 = "Application";
 result = usEnglish.difference(str1, str2);
 assertEquals(4, result);
 }

 /**
  * Test method for {@link Soundex#encode(String)}
  */
 @Test
 public void testEncode() {
 String str = "Apple";
 String expected = "A231";
 String result = usEnglish.encode(str);
 assertEquals(expected, result);
 }

 /**
  * Test method for {@link Soundex#encode(Object)}
  */
 @Test(expected = EncoderException.class)
 public void testEncodeInvalidType() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 Object obj = new Object();
 usEnglish.encode(obj);
 }

 /**
  * Test method for {@link Soundex#getMaxLength()}
  */
 @Test
 @Deprecated
 public void testGetMaxLength() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 int maxLength = usEnglish.getMaxLength();
 assertEquals(4, maxLength);
 }

 /**
  * Test method for {@link Soundex#getSoundexMapping()}
  */
 @Test
 public void testGetSoundexMapping() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 char[] expectedMapping = {'0', '1', '2', '3', '0', '1', '2', '0', '2', '2', '4', '5', '0', '1', '2', '3', '0'};
 char[] resultMapping = usEnglish.getSoundexMapping();
 assertArrayEquals(expectedMapping, resultMapping);
 }

 /**
  * Test method for {@link Soundex#map(char)}
  */
 @Test(expected = IllegalArgumentException.class)
 public void testMapInvalidCharacter() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 char invalidChar = '|';
 usEnglish.map(invalidChar);
 }

 /**
  * Test method for {@link Soundex#soundex(String)}
  */
 @Test
 public void testSoundex() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 String str1 = "Apple";
 String expected = "A231";
 String result = usEnglish.soundex(str1);
 assertEquals(expected, result);
 }

 /**
  * Test method for handling HW rule
  */
 @Test
 public void testSoundexHandlerHWRule() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 String str1 = "What's up";
 String expected = "W000";
 String result = usEnglish.soundex(str1);
 assertEquals(expected, result);
 }

 /**
  * Test method for handling multiple HW rule
  */
 @Test
 public void testSoundexHandlerMultipleHWRule() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 String str1 = "What's up Hackett";
 String expected = "W000";
 String result = usEnglish.soundex(str1);
 assertEquals(expected, result);
 }

 /**
  * Test method for handling multiple same letters
  */
 @Test
 public void testSoundexHandlerMultipleSameLetter() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 String str1 = "Applesap";
 String expected = "A231";
 String result = usEnglish.soundex(str1);
 assertEquals(expected, result);
 }

 /**
  * Test method for handling multiple same letters in the middle of the string
  */
 @Test
 public void testSoundexHandlerMultipleSameLetterInMiddle() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 String str1 = "Bananamonkey";
 String expected = "B162";
 String result = usEnglish.soundex(str1);
 assertEquals(expected, result);
 }

 /**
  * Test method for handling multiple same letters at the end of the string
  */
 @Test
 public void testSoundexHandlerMultipleSameLetterAtEnd() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 String str1 = "HelloHello";
 String expected = "H220";
 String result = usEnglish.soundex(str1);
 assertEquals(expected, result);
 }

 /**
  * Test method for handling empty string
  */
 @Test
 public void testSoundexHandlerEmptyString() {
 Soundex usEnglish = Soundex.US_ENGLISH;
 String str1 = "";
 String result = usEnglish.soundex(str1);
 assertEquals("", result);
 }

}