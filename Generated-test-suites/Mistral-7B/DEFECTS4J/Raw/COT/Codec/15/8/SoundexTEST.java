package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;
import static org.apache.commons.codec.language.Soundex.*;

public class SoundexTest {

 private final Soundex soundexUS = new Soundex();
 private final Soundex soundexCustom = new Soundex(US_ENGLISH_MAPPING);

 @Test
 public void testUS_English_Mapping_Default_Constructor() {
     String expected = "K321";
     String actual = soundexUS.encode("Kathleen");
     assertEquals(expected, actual);
 }

 @Test
 public void testUS_English_Mapping_Custom_Constructor() {
     String expected = "K321";
     String actual = soundexCustom.encode("Kathleen");
     assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeWithNonStringObject() {
     Soundex soundex = new Soundex();
     Object nonStringObject = new Object();
     soundex.encode(nonStringObject);
 }

 @Test
 public void testEncodeEmptyString() {
     String actual = soundexUS.encode("");
     assertEquals("", actual);
 }

 @Test
 public void testEncodeNullString() {
     String actual = soundexUS.encode(null);
     assertEquals(null, actual);
 }

 @Test
 public void testEncodeSingleCharacterString() {
     String expected = "S500";
     String actual = soundexUS.encode("S");
     assertEquals(expected, actual);
 }

 @Test
 public void testEncodeMultipleCharactersStringWithSameSoundexCode() {
     String expected = "B420";
     String actual = soundexUS.encode("Bobby");
     assertEquals(expected, actual);
     String actual2 = soundexUS.encode("Barbara");
     assertEquals(expected, actual2);
 }

 @Test
 public void testEncodeMultipleCharactersStringWithDifferentSoundexCodes() {
     String expected1 = "F230";
     String actual1 = soundexUS.encode("Francis");
     String expected2 = "J162";
     String actual2 = soundexUS.encode("James");
     assertNotEquals(expected1, expected2);
 }

 @Test
 public void testEncodeMultipleCharactersStringWithHWRule() {
     String expected = "F230";
     String actual = soundexUS.encode("Francis");
     String actual2 = soundexUS.encode("Francis Hawthorn");
     assertEquals(expected, actual2);
 }

 @Test
 public void testEncodeMultipleCharactersStringWithCustomMapping() {
     String expected = "S351";
     String actual = soundexCustom.encode("Schuyler");
     assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapWithUnmappedCharacter() {
     Soundex soundex = new Soundex(US_ENGLISH_MAPPING);
     soundex.map((char) 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapWithUnmappedCharacter_CustomConstructor() {
     Soundex soundex = new Soundex(US_ENGLISH_MAPPING_STRING.toCharArray());
     soundex.map((char) 0);
 }

 @Test
 public void testDifferenceWithSameString() {
     int actual = soundexUS.difference("Francis", "Francis");
     assertEquals(4, actual);
 }

 @Test
 public void testDifferenceWithDifferentStrings() {
     int actual = soundexUS.difference("Francis", "James");
     assertEquals(0, actual);
 }

 @Test
 public void testDifferenceWithDifferentStringsWithSameFirstSoundexCode() {
     int actual = soundexUS.difference("Francis", "Francisco");
     assertEquals(3, actual);
 }

 @Test
 public void testDifferenceWithDifferentStringsWithSameLastSoundexCode() {
     int actual = soundexUS.difference("Francis", "Francis Hawthorn");
     assertEquals(4, actual);
 }

 @Test
 public void testDifferenceWithDifferentStringsWithSameSoundexCodeButDifferentLength() {
     int actual = soundexUS.difference("Francis", "Francisk");
     assertEquals(3, actual);
 }

}