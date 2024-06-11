package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
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
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = EncoderException.class)
 public void testNonStringObject() {
 try {
 caverphone.encode(new Object());
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSimpleCaverphoneString() {
 String input = "apple";
 String expected = "ppleApl";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testSimpleCaverphoneStringWithStartOptions() {
 String input = "CoughApple";
 String expected = "cou2fppleApl";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testSimpleCaverphoneStringWithReplacements() {
 String input = "Quietcanyon";
 String expected = "qietcanyonSiacony";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testSimpleCaverphoneStringWithBoth() {
 String input = "CoughQuietcanyon";
 String expected = "cou2fqietcanyonSiacony";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testComplexCaverphoneString() {
 String input = "Thequickbrownfoxjumpsoverthelazydog";
 String expected = "Th3qu1ckbr0wnf0xjumps0verth3lazyd0g";
 String output = caverphone.caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeObject() {
 String input = "test";
 String expected = caverphone.caverphone(input);
 Object encodedObject = caverphone.encode(input);
 assertEquals(expected, encodedObject.toString());
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "test";
 String str2 = "test";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
 String str1 = "test";
 String str2 = "test1";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

}