package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneWithEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneWithNullString() {
 try {
 caverphone.caverphone(null);
 fail("Should have thrown EncoderException");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneWithLowerCaseLetters() {
 String input = "hello world";
 String result = caverphone.caverphone(input);
 String expected = "H2h2l2o2w2o2r2l2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithUpperCaseLetters() {
 String input = "HELLO WORLD";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2o2w2o2r2l2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithDigits() {
 String input = "12345";
 String result = caverphone.caverphone(input);
 String expected = "111111";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithSpecialCharacters() {
 String input = "Hello, World!";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2o2w2o2r2l2d2!";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneWithNullInputThrowsException() {
 caverphone.caverphone(null);
 }

 @Test
 public void testCaverphoneEncodeObject() {
 String input = "Test String";
 String result = (String) caverphone.encode(input);
 String expected = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String str1 = "hello world";
 String str2 = "HELLO WORLD";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testCaverphoneIsCaverphoneNotEqual() {
 String str1 = "hello world";
 String str2 = "hello world1";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }
}