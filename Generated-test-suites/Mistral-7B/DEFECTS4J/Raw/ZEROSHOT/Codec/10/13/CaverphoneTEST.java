package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String encoded = caverphone.caverphone("");
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testNullString() {
 try {
 caverphone.caverphone(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testLowerCase() {
 String input = "Hello World";
 String expected = "HhElLoWrLd";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testSpecialCharacters() {
 String input = "Hello@World!";
 String expected = "HhElLoWrLd";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testStartOptions() {
 String input = "CoughCough";
 String expected = "Cou2fCou2f";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);

 input = "Rough";
 expected = "Rou2f";
 encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);

 input = "Tough";
 expected = "Tou2f";
 encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);

 input = "Enough";
 expected = "Enou2f";
 encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);

 input = "Trough";
 expected = "Trou2f";
 encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);

 input = "gn";
 expected = "2n";
 encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testHandlingReplacements() {
 String input = "CqUiCeCyTchC";
 String expected = "2qsi3se2ch2";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testHandlingRemovals() {
 String input = "2333333333";
 String encoded = caverphone.caverphone(input);
 assertEquals("", encoded);
 }

 @Test
 public void testHandlingLength() {
 String input = "CaverphoneTest";
 String expected = "Ca2v3Ap3h3o3n3T3st";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testHandlingLengthWithOneZero() {
 String input = "CaverphoneTest1";
 String expected = "Ca2v3Ap3h3o3n3T3st1";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testHandlingLengthWithSixOnes() {
 String input = "111111CaverphoneTest";
 String expected = "111111Ca2v3Ap3h3o3n3T3st";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testHandlingLengthWithSevenOnes() {
 String input = "1111111CaverphoneTest";
 String expected = "1111111Ca2v3Ap3h3o3n3T3st";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testHandlingLengthWithEightOnes() {
 String input = "11111111CaverphoneTest";
 String expected = "11111111Ca2v3Ap3h3o3n3T3st";
 String encoded = caverphone.caverphone(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testEncode() {
 String input = "CaverphoneTest";
 String expected = "Ca2v3Ap3h3o3n3T3st";
 String encoded = (String) caverphone.encode(input);
 assertEquals(expected, encoded);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "CaverphoneTest";
 String str2 = "CaverphoneTest";
 String str3 = "TestCaverphone";
 boolean result1 = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result1);
 boolean result2 = caverphone.isCaverphoneEqual(str1, str3);
 assertFalse(result2);
 }
}