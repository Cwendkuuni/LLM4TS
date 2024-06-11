package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class SoundexTest {

 private Soundex soundex;

 @Before
 public void setup() {
 soundex = new Soundex();
 }

 @Test
 public void testEmptyString() {
 String emptySoundex = soundex.encode("");
 assertNull(emptySoundex);
 }

 @Test
 public void testSingleCharacter() {
 String singleCharacterSoundex = soundex.encode("A");
 assertEquals("A000", singleCharacterSoundex);
 }

 @Test
 public void testMultipleCharacters_SameCode() {
 String multiCharactersSoundex = soundex.encode("Apple");
 assertEquals("A233", multiCharactersSoundex);
 }

 @Test
 public void testMultipleCharacters_DifferentCodes() {
 String multiCharactersSoundex = soundex.encode("Banana");
 assertEquals("B162", multiCharactersSoundex);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringArgument() {
 soundex.encode(new Object());
 }

 @Test(expected = EncoderException.class)
 public void testNullArgument() {
 soundex.encode(null);
 }

 @Test
 public void testSoundexWithCustomMapping() {
 Soundex customSoundex = new Soundex(new String("1231200120022455010202").toCharArray());
 String singleCharacterSoundexWithCustomMapping = customSoundex.encode("A");
 assertEquals("1", singleCharacterSoundexWithCustomMapping);
 }

 @Test
 public void testDifference() throws EncoderException {
 String str1 = "Apple";
 String str2 = "Application";
 int difference = soundex.difference(str1, str2);
 assertEquals(3, difference);
 }

 @Test
 public void testDifferenceSameString() throws EncoderException {
 String str1 = "Apple";
 int difference = soundex.difference(str1, str1);
 assertEquals(4, difference);
 }

 @Test
 public void testDifferenceEmptyStrings() throws EncoderException {
 String str1 = "";
 String str2 = "";
 int difference = soundex.difference(str1, str2);
 assertEquals(0, difference);
 }

 @Test
 public void testDifferenceNullStrings() throws EncoderException {
 String str1 = null;
 String str2 = null;
 int difference = soundex.difference(str1, str2);
 assertNull(difference);
 }
}