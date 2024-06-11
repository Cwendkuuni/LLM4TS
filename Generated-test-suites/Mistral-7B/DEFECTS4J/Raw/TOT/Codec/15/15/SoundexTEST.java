package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

 @Test
 public void testUS_ENGLISH_Encoding() {
 String input = "Apple, Application, Approach, April, Applebee's, Approximation";
 String expected = "A120 A120 A110 A114 A123 A111";
 try {
 String encoded = new Soundex(Soundex.US_ENGLISH_MAPPING).encode(input);
 assertEquals(expected, encoded);
 } catch (EncoderException e) {
 // Should not reach here
 }
 }

 @Test
 public void testCustomMappingEncoding() {
 String input = "Mango, Mangoes, Marmalade, Marmoset, Marabou";
 String expected = "M320 M320 M310 M330 M301";
 try {
 Soundex soundex = new Soundex(new char[]{'M', '3', 'A', '3', '2', '0', '3', '1', '0'});
 String encoded = soundex.encode(input);
 assertEquals(expected, encoded);
 } catch (EncoderException e) {
 // Should not reach here
 }
 }

 @Test(expected = EncoderException.class)
 public void testEncodingNonString() {
 Soundex soundex = new Soundex();
 soundex.encode(123);
 }

 @Test
 public void testEmptyString() {
 String encoded = new Soundex().encode("");
 assertEquals("", encoded);
 }

 @Test
 public void testSingleCharacterString() {
 String input = "A";
 String expected = "A000";
 String encoded = new Soundex().encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testSoundexDifference() {
 String str1 = "appelle";
 String str2 = "apfel";
 int difference = new Soundex().difference(str1, str2);
 assertEquals(1, difference);
 }

 @Test
 public void testSoundexDifferenceIdentical() {
 String str1 = "appelle";
 String str2 = "appelle";
 int difference = new Soundex().difference(str1, str2);
 assertEquals(4, difference);
 }

 @Test
 public void testSoundexDifferenceDifferentLength() {
 String str1 = "appelle";
 String str2 = "apfelpomme";
 int difference = new Soundex().difference(str1, str2);
 assertEquals(0, difference);
 }

 @Test(expected = EncoderException.class)
 public void testSoundexDifferenceNull() {
 String str1 = null;
 String str2 = "apfel";
 int difference = new Soundex().difference(str1, str2);
 }

 @Test(expected = EncoderException.class)
 public void testSoundexDifferenceNull2() {
 String str1 = "appelle";
 String str2 = null;
 int difference = new Soundex().difference(str1, str2);
 }
}