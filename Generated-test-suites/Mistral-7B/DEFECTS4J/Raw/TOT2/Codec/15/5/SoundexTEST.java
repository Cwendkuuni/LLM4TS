package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH;

public class SoundexTest {

 private Soundex soundexUSEnglish;

 @Test
 public void testSoundexConstructorWithDefaultMapping() {
 soundexUSEnglish = new Soundex();
 }

 @Test
 public void testSoundexDefaultMapping() {
 assertEquals("K000", soundexUSEnglish.encode("Kate"));
 assertEquals("T330", soundexUSEnglish.encode("Tom"));
 assertEquals("B262", soundexUSEnglish.encode("Barbara"));
 assertEquals("N162", soundexUSEnglish.encode("Nancy"));
 }

 @Test
 public void testSoundexConstructorWithCustomMapping() {
 soundexUSEnglish = new Soundex(US_ENGLISH_MAPPING_STRING.toCharArray());
 }

 @Test
 public void testSoundexCustomMapping() {
 assertEquals("K000", soundexUSEnglish.encode("Kate"));
 assertEquals("T330", soundexUSEnglish.encode("Tom"));
 assertEquals("B022", soundexUSEnglish.encode("Barbara")); // Different from US_ENGLISH for "B"
 assertEquals("N162", soundexUSEnglish.encode("Nancy"));
 }

 @Test
 public void testSoundexThrowsExceptionForNullInput() {
 try {
 soundexUSEnglish.encode(null);
 fail("Soundex should not accept a null input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSoundexThrowsExceptionForNonStringInput() {
 try {
 soundexUSEnglish.encode(new Object());
 fail("Soundex should not accept a non-String input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSoundexEncodeWithEmptyString() {
 assertEquals("", soundexUSEnglish.encode(""));
 }

 @Test
 public void testSoundexEncodeWithLongString() {
 final String longString = "The quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog, the quick brown fox jumps over the lazy dog";
 assertEquals(soundexUSEnglish.encode(longString).length(), 4);
 }

 @Test
 public void testSoundexDifference() {
 final String s1 = "Apple";
 final String s2 = "Orange";
 final int difference = soundexUSEnglish.difference(s1, s2);
 assertEquals(difference, 0);

 final String s3 = "Kate";
 final String s4 = "Tom";
 final int difference2 = soundexUSEnglish.difference(s3, s4);
 assertEquals(difference2, 0);

 final String s5 = "Apple";
 final String s6 = "Apple";
 final int difference3 = soundexUSEnglish.difference(s5, s6);
 assertEquals(difference3, 4);

 final String s7 = "Apple";
 final String s8 = "Orange";
 final int difference4 = soundexUSEnglish.difference(s7, s8);
 assertEquals(difference4, 0);
}

 @Test
 public void testSoundexCustomMappingDifference() {
 soundexUSEnglish = new Soundex(US_ENGLISH_MAPPING_STRING.toCharArray());
 final String s1 = "Apple";
 final String s2 = "Orange";
 final int difference = soundexUSEnglish.difference(s1, s2);
 assertEquals(difference, 0);

 final String s3 = "Kate";
 final String s4 = "Tom";
 final int difference2 = soundexUSEnglish.difference(s3, s4);
 assertEquals(difference2, 0);

 final String s5 = "Apple";
 final String s6 = "Apple";
 final int difference3 = soundexUSEnglish.difference(s5, s6);
 assertEquals(difference3, 4);

 final String s7 = "Apple";
 final String s8 = "Orange";
 final int difference4 = soundexUSEnglish.difference(s7, s8);
 assertEquals(difference4, 0);
}

}