package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.*;

public class SoundexTest {

 //Test for constructor with no arguments
 @Test
 public void testUS_ENGLISH() {
 String soundexCode = encode("test");
 assertEquals("tesT", soundexCode);
 }

 //Test for constructor with US_ENGLISH_MAPPING argument
 @Test
 public void testUS_ENGLISH_Mapping() {
 Soundex soundexObject = new Soundex(US_ENGLISH_MAPPING);
 String soundexCode = soundexObject.encode("test");
 assertEquals("tesT", soundexCode);
 }

 //Test for constructor with US_ENGLISH_MAPPING_STRING argument
 @Test
 public void testUS_ENGLISH_MappingString() {
 Soundex soundexObject = new Soundex(US_ENGLISH_MAPPING_STRING);
 String soundexCode = soundexObject.encode("test");
 assertEquals("tesT", soundexCode);
 }

 //Test for constructor with custom mapping
 @Test
 public void testCustomMapping() {
 char[] customMapping = {'1', '1', '2', '2'};
 Soundex soundexObject = new Soundex(customMapping);
 String soundexCode = soundexObject.encode("test");
 assertNotEquals("tesT", soundexCode);
 }

 //Test for difference method with identical strings
 @Test
 public void testDifferenceIdenticalStrings() {
 String str1 = "test";
 String str2 = "test";
 int difference = difference(str1, str2);
 assertEquals(4, difference);
 }

 //Test for difference method with non-identical strings
 @Test
 public void testDifferenceNonIdenticalStrings() {
 String str1 = "test";
 String str2 = "example";
 int difference = difference(str1, str2);
 assertEquals(0, difference);
 }

 //Test for difference method with null strings
 @Test(expected = EncoderException.class)
 public void testDifferenceNullStrings() {
 String str1 = null;
 String str2 = "test";
 difference(str1, str2);
 }

 //Test for difference method with one null string
 @Test(expected = EncoderException.class)
 public void testDifferenceNullStringOne() {
 String str1 = "test";
 String str2 = null;
 difference(str1, str2);
 }

 //Test for encode method with null string
 @Test(expected = EncoderException.class)
 public void testEncodeNullString() {
 encode(null);
 }

 //Test for encode method with empty string
 @Test
 public void testEncodeEmptyString() {
 String soundexCode = encode("");
 assertEquals("", soundexCode);
 }

 //Test for encode method with single character string
 @Test
 public void testEncodeSingleCharacterString() {
 String soundexCode = encode("A");
 assertEquals("A000", soundexCode);
 }

 //Test for encode method with multiple characters string
 @Test
 public void testEncodeMultipleCharactersString() {
 String soundexCode = encode("test");
 assertEquals("tesT", soundexCode);
 }

 //Test for encode method with internationalized mapping
 @Test
 public void testEncodeInternationalizedMapping() {
 Soundex soundexObject = new Soundex(new String("1234567890123456").toCharArray());
 String soundexCode = soundexObject.encode("test");
 assertNotEquals("tesT", soundexCode);
 }

 //Test for soundex method with null string
 @Test(expected = IllegalArgumentException.class)
 public void testSoundexNullString() {
 soundex(null);
 }

 //Test for soundex method with empty string
 @Test
 public void testSoundexEmptyString() {
 String soundexCode = soundex("");
 assertEquals("", soundexCode);
 }

 //Test for soundex method with single character string
 @Test
 public void testSoundexSingleCharacterString() {
 String soundexCode = soundex("A");
 assertEquals("A000", soundexCode);
 }

 //Test for soundex method with multiple characters string
 @Test
 public void testSoundexMultipleCharactersString() {
 String soundexCode = soundex("test");
 assertEquals("tesT", soundexCode);
 }

 //Test for soundex method with internationalized mapping
 @Test
 public void testSoundexInternationalizedMapping() {
 Soundex soundexObject = new Soundex(new String("1234567890123456").toCharArray());
 String soundexCode = soundexObject.soundex("test");
 assertNotEquals("tesT", soundexCode);
 }
}