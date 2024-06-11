package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

 private final Soundex soundexUSEnglish = new Soundex();

 /**
 * Test method for {@link Soundex#Soundex()} constructor.
 */
 @Test
 public void testSoundexDefaultConstructor() {
 String code = soundexUSEnglish.encode("test");
 assertEquals("T230", code);
 }

 /**
 * Test method for {@link Soundex#Soundex(char[])} constructor.
 */
 @Test
 public void testSoundexCustomMappingConstructor() {
 Soundex soundexCustomMapping = new Soundex(new char[]{'B', '1', 'C', '2', 'D', '2', 'E', '3', 'F', '1', 'G', '0', 'H', '0', 'I', '1', 'J', '0', 'K', '1', 'L', '1', 'M', '1', 'N', '2', 'O', '0', 'P', '1', 'Q', '1', 'R', '2', 'S', '1', 'T', '2', 'U', '1', 'V', '1', 'W', '1', 'X', '0', 'Y', '1', 'Z', '1'});
 String code = soundexCustomMapping.encode("test");
 assertEquals("T130", code);
 }

 /**
 * Test method for {@link Soundex#Soundex(String)} constructor.
 */
 @Test
 public void testSoundexMappingStringConstructor() {
 Soundex soundexMappingString = new Soundex("B1C2D2E3F1G0H0I1J0K1L1M1N2O0P1Q1R2S1T2U1V1W1X0Y1Z1");
 String code = soundexMappingString.encode("test");
 assertEquals("T130", code);
 }

 /**
 * Test method for {@link Soundex#difference(String, String)} method.
 */
 @Test
 public void testDifferenceMethod() {
 String code1 = soundexUSEnglish.encode("test1");
 String code2 = soundexUSEnglish.encode("test2");
 int difference = soundexUSEnglish.difference("test1", "test2");
 assertEquals(2, difference);
 String code3 = soundexUSEnglish.encode("test1");
 String code4 = soundexUSEnglish.encode("test3");
 difference = soundexUSEnglish.difference("test1", "test3");
 assertEquals(1, difference);
 }

 /**
 * Test method for {@link Soundex#encode(Object)} method.
 */
 @Test(expected = EncoderException.class)
 public void testEncodeObjectIllegalArgumentException() {
 Soundex soundex = new Soundex();
 soundex.encode(123);
 }

 /**
 * Test method for {@link Soundex#encode(String)} method.
 */
 @Test
 public void testEncodeString() {
 String code = soundexUSEnglish.encode("test");
 assertEquals("T230", code);
 }

 /**
 * Test method for {@link Soundex#getMappingCode(String, int)} method.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeIllegalArgumentException() {
 Soundex soundex = new Soundex();
 soundex.getMappingCode("test", 5);
 }

 /**
 * Test method for {@link Soundex#getSoundexMapping()} method.
 */
 @Test
 public void testGetSoundexMapping() {
 char[] soundexMapping = soundexUSEnglish.getSoundexMapping();
 assertEquals('0', soundexMapping[0]);
 assertEquals('1', soundexMapping[1]);
 assertEquals('2', soundexMapping[2]);
 // ... other assertions for all the mapping values
 }

 /**
 * Test method for {@link Soundex#map(char)} method.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testMapIllegalArgumentException() {
 Soundex soundex = new Soundex();soundex.map('!');
 }

 /**
 * Test method for {@link Soundex#soundex(String)} method.
 */
 @Test
 public void testSoundexMethod() {
 String code = soundexUSEnglish.soundex("test");
 assertEquals("T230", code);
 }
}