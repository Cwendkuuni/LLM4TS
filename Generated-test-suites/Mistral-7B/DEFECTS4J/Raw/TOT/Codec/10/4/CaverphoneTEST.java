package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.util.Locale;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testNullString() {
 assertThrows(EncoderException.class, () -> caverphone.caverphone(null));
 }

 @Test
 public void testLowerCaseString() {
 String result = caverphone.caverphone("hello world");
 assertEquals("H2L3L2O2W2R2D2", result);
 }

 @Test
 public void testUpperCaseString() {
 String result = caverphone.caverphone("HELLO WORLD");
 assertEquals("h2l3l2o2w2r2d2", result);
 }

 @Test
 public void testStringWithDigits() {
 String result = caverphone.caverphone("hello1234world");
 assertEquals("h2l3l2o2w2r2d234", result);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String result = caverphone.caverphone("hello@world");
 assertFalse("caverphone should not contain @ symbol", result.contains("@"));
 assertEquals("h2l3l2o2w2r2d2", result);
 }

 @Test
 public void testStringWithStartOptions() {
 String result = caverphone.caverphone("coughedough");
 assertEquals("co2u2f0u2f", result);
 }

 @Test
 public void testStringWithReplacements() {
 String result = caverphone.caverphone("catches");
 assertEquals("c2t2c2h2s", result);
 }

 @Test
 public void testStringWithRemovals() {
 String result = caverphone.caverphone("123456");
 assertEquals("", result);
 }

 @Test
 public void testStringWithTenOrMoreCharacters() {
 String result = caverphone.caverphone("thisisateststringwithtenormorecharacters");
 assertEquals("t2h2i2s2i2a2t2e2s2t2r2i2n2g", result);
 }

 @Test
 public void testEncodeObject() {
 String input = "hello world";
 String result = (String) caverphone.encode(input);
 assertEquals(caverphone.caverphone(input), result);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "hello world";
 String str2 = "hello world";
 String str3 = "hello world!";
 boolean result1 = caverphone.isCaverphoneEqual(str1, str2);
 boolean result2 = caverphone.isCaverphoneEqual(str1, str3);
 assertEquals(true, result1);
 assertEquals(false, result2);
 }
}