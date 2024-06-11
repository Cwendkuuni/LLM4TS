import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowercaseLetters() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h2l2o";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneCapitalLetters() {
 String input = "HELLO";
 String result = caverphone.caverphone(input);
 String expected = "h2l2o";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonAlphabetInput() {
 String input = "12345";
 caverphone.caverphone(input);
 }

 @Test
 public void testCaverphoneStartOptionHandling() {
 String input = "coughtough";
 String result = caverphone.caverphone(input);
 String expected = "cou2fou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplacementHandling() {
 String input = "cecyqci";
 String result = caverphone.caverphone(input);
 String expected = "se2qsi";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneMultipleReplacements() {
 String input = "cecyqciqcy";
 String result = caverphone.caverphone(input);
 String expected = "se2qsi2qsi";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesDifferentStartOptions() {
 String input = "toughcoughenough";
 String result = caverphone.caverphone(input);
 String expected = "tou2fou2feno2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesDifferentReplacements() {
 String input = "cecyqciqcecyqci";
 String result = caverphone.caverphone(input);
 String expected = "se2qsi2qsi2qsi";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesDifferentStartAndReplacements() {
 String input = "toughcoughenoughcecyqciqcecyqci";
 String result = caverphone.caverphone(input);
 String expected = "tou2fou2feno2fse2qsi2qsi2qsi";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesMultipleInputStrings() {
 String input1 = "hello";
 String input2 = "world";
 String result1 = caverphone.caverphone(input1);
 String result2 = caverphone.caverphone(input2);
 String expected = "h2l2o";
 assertEquals(expected, result1);
 expected = "w2r2d2";
 assertEquals(expected, result2);
 assertTrue(caverphone.isCaverphoneEqual(input1, input2));
 }

 @Test
 public void testCaverphoneHandlesDifferentInputStrings() {
 String input1 = "hello";
 String input2 = "worldworld";
 String result1 = caverphone.caverphone(input1);
 String result2 = caverphone.caverphone(input2);
 String expected = "h2l2o";
 assertEquals(expected, result1);
 expected = "w2r2d2r2d2";
 assertEquals(expected, result2);
 assertFalse(caverphone.isCaverphoneEqual(input1, input2));
 }

}