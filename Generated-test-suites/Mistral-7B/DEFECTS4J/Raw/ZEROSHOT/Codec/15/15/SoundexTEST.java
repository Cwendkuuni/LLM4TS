package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

 Soundex soundex = new Soundex();

 @Test
 public void testDifference() throws EncoderException {
 String str1 = "Apple";
 String str2 = "Orange";
 int difference = soundex.difference(str1, str2);
 assertEquals(0, difference);

 str1 = "Apple";
 str2 = "Application";
 difference = soundex.difference(str1, str2);
 assertEquals(3, difference);

 str1 = "Apple";
 str2 = "Apple";
 difference = soundex.difference(str1, str2);
 assertEquals(4, difference);
 }

 @Test
 public void testEncode() throws EncoderException {
 String str = "Apple";
 String encoded = soundex.encode(str);
 assertEquals("A220", encoded);

 str = "Orange";
 encoded = soundex.encode(str);
 assertEquals("O162", encoded);
 }

 @Test
 public void testEncodeObject() throws EncoderException {
 Object obj = "Apple";
 String encoded = (String) soundex.encode(obj);
 assertEquals("A220", encoded);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidType() throws EncoderException {
 Object obj = 123;
 soundex.encode(obj);
 }

 @Test
 public void testGetMaxLength() {
 // Deprecated method, not tested
 }

 @Test
 public void testGetSoundexMapping() {
 char[] soundexMapping = soundex.getSoundexMapping();
 assertEquals("01230120022455012623010202", new String(soundexMapping));
 }

 @Test
 public void testMap() {
 char ch = 'A';
 char mapped = soundex.map(ch);
 assertEquals('A', mapped);

 ch = 'B';
 mapped = soundex.map(ch);
 assertEquals('B', mapped);

 ch = 'X';
 mapped = soundex.map(ch);
 assertNull(mapped);
 }

 @Test
 public void testSoundex() {
 String str = null;
 String soundexCode = soundex.soundex(str);
 assertNull(soundexCode);

 str = "";
 soundexCode = soundex.soundex(str);
 assertEquals("", soundexCode);

 str = "Apple";
 soundexCode = soundex.soundex(str);
 assertEquals("A220", soundexCode);

 str = "Orange";
 soundexCode = soundex.soundex(str);
 assertEquals("O162", soundexCode);
 }
}