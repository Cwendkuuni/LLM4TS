package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testLowercaseString() {
 String input = "hello world";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2l2o2o2w2w2";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String input = "123hello4world5!";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2l2o2o2w2";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "bake";
 String result = caverphone.caverphone(input);
 String expected = "bak";
 assertEquals(expected, result);
 }

 @Test
 public void testHandeleStartOptions() {
 String input = "cough";
 String result = caverphone.caverphone(input);
 String expected = "cou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleReplacements() {
 String input = "action";
 String result = caverphone.caverphone(input);
 String expected = "asi2on";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleRemovals() {
 String input = "one two three four five six seven eight nine ten";
 String result = caverphone.caverphone(input);
 String expected = "tvhvhvhvhvhvhvhvh";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(123);
 }

 @Test
 public void testIsCaverphoneEqualTrue() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testIsCaverphoneEqualFalse() {
 String str1 = "hello world";
 String str2 = "hi there";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

 @Test
 public void testEncodeNullInput() {
 String result = (String) caverphone.encode(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeEmptyInput() {
 String result = (String) caverphone.encode("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeString() {
 String input = "hello world";
 String result = (String) caverphone.encode(input);
 String expected = "h2h2l2l2o2o2w2";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringInput() {
 caverphone.encode(123);
 }

}