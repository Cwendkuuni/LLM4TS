package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for Soundex.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SoundexTest {

 private Soundex soundex;

 @Before
 public void setup() {
 soundex = Soundex.US_ENGLISH;
 }

 /**
 * Test method for {@link Soundex#difference(String, String)}.
 */
 @Test
 public void testDifference() {
 String s1 = "apple";
 String s2 = "appel";
 int result = 0;
 try {
 result = soundex.difference(s1, s2);
 } catch (EncoderException e) {
 e.printStackTrace();
 }
 assertEquals(2, result);
 }

 /**
 * Test method for {@link Soundex#encode(String)}.
 */
 @Test
 public void testEncodeString() {
 String input = "Soundex Test";
 String expected = "S362 T161";
 String result = null;
 try {
 result = soundex.encode(input);
 } catch (EncoderException e) {
 e.printStackTrace();
 }
 assertNull(result);

 result = soundex.encode(input.toUpperCase());
 assertArrayEquals(expected.split(""), result.split(""));
 }

 /**
 * Test method for {@link Soundex#encode(Object)}.
 */
 @Test(expected = EncoderException.class)
 public void testEncodeInvalidObject() {
 soundex.encode(123);
 }

 /**
 * Test method for {@link Soundex#map(char)}.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testMapInvalidCharacter() {
 char invalidChar = '\u0001';
 soundex.map(invalidChar);
 }

 /**
 * Test method for {@link Soundex#soundex(String)}.
 */
 @Test
 public void testSoundexString() {
 String input = "Soundex Test";
 String expected = "S362 T161";
 String result = null;
 try {
 result = soundex.soundex(input);
 } catch (EncoderException e) {
 e.printStackTrace();
 }
 assertNull(result);

 result = soundex.soundex(input.toUpperCase());
 assertArrayEquals(expected.split(""), result.split(""));
 }
}