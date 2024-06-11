package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "The quick Brown fox jumps over the lazy Dog";
 String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testDifference() throws EncoderException {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String str1 = "Apple";
 String str2 = "Orange";
 int expected = 0;
 int actual = SoundexUtils.difference(encoder, str1, str2);
 assertEquals(expected, actual);

 str1 = "Apple";
 str2 = "Apricot";
 expected = 3;
 actual = SoundexUtils.difference(encoder, str1, str2);
 assertEquals(expected, actual);

 str1 = "Apple";
 str2 = "Apple";
 expected = 4;
 actual = SoundexUtils.difference(encoder, str1, str2);
 assertEquals(expected, actual);
 }

 @Test
 public void testDifferenceEncoded() {
 String es1 = "A123";
 String es2 = "A121";
 int expected = 3;
 int actual = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, actual);
 }

 @Test
 public void testDifferenceEncodedWithLongerStrings() {
 String es1 = "A1234";
 String es2 = "A1256";
 int expected = 3;
 int actual = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceThrowsExceptionWithNull() throws EncoderException {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String s1 = null;
 String s2 = "Apple";
 SoundexUtils.difference(encoder, s1, s2);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceThrowsExceptionWithNull2() throws EncoderException {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String s1 = "Apple";
 String s2 = null;
 SoundexUtils.difference(encoder, s1, s2);
 }
}