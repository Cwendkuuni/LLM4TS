import org.apache.commons.codec.EncoderException;
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
 public void testCaverphoneLowerCase() {
 String input = "helloWorld";
 String result = caverphone.caverphone(input);
 String expected = "He2l2l2o2w2o2r2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String input = "Hello1World!@#$%^&*()_+-=[]{}|;:,.<>?23456789";
 String result = caverphone.caverphone(input);
 String expected = "He2l2l2o2w2o2r2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceCough() {
 String input = "Cough";
 String result = caverphone.caverphone(input);
 String expected = "cou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceRough() {
 String input = "Rough";
 String result = caverphone.caverphone(input);
 String expected = "rou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceTough() {
 String input = "Tough";
 String result = caverphone.caverphone(input);
 String expected = "tou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceEnough() {
 String input = "Enough";
 String result = caverphone.caverphone(input);
 String expected = "enou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceTrough() {
 String input = "Trough";
 String result = caverphone.caverphone(input);
 String expected = "trou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceGn() {
 String input = "Gn";
 String result = caverphone.caverphone(input);
 String expected = "2n";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneExceptionForInvalidInput() {
 String input = "12345";
 String result = caverphone.caverphone(input);
 fail("Expected EncoderException for invalid input");
 }

 @Test
 public void testCaverphoneEncodeObject() {
 String input = "Test Input";
 String result = (String) caverphone.encode(input);
 String expected = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testCaverphoneIsCaverphoneNotEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld1";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

}