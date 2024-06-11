import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testLowercaseLetters() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h2l2o";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String input = "1Two3Four5";
 String result = caverphone.caverphone(input);
 String expected = "tf";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "cavere";
 String result = caverphone.caverphone(input);
 String expected = "caver";
 assertEquals(expected, result);
 }

 @Test
 public void testStartingPatterns() {
 String input = "enough";
 String result = caverphone.caverphone(input);
 String expected = "enou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlingReplacements() {
 String input = "quiet";
 String result = caverphone.caverphone(input);
 String expected = "2qiet";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlingSingleLetters() {
 String input = "c";
 String result = caverphone.caverphone(input);
 String expected = "k";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlingImplicitVowels() {
 String input = "abstemious";
 String result = caverphone.caverphone(input);
 String expected = "abstemiousA3m3iou3s";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlingDoubleLetters() {
 String input = "caverphone";
 String result = caverphone.caverphone(input);
 String expected = "caverphon3";
 assertEquals(expected, result);
 }

 @Test (expected = EncoderException.class)
 public void testInvalidInputType() {
 Integer num = 42;
 caverphone.encode(num);
 }

 @Test
 public void testCaverphoneEqual() {
 String str1 = "hello";
 String str2 = "hello";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testCaverphoneNotEqual() {
 String str1 = "hello";
 String str2 = "world";
 assertFalse(caverphone.isCaverphoneEqual(str1, str2));
 }

}