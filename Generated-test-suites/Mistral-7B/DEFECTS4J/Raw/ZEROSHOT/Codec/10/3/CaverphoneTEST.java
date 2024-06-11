package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String input = null;
 String expected = "1111111111";
 try {
 String output = caverphone.encode(input);
 assertNull(output);
 } catch (EncoderException e) {
 // Expected
 }
 assertEquals(expected, caverphone.caverphone(input));
 }

 @Test
 public void testEmptyInput() {
 String input = "";
 String expected = "1111111111";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testBasicEncoding() {
 String input = "hello world";
 String expected = "h2h2l2l2o2o2w2w2d2d2";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testBasicDecoding() {
 String input = "h2h2l2l2o2o2w2w2d2d2";
 String expected = "hello world";
 String output = caverphone.decode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testDecodeNullInput() {
 String input = null;
 try {
 String output = caverphone.decode(input);
 assertNull(output);
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testDecodeEmptyInput() {
 String input = "";
 try {
 String output = caverphone.decode(input);
 assertNull(output);
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testDecodeInvalidInput() {
 String input = "invalid";
 try {
 String output = caverphone.decode(input);
 assertFalse(output != null);
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testLongInput() {
 String input = "this is a long input for testing purposes";
 String expected = "t2h2i2s2i2a2l2o2n2g2i2t2i2n2g2p2u2r2p2o2u2s2e2s2";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeNullInput() {
 String input = null;
 try {
 String output = caverphone.encode(input);
 assertNull(output);
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testEncodeEmptyInput() {
 String input = "";
 String expected = "1111111111";
 String output = caverphone.encode(input);
 assertEquals(expected, output);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidInput() {
 String input = "invalid";
 String output = caverphone.encode(input);
 assertNull(output);
 }

 @Test
 public void testIsCaverphoneEqualTrue() {
 String input1 = "hello world";
 String input2 = "hello world";
 boolean expected = true;
 boolean output = caverphone.isCaverphoneEqual(input1, input2);
 assertEquals(expected, output);
 }

 @Test
 public void testIsCaverphoneEqualFalse() {
 String input1 = "hello world";
 String input2 = "hello there";
 boolean expected = false;
 boolean output = caverphone.isCaverphoneEqual(input1, input2);
 assertEquals(expected, output);
 }

 @Test
 public void testIsCaverphoneEqualNull() {
 String input1 = null;
 String input2 = "hello world";
 boolean expected = false;
 boolean output = caverphone.isCaverphoneEqual(input1, input2);
 assertEquals(expected, output);
 }
}