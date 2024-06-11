package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;
import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING;

public class SoundexTest {

 private Soundex soundexUSEnglish;
 private Soundex soundexCustomMapping;

 @Before
 public void setUp() {
 soundexUSEnglish = new Soundex();
 soundexCustomMapping = new Soundex(US_ENGLISH_MAPPING);
 }

 // Test constructor using default US_ENGLISH_MAPPING
 @Test
 public void testDefaultConstructor() {
 String actual = soundexUSEnglish.encode("test");
 String expected = "T230";
 assertEquals(expected, actual);
 }

 // Test constructor using custom mapping
 @Test
 public void testCustomMappingConstructor() {
 char[] customMapping = US_ENGLISH_MAPPING_STRING.toCharArray();
 String actual = soundexCustomMapping.encode("test");
 String expected = "T230";
 assertEquals(expected, actual);
 }

 // Test encode method with valid strings
 @Test
 public void testEncodeValidString() {
 String actual = soundexUSEnglish.encode("apple");
 String expected = "A130";
 assertEquals(expected, actual);
 }

 // Test encode method with empty string
 @Test
 public void testEncodeEmptyString() {
 String actual = soundexUSEnglish.encode("");
 String expected = "";
 assertEquals(expected, actual);
 }

 // Test encode method with null string
 @Test(expected = EncoderException.class)
 public void testEncodeNullString() {
 soundexUSEnglish.encode(null);
 }

 // Test difference method with valid strings
 @Test
 public void testDifferenceValidStrings() {
 String str1 = "apple";
 String str2 = "apples";
 int difference = soundexUSEnglish.difference(str1, str2);
 int expected = 1;
 assertEquals(expected, difference);
 }

 // Test difference method with same string
 @Test
 public void testDifferenceSameString() {
 String str1 = "apple";
 int difference = soundexUSEnglish.difference(str1, str1);
 int expected = 4;
 assertEquals(expected, difference);
 }

 // Test difference method with null strings
 @Test(expected = EncoderException.class)
 public void testDifferenceNullStrings() {
 soundexUSEnglish.difference(null, null);
 }

 // Test difference method with different lengths
 @Test
 public void testDifferenceDifferentLengths() {
 String str1 = "apple";
 String str2 = "orange";
 int difference = soundexUSEnglish.difference(str1, str2);
 int expected = 0;
 assertEquals(expected, difference);
 }

 // Test getMappingCode method with valid index
 @Test
 public void testGetMappingCodeValidIndex() {
 String str = "test";
 char mappingCode = soundexUSEnglish.getMappingCode(str, 0);
 char expected = 'T';
 assertEquals(expected, mappingCode);
 }

 // Test getMappingCode method with invalid index (out of bounds)
 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeInvalidIndex() {
 String str = "test";
 soundexUSEnglish.getMappingCode(str, str.length());
 }

 // Test map method with valid character
 @Test
 public void testMapValidCharacter() {
 char ch = 'T';
 char mappingCode = soundexUSEnglish.map(ch);
 char expected = 'T';
 assertEquals(expected, mappingCode);
 }

 // Test map method with invalid character (not mapped)
 @Test(expected = IllegalArgumentException.class)
 public void testMapInvalidCharacter() {
 char ch = 'X';
 soundexUSEnglish.map(ch);
 }

}