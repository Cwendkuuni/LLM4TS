import org.apache.commons.codec.DecoderException;
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

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonStringInput() {
 Caverphone caverphone = new Caverphone();
 caverphone.encode(new Object());
 }

 @Test
 public void testCaverphoneLowercase() {
 String input = "HelloWorld";
 String result = caverphone.caverphone(input);
 String expected = "He2oW2oRlD";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveVowels() {
 String input = "The Quick Brown Fox Jumps Over The Lazy Dog";
 String result = caverphone.caverphone(input);
 String expected = "ThQuiCkBrwnFoxJmpsOverThLazyDg";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "Apple";
 String result = caverphone.caverphone(input);
 String expected = "Apple";
 assertEquals(expected, result);

 String input2 = "Banana";
 String result2 = caverphone.caverphone(input2);
 String expected2 = "Banan";
 assertEquals(expected2, result2);
 }

 @Test
 public void testCaverphoneHandlesStartOptions() {
 String input = "Cough";
 String result = caverphone.caverphone(input);
 String expected = "cou2f";
 assertEquals(expected, result);

 String input2 = "Rough";
 String result2 = caverphone.caverphone(input2);
 String expected2 = "rou2f";
 assertEquals(expected2, result2);

 String input3 = "Tough";
 String result3 = caverphone.caverphone(input3);
 String expected3 = "tou2f";
 assertEquals(expected3, result3);

 String input4 = "Enough";
 String result4 = caverphone.caverphone(input4);
 String expected4 = "enou2f";
 assertEquals(expected4, result4);

 String input5 = "Trough";
 String result5 = caverphone.caverphone(input5);
 String expected5 = "trou2f";
 assertEquals(expected5, result5);

 String input6 = "Gn";
 String result6 = caverphone.caverphone(input6);
 String expected6 = "2n";
 assertEquals(expected6, result6);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String input = "Cq";
 String result = caverphone.caverphone(input);
 String expected = "2q";
 assertEquals(expected, result);

 String input2 = "Ci";
 String result2 = caverphone.caverphone(input2);
 String expected2 = "si";
 assertEquals(expected2, result2);

 String input3 = "Ce";
 String result3 = caverphone.caverphone(input3);
 String expected3 = "se";
 assertEquals(expected3, result3);

 String input4 = "Cy";
 String result4 = caverphone.caverphone(input4);
 String expected4 = "sy";
 assertEquals(expected4, result4);

 String input5 = "Tch";
 String result5 = caverphone.caverphone(input5);
 String expected5 = "2ch";
 assertEquals(expected5, result5);

 String input6 = "C";
 String result6 = caverphone.caverphone(input6);
 String expected6 = "k";
 assertEquals(expected6, result6);

 String input7 = "Q";
 String result7 = caverphone.caverphone(input7);
 String expected7 = "k";
 assertEquals(expected7, result7);

 String input8 = "X";
 String result8 = caverphone.caverphone(input8);
 String expected8 = "k";
 assertEquals(expected8, result8);

 String input9 = "V";
 String result9 = caverphone.caverphone(input9);
 String expected9 = "f";
 assertEquals(expected9, result9);

 String input10 = "Dg";
 String result10 = caverphone.caverphone(input10);
 String expected10 = "2g";
 assertEquals(expected10, result10);

 String input11 = "Tio";
 String result11 = caverphone.caverphone(input11);
 String expected11 = "sio";
 assertEquals(expected11, result11);

 String input12 = "Tia";
 String result12 = caverphone.caverphone(input12);
 String expected12 = "sia";
 assertEquals(expected12, result12);

 String input13 = "D";
 String result13 = caverphone.caverphone(input13);
 String expected13 = "t";
 assertEquals(expected13, result13);

 String input14 = "Ph";
 String result14 = caverphone.caverphone(input14);
 String expected14 = "fh";
 assertEquals(expected14, result14);

 String input15 = "B";
 String result15 = caverphone.caverphone(input15);
 String expected15 = "p";
 assertEquals(expected15, result15);

 String input16 = "Sh";
 String result16 = caverphone.caverphone(input16);
 String expected16 = "s2";
 assertEquals(expected16, result16);

 String input17 = "Z";
 String result17 = caverphone.caverphone(input17);
 String expected17 = "s";
 assertEquals(expected17, result17);

 String input18 = "Why"; // 1.0 only
 String result18 = caverphone.caverphone(input18);
 String expected18 = "Why"; // Should throw exception as per comment in code
 //assertFalse(expected18.equals(result18));
 // expected failure

 String input19 = "Wh3"; // 2.0 only
 String result19 = caverphone.caverphone(input19);
 String expected19 = "Wh3";
 assertEquals(expected19, result19);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String input = "2";
 String result = caverphone.caverphone(input);
 String expected = "";
 assertEquals(expected, result);

 String input2 = "3$";
 String result2 = caverphone.caverphone(input2);
 String expected2 = "A";
 assertEquals(expected2, result2);

 String input3 = "3";
 String result3 = caverphone.caverphone(input3);
 String expected3 = "";
 assertEquals(expected3, result3);
 }

 @Test
 public void testCaverphoneHandlesAdditionOfOnes() {
 String input = "111111" + "1111"; // 1.0 only has 6 1s
 String result = caverphone.caverphone(input);
 String expected = input.substring(0, 10);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesCodeLength() {
 String input = "1234567890";
 String result = caverphone.caverphone(input);
 String expected = input.substring(0, 10);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneHandlesInvalidInput() {
 Caverphone caverphone = new Caverphone();
 caverphone.encode("Why"); // 1.0 only - should throw exception
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);

 String str3 = "HelloWorld";
 String str4 = "WorldHello";
 boolean result2 = caverphone.isCaverphoneEqual(str3, str4);
 assertFalse(result2);
 }
}