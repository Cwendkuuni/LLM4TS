package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

 private final Soundex usEnglishSoundex = new Soundex();

 /**
  * Test method for {@link Soundex#difference(String, String)}
  */
 @Test
 public void testDifference() throws EncoderException {
 String str1 = "Apple";
 String str2 = "Orange";
 int difference = usEnglishSoundex.difference(str1, str2);
 assertEquals(0, difference);
 str1 = "Apple";
 str2 = "Apricot";
 difference = usEnglishSoundex.difference(str1, str2);
 assertEquals(3, difference);
 str1 = "Apple";
 str2 = "Apple";
 difference = usEnglishSoundex.difference(str1, str2);
 assertEquals(4, difference);
 }

 /**
  * Test method for {@link Soundex#encode(String)}
  */
 @Test
 public void testEncode() {
 String input = "Apple";
 String expectedOutput = "A220";
 String actualOutput = usEnglishSoundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 input = "Apricot";
 expectedOutput = "A162";
 actualOutput = usEnglishSoundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 input = "Ape";
 expectedOutput = "A100";
 actualOutput = usEnglishSoundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 /**
  * Test method for {@link Soundex#encode(Object)}
  */
 @Test(expected = EncoderException.class)
 public void testEncodeObject() {
 Object input = new Object();
 usEnglishSoundex.encode(input);
 }

 /**
  * Test method for {@link Soundex#getMaxLength()}
  */
 @Test
 @Deprecated
 public void testGetMaxLength() {
 assertEquals(4, usEnglishSoundex.getMaxLength());
 }

 /**
  * Test method for {@link Soundex#getSoundexMapping()}
  */
 @Test
 public void testGetSoundexMapping() {
 char[] expectedSoundexMapping = Soundex.US_ENGLISH_MAPPING;
 char[] actualSoundexMapping = usEnglishSoundex.getSoundexMapping();
 assertArrayEquals(expectedSoundexMapping, actualSoundexMapping);
 }

 /**
  * Test method for {@link Soundex#map(char)}
  */
 @Test(expected = IllegalArgumentException.class)
 public void testMapIllegalArgument() {
 Soundex soundex = new Soundex();
 try {
 soundex.map('z');
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 /**
  * Test method for {@link Soundex#map(char)}
  */
 @Test
 public void testMap() {
 Soundex soundex = new Soundex();
 assertEquals('1', soundex.map('A'));
 assertEquals('0', soundex.map('B'));
 assertEquals('1', soundex.map('C'));
 // ...
 assertEquals('5', soundex.map('Z'));
 }

 /**
  * Test method for {@link Soundex#soundex(String)}
  */
 @Test
 public void testSoundex() {
 String input = "Apple";
 String expectedOutput = "A220";
 String actualOutput = usEnglishSoundex.soundex(input);
 assertEquals(expectedOutput, actualOutput);
 input = "Apricot";
 expectedOutput = "A162";
 actualOutput = usEnglishSoundex.soundex(input);
 assertEquals(expectedOutput, actualOutput);
 input = "Ape";
 expectedOutput = "A100";
 actualOutput = usEnglishSoundex.soundex(input);
 assertEquals(expectedOutput, actualOutput);
 input = "";
 expectedOutput = "";
 actualOutput = usEnglishSoundex.soundex(input);
 assertEquals(expectedOutput, actualOutput);
 }

}