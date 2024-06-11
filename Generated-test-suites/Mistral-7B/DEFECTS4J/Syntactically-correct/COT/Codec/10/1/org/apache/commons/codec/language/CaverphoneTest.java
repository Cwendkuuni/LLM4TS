import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmpty() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneSimpleString() {
 String input = "hello";
 String expected = "hel2l2o";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String input = "enough";
 String expected = "enou2f";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneStartOptionsWithY() {
 String input = "why";
 String expected = "Why";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test (expected = EncoderException.class)
 public void testCaverphoneNonString() {
 caverphone.encode(123);
 }

 @Test
 public void testCaverphoneEqualStrings() {
 String str1 = "hello";
 String str2 = "hello";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testCaverphoneNonEqualStrings() {
 String str1 = "hello";
 String str2 = "world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

 @Test
 public void testCaverphoneHandlesDigits() {
 String input = "12345";
 String expected = "12345";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesLongStrings() {
 String input = "thisisateststringtoshowhowlongstringscaverphoneencodes";
 String expected = "thi3isat3ststr2ngt5oshowh3owl3ngstr3ngscaverphoneencodes";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesTwoLetterWords() {
 String input = "qu";
 String expected = "2q";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

}