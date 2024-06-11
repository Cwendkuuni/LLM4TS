import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
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
 public void testCaverphoneLowercase() {
 String input = "Hello World";
 String expected = "H2l2o2W2r2l2d";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String input = "123Hello World456";
 String expected = "H2l2o2W2r2l2d";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test (expected = EncoderException.class)
 public void testCaverphoneInvalidEncodeObject() {
 caverphone.encode(new Object());
 }

 @Test
 public void testCaverphoneEncodeString() {
 String input = "Hello World";
 String expected = "H2l2o2W2r2l2d";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneEqual() {
 String str1 = "Hello World";
 String str2 = "Hello World";
 String str3 = "Hello World1";
 boolean result1 = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result1);

 boolean result2 = caverphone.isCaverphoneEqual(str1, str3);
 assertFalse(result2);
 }

 @Test
 public void testCaverphoneHandleStartOptions() {
 String input1 = "Cough";
 String expected1 = "cou2f";
 String result1 = caverphone.caverphone(input1);
 assertEquals(expected1, result1);

 String input2 = "Rough";
 String expected2 = "rou2f";
 String result2 = caverphone.caverphone(input2);
 assertEquals(expected2, result2);

 String input3 = "Tough";
 String expected3 = "tou2f";
 String result3 = caverphone.caverphone(input3);
 assertEquals(expected3, result3);

 String input4 = "Enough";
 String expected4 = "enou2f";
 String result4 = caverphone.caverphone(input4);
 assertEquals(expected4, result4);

 String input5 = "Trough";
 String expected5 = "trou2f";
 String result5 = caverphone.caverphone(input5);
 assertEquals(expected5, result5);
 }

 @Test
 public void testCaverphoneHandleReplacements() {
 String input1 = "Cq";
 String expected1 = "2q";
 String result1 = caverphone.caverphone(input1);
 assertEquals(expected1, result1);

 String input2 = "Ci";
 String expected2 = "si";
 String result2 = caverphone.caverphone(input2);
 assertEquals(expected2, result2);

 String input3 = "Ce";
 String expected3 = "se";
 String result3 = caverphone.caverphone(input3);
 assertEquals(expected3, result3);

 String input4 = "Cy";
 String expected4 = "sy";
 String result4 = caverphone.caverphone(input4);
 assertEquals(expected4, result4);

 String input5 = "Tch";
 String expected5 = "2ch";
 String result5 = caverphone.caverphone(input5);
 assertEquals(expected5, result5);

 String input6 = "C";
 String expected6 = "k";
 String result6 = caverphone.caverphone(input6);
 assertEquals(expected6, result6);

 String input7 = "Q";
 String expected7 = "k";
 String result7 = caverphone.caverphone(input7);
 assertEquals(expected7, result7);

 String input8 = "X";
 String expected8 = "k";
 String result8 = caverphone.caverphone(input8);
 assertEquals(expected8, result8);

 String input9 = "V";
 String expected9 = "f";
 String result9 = caverphone.caverphone(input9);
 assertEquals(expected9, result9);

 String input10 = "Dg";
 String expected10 = "2g";
 String result10 = caverphone.caverphone(input10);
 assertEquals(expected10, result10);

 String input11 = "Tio";
 String expected11 = "sio";
 String result11 = caverphone.caverphone(input11);
 assertEquals(expected11, result11);

 String input12 = "Tia";
 String expected12 = "sia";
 String result12 = caverphone.caverphone(input12);
 assertEquals(expected12, result12);

 String input13 = "D";
 String expected13 = "t";
 String result13 = caverphone.caverphone(input13);
 assertEquals(expected13, result13);

 String input14 = "Ph";
 String expected14 = "fh";
 String result14 = caverphone.caverphone(input14);
 assertEquals(expected14, result14);

 String input15 = "B";
 String expected15 = "p";
 String result15 = caverphone.caverphone(input15);
 assertEquals(expected15, result15);

 String input16 = "Sh";
 String expected16 = "s2";
 String result16 = caverphone.caverphone(input16);
 assertEquals(expected16, result16);

 String input17 = "Z";
 String expected17 = "s";
 String result17 = caverphone.caverphone(input17);
 assertEquals(expected17, result17);

 String input18 = "Why";
 String expected18 = "Why";
 String result18 = caverphone.caverphone(input18);
 assertEquals(expected18, result18);
 }

 @Test
 public void testCaverphoneHandleRemovals() {
 String input = "22gh3g3gh3";
 String expected = "k";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphonePutTen1s() {
 String input = "22gh3g3gh3";
 String expected = "1111111111";
 String result = caverphone.caverphone(input).substring(0, 10);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneTakeFirstSixCharacters() {
 String input = "1111111111";
 String expected = "111111";
 String result = StringUtils.substring(caverphone.caverphone("1111111111"), 0, 6);
 assertEquals(expected, result);
 }

}