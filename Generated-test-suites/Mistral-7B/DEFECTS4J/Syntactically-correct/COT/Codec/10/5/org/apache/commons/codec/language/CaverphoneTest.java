import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneWithEmptyString() {
 String result = caverphone.caverphone("");
 String expected = "1111111111";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithNullString() {
 try {
 caverphone.caverphone(null);
 fail("Expected EncoderException");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneWithLowercaseString() {
 String input = "example";
 String result = caverphone.caverphone(input);
 String expected = "m2p2l2t2e2o2m";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithStringContainingDigits() {
 String input = "ab1c2d3e";
 String result = caverphone.caverphone(input);
 String expected = "m2a2b2k2k2l2k2k2d2k2k3e";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithStringContainingSpecialCharacters() {
 String input = "a!b@c#d$";
 String result = caverphone.caverphone(input);
 String expected = "m2a2k2k2l2k2k2k2l2k2k2k2k";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithStringContainingUppercaseLetters() {
 String input = "ABC";
 String result = caverphone.caverphone(input);
 String expected = "A2k2A2k2A2k";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithStringStartingWithPatterns() {
 String input = "coughenough";
 String result = caverphone.caverphone(input);
 String expected = "cou2fenou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithStringStartingWithPatternsAndReplacements() {
 String input = "troughjiqui";
 String result = caverphone.caverphone(input);
 String expected = "trou2fjiy";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithStringHavingAllLetters() {
 String input = "abcdefghijklmnopqrstuvwxyz";
 String result = caverphone.caverphone(input);
 String expected = "m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m2p2l2t2e2o2m";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneWithNullInputThrowsException() {
 caverphone.caverphone(null);
 }

}