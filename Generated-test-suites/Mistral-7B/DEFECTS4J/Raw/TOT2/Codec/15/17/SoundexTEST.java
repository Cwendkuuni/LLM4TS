package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for Soundex class
 */
public class SoundexTest {

 @Test
 public void testDefaultSoundexInstance() {
 String s1 = "apple";
 String s2 = "appel";
 int result = new Soundex().difference(s1, s2);
 assertEquals(3, result);
 }

 @Test
 public void testCustomSoundexInstance() {
 char[] customMapping = {'1', '1', '1', '0', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'};
 Soundex soundex = new Soundex(customMapping);
 String s1 = "apple";
 String s2 = "appel";
 int result = soundex.difference(s1, s2);
 assertEquals(3, result);
 }

 @Test
 public void testEncodeString() throws EncoderException {
 String str = "apple";
 String encoded = new Soundex().encode(str);
 assertEquals("A123", encoded);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringObject() throws EncoderException {
 Object obj = new Object();
 String encoded = new Soundex().encode(obj);
 }

 @Test
 public void testGetSoundexMapping() {
 Soundex soundex = new Soundex();
 char[] mapping = soundex.getSoundexMapping();
 assertEquals('0', mapping[0]);
 assertEquals('1', mapping[1]);
 }

 @Test
 public void testSoundexWithSpecialCharacters() {
 String s1 = "Apple&Orange";
 String s2 = "Apple Orange";
 int result = new Soundex().difference(s1, s2);
 assertEquals(3, result);
 }

 @Test
 public void testSoundexEmptyString() {
 String s1 = "";
 String s2 = "";
 int result = new Soundex().difference(s1, s2);
 assertEquals(0, result);
 }
}