package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class CaverphoneTest {

 Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneNullString() {
 try {
 caverphone.caverphone(null);
 } catch (EncoderException e) {
 assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
 }
 }

 @Test
 public void testCaverphoneLowerCase() {
 String result = caverphone.caverphone("Hello World");
 assertEquals("HlLoWrLd", result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabet() {
 String result = caverphone.caverphone("1H3ll0 W0rld");
 assertEquals("HlLWrd", result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String result = caverphone.caverphone("Caverphone");
 assertEquals("Caverphone".substring(0, "Caverphone".length() - 1), result);
 }

 @Test
 public void testCaverphoneStartsWith() {
 String result = caverphone.caverphone("Cough");
 assertEquals("cou2f", result);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String result = caverphone.caverphone("Caverphone is a Caverphone encoder.");
 assertEquals("Cvvrphn is a Cvrphn encoder.", result);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String result = caverphone.caverphone("The quick brown fox jumps over the lazy dog.");
 assertEquals("Thqbrwn fx jmpv r th lzy dg.", result);
 }

 @Test
 public void testCaverphoneHandlesDoubles() {
 String result = caverphone.caverphone("The quick brown fox jumps over the quick brown fox.");
 assertEquals("Thqbrwn fx jmpv r Thqbrwn fx.", result);
 }

 @Test
 public void testCaverphoneHandlesStartsWithDifferentCase() {
 String result = caverphone.caverphone("The quick brown fox jumps over The LAZY dog.");
 assertEquals("Thqbrwn fx jmpv r Th lzy dg.", result);
 }

 @Test
 public void testCaverphoneHandlesStartsWithMultiple() {
 String result = caverphone.caverphone("The quick brown fox jumps over The quick brown fox jumps over the LAZY dog.");
 assertEquals("Thqbrwn fx jmpv r Thqbrwn fx jmpv r Th lzy dg.", result);
 }

 @Test
 public void testCaverphoneHandlesMultipleStartsWith() {
 String result = caverphone.caverphone("The quick brown fox jumps over The quick brown fox jumps over The LAZY dog.");
 assertEquals("Thqbrwn fx jmpv r Thqbrwn fx jmpv r Th lzy dg.", result);
 }

 @Test
 public void testCaverphoneHandlesEndsWith() {
 String result = caverphone.caverphone("The quick brown fox jumps over the LAZY dog.");
 assertEquals("Thqbrwn fx jmpv r th lzy dgA", result);
 }

 @Test
 public void testCaverphoneHandlesEndsWithMultiple() {
 String result = caverphone.caverphone("The quick brown fox jumps over the quick brown fox jumps over the LAZY dog.");
 assertEquals("Thqbrwn fx jmpv r Thqbrwn fx jmpv r th lzy dgA", result);
 }

 @Test
 public void testCaverphoneHandlesStartsWithAndEndsWith() {
 String result = caverphone.caverphone("The quick brown fox jumps over the quick brown fox jumps over the LAZY dog.");
 assertEquals("Thqbrwn fx jmpv r Thqbrwn fx jmpv r th lzy dgA", result);
 }

 @Test
 public void testCaverphoneHandlesStartsWithDifferentChar() {
 String result = caverphone.caverphone("The quick brown fox jumps over the quick brown fox jumps over the q UICK dog.");
 assertEquals("Thqbrwn fx jmpv r Thqbrwn fx jmpv r th quck dg.", result);
 }

 @Test
 public void testCaverphoneHandlesStartsWithMultipleDifferent() { 
 String result = caverphone.caverphone("The quick brown fox jumps over the quick brown fox jumps over the q UICK dog.");
 assertEquals("Thqbrwn fx jmpv r Thqbrwn fx jmpv r th quck dg.", result);
 }

 @Test
 public void testEncodeDecode() throws EncoderException {
 String originalString = "The quick brown fox jumps over the quick brown fox jumps over the LAZY dog.";
 String encodedString = caverphone.encode(originalString);
 String decodedString = (String) caverphone.decode(encodedString);
 assertEquals(originalString, decodedString);
 }

 @Test
 public void testEncodeDecodeDifferentOrder() throws EncoderException {
 String originalString = "The quick brown fox jumps over the quick brown fox jumps over the LAZY dog.";
 String encodedString1 = caverphone.encode(originalString);
 String decodedString1 = caverphone.decode(encodedString1);
 assertEquals(originalString, decodedString1);
 String encodedString2 = caverphone.encode(decodedString1);
 assertEquals(encodedString1, encodedString2);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "The quick brown fox jumps over the quick brown fox jumps over the LAZY dog.";
 String str2 = "The quick brown fox jumps over the quick brown fox jumps over the LAZY dog.";
 String str3 = "The quick brown fox jumps over the quick brown fox jumps over the Lazy dog.";
 boolean result1 = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(true, result1);
 boolean result2 = caverphone.isCaverphoneEqual(str1, str3);
 assertEquals(false, result2);
 }

 @Test
 public void testIsCaverphoneEqualEmptyStrings() {
 String str1 = "";
 String str2 = "";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(true, result);
 }

 @Test
 public void testIsCaverphoneEqualNullStrings() {
 String str1 = null;
 String str2 = null;
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(true, result);
 }
}