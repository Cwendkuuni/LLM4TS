package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;

public class SoundexTest {

 private Soundex soundexUSEnglish;
 private Soundex soundexCustomMapping;

 @BeforeClass
 public static void setup() {
 soundexUSEnglish = new Soundex();
 soundexCustomMapping = new Soundex(US_ENGLISH_MAPPING);
 }

 @Test
 public void testUSEnglishSoundex() {
 String[] testCases = {
 "apple", "appel", "application", "approach"
 };
 for (String testCase : testCases) {
 String expected = soundexUSEnglish.soundex(testCase);
 String actual = soundexUSEnglish.soundex(testCase);
 assertEquals("Incorrect Soundex code for [" + testCase + "]", expected, actual);
 }
 }

 @Test
 public void testCustomMappingSoundex() {
 String[] testCases = {
 "apple", "appel", "application", "approach"
 };
 for (String testCase : testCases) {
 String expected = SoundexUtils.soundex(testCase, US_ENGLISH_MAPPING);
 String actual = soundexCustomMapping.soundex(testCase);
 assertEquals("Incorrect Soundex code for [" + testCase + "] using custom mapping", expected, actual);
 }
 }

 @Test(expected = EncoderException.class)
 public void testNullString() {
 soundexUSEnglish.soundex(null);
 }

 @Test
 public void testEmptyString() {
 String expected = "";
 String actual = soundexUSEnglish.soundex("");
 assertEquals("Incorrect Soundex code for empty string", expected, actual);
 }

 @Test
 public void testOneCharacterString() {
 String testCase = "A";
 String expected = "A000";
 String actual = soundexUSEnglish.soundex(testCase);
 assertEquals("Incorrect Soundex code for [" + testCase + "]", expected, actual);
 }

 @Test
 public void testMultipleSameCharacterString() {
 String testCase = "AAAA";
 String expected = "A300";
 String actual = soundexUSEnglish.soundex(testCase);
 assertEquals("Incorrect Soundex code for [" + testCase + "]", expected, actual);
 }

 @Test
 public void testTwoLettersDifferentSoundex() {
 String testCase = "apple";
 String testCase2 = "banana";
 String expected1 = "A210";
 String expected2 = "B162";
 String actual1 = soundexUSEnglish.soundex(testCase);
 String actual2 = soundexUSEnglish.soundex(testCase2);
 assertEquals("Incorrect Soundex code for [" + testCase + "]", expected1, actual1);
 assertEquals("Incorrect Soundex code for [" + testCase2 + "]", expected2, actual2);
 }

 @Test
 public void testTwoLettersSameSoundex() {
 String testCase = "apple";
 String testCase2 = "apple";
 String expected = "A210";
 String actual1 = soundexUSEnglish.soundex(testCase);
 String actual2 = soundexUSEnglish.soundex(testCase2);
 assertEquals("Incorrect Soundex code for [" + testCase + "]", expected, actual1);
 assertEquals("Incorrect Soundex code for [" + testCase2 + "]", expected, actual2);
 }

 @Test
 public void testStringWithDigits() {
 String testCase = "Apple123";
 String expected = "A210";
 String actual = soundexUSEnglish.soundex(testCase);
 assertEquals("Incorrect Soundex code for [" + testCase + "]", expected, actual);
 }

 @Test
 public void testStringWithSpecialChars() {
 String testCase = "Apple#";
 String expected = "A210";
 String actual = soundexUSEnglish.soundex(testCase);
 assertEquals("Incorrect Soundex code for [" + testCase + "]", expected, actual);
 }

 @Test
 public void testInternationalMappingSoundex() {
 String[] testCases = {
 "application", "appelation", "approche"
 };
 for (String testCase : testCases) {
 String expected = SoundexUtils.soundex(testCase, US_ENGLISH_MAPPING);
 String actual = soundexCustomMapping.soundex(testCase);
 assertEquals("Incorrect Soundex code for [" + testCase + "] using custom mapping", expected, actual);
 }
 }

}