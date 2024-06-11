import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

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
 String input = "HelloWorld";
 String result = caverphone.caverphone(input);
 String expected = "He2LoWoRlD";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabet() {
 String input = "123HelloWorld456";
 String result = caverphone.caverphone(input);
 String expected = "He2LoWoRlD";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneInvalidInput() {
 String input = "123";
 String result = caverphone.caverphone(input);
 fail("Should have thrown EncoderException for invalid input");
 }

 @Test
 public void testCaverphoneWithStartOptions() {
 String input = "CoughCaterpillar";
 String result = caverphone.caverphone(input);
 String expected = "Cou2fCat2erpillar";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithReplacements() {
 String input = "Cequeantcy";
 String result = caverphone.caverphone(input);
 String expected = "2qent2i";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithRemovals() {
 String input = "Cequeantcy2222";
 String result = caverphone.caverphone(input);
 String expected = "ent2i";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithMiddleLetters() {
 String input = "Cequeantcy2222middleCequeantcy2222";
 String result = caverphone.caverphone(input);
 String expected = "ent2iCent2i";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithMultipleCodes() {
 String input = "Cequeantcy2222middleCequeantcy2222Cequeantcy2222";
 String result = caverphone.caverphone(input);
 String expected = "ent2iCent2ient2i";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithWhitespace() {
 String input = "Hello World";
 String result = caverphone.caverphone(input);
 String expected = "He2LoWoRlD";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithCapitalLetters() {
 String input = "HELLO WORLD";
 String result = caverphone.caverphone(input);
 String expected = "He2LoWoRlD";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithEdgeCases() {
 String input1 = "12345";
 String input2 = "1234567890";
 String input3 = "12345678901234567890";
 String input4 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 String input5 = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
 String input6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
 String result1 = caverphone.caverphone(input1);
 String result2 = caverphone.caverphone(input2);
 String result3 = caverphone.caverphone(input3);
 String result4 = caverphone.caverphone(input4);
 String result5 = caverphone.caverphone(input5);
 String result6 = caverphone.caverphone(input6);
 String expected1 = "111111";
 String expected2 = "1111111111";
 String expected3 = "111111111111111111";
 String expected4 = "A";
 String expected5 = "AA";
 String expected6 = "AA1111111111AA";
 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 assertEquals(expected3, result3);
 assertEquals(expected4, result4);
 assertEquals(expected5, result5);
 assertEquals(expected6, result6);
 }

 @Test
 public void testCaverphoneWithExceptionHandling() {
 String input = new String("Java is a programming language");
 try {
 caverphone.caverphone(input);
 fail("Should have thrown EncoderException for invalid input");
 } catch (EncoderException e) {
 // Expected
 }
 }

}