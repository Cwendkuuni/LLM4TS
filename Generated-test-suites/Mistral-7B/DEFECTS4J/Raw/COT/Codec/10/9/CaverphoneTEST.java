import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEncode() {
 String input = "hello world";
 String expected = "H2l2o2w2r2l2o2l2A";
 String result = caverphone.caverphone(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneEncodeObject() {
 String input = "hello world";
 String expected = "H2l2o2w2r2l2o2l2A";
 try {
 Object result = caverphone.encode(input);
 Assert.assertEquals(expected, result.toString());
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 Object result = caverphone.encode(123);
 }

 @Test
 public void testCaseSensitivity() {
 String input1 = "Hello World";
 String expected1 = "H2L2o2w2r2l2O2l2A";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);

 String input2 = "hello world";
 String expected2 = "h2l2o2w2r2l2o2l2a";
 String result2 = caverphone.caverphone(input2);
 Assert.assertEquals(expected2, result2);
 }

 @Test
 public void testHandlesStartOptions() {
 String input1 = "Cough";
 String expected1 = "cou2f";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);

 String input2 = "Rough";
 String expected2 = "rou2f";
 String result2 = caverphone.caverphone(input2);
 Assert.assertEquals(expected2, result2);

 String input3 = "Tough";
 String expected3 = "tou2f";
 String result3 = caverphone.caverphone(input3);
 Assert.assertEquals(expected3, result3);

 String input4 = "Enough";
 String expected4 = "enou2f";
 String result4 = caverphone.caverphone(input4);
 Assert.assertEquals(expected4, result4);

 String input5 = "trough";
 String expected5 = "trou2f";
 String result5 = caverphone.caverphone(input5);
 Assert.assertEquals(expected5, result5);
 }

 @Test
 public void testHandlesReplacements() {
 String input1 = "Cqueue";
 String expected1 = "2q";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);

 String input2 = "Ciagon";
 String expected2 = "si";
 String result2 = caverphone.caverphone(input2);
 Assert.assertEquals(expected2, result2);

 String input3 = "Ceaser";
 String expected3 = "se";
 String result3 = caverphone.caverphone(input3);
 Assert.assertEquals(expected3, result3);

 String input4 = "Cypress";
 String expected4 = "sy";
 String result4 = caverphone.caverphone(input4);
 Assert.assertEquals(expected4, result4);

 String input5 = "Tchatter";
 String expected5 = "2ch";
 String result5 = caverphone.caverphone(input5);
 Assert.assertEquals(expected5, result5);

 String input6 = "Cakewalk";
 String expected6 = "k";
 String result6 = caverphone.caverphone(input6);
 Assert.assertEquals(expected6, result6);

 String input7 = "Quack";
 String expected7 = "k";
 String result7 = caverphone.caverphone(input7);
 Assert.assertEquals(expected7, result7);

 String input8 = "Xmas";
 String expected8 = "k";
 String result8 = caverphone.caverphone(input8);
 Assert.assertEquals(expected8, result8);

 String input9 = "Victor";
 String expected9 = "f";
 String result9 = caverphone.caverphone(input9);
 Assert.assertEquals(expected9, result9);

 String input10 = "Dog";
 String expected10 = "t";
 String result10 = caverphone.caverphone(input10);
 Assert.assertEquals(expected10, result10);

 String input11 = "Phone";
 String expected11 = "fh";
 String result11 = caverphone.caverphone(input11);
 Assert.assertEquals(expected11, result11);

 String input12 = "Bubble";
 String expected12 = "p";
 String result12 = caverphone.caverphone(input12);
 Assert.assertEquals(expected12, result12);

 String input13 = "Shoes";
 String expected13 = "s2";
 String result13 = caverphone.caverphone(input13);
 Assert.assertEquals(expected13, result13);

 String input14 = "Zebra";
 String expected14 = "s";
 String result14 = caverphone.caverphone(input14);
 Assert.assertEquals(expected14, result14);

 String input15 = "Why";
 String expected15 = "A";
 String result15 = caverphone.caverphone(input15);
 Assert.assertEquals(expected15, result15);

 String input16 = "Why3";
 String expected16 = "W3";
 String result16 = caverphone.caverphone(input16);
 Assert.assertEquals(expected16, result16);

 String input17 = "Why";
 String expected17 = "A";
 String result17 = caverphone.caverphone(input17);
 Assert.assertEquals(expected17, result17);

 String input18 = "Why";
 String expected18 = "3";
 String result18 = caverphone.caverphone(input18);
 Assert.assertEquals(expected18, result18);
 }

 @Test
 public void testHandlesRemovals() {
 String input1 = "Two threes";
 String expected1 = "2";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);

 String input2 = "Three twos";
 String expected2 = "3";
 String result2 = caverphone.caverphone(input2);
 Assert.assertEquals(expected2, result2);

 String input3 = "Three";
 String expected3 = "";
 String result3 = caverphone.caverphone(input3);
 Assert.assertEquals(expected3, result3);

 String input4 = "A";
 String expected4 = "";
 String result4 = caverphone.caverphone(input4);
 Assert.assertEquals(expected4, result4);

 String input5 = "Three ones";
 String expected5 = "";
 String result5 = caverphone.caverphone(input5);
 Assert.assertEquals(expected5, result5);
 }

 @Test
 public void testHandlesAddition() {
 String input1 = "Three twos and three ones";
 String expected1 = "22";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);
 }

 @Test
 public void testHandlesMultipleReplacements() {
 String input1 = "Quack quack";
 String expected1 = "kk";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);
 }

 @Test
 public void testHandlesDigits() {
 String input1 = "12345";
 String expected1 = "12345";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);
 }

 @Test
 public void testHandlesDoubledLetters() {
 String input1 = "Bookkeeper";
 String expected1 = "bkkkpeeppr";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);
 }

 @Test
 public void testHandlesLongStrings() {
 String input1 = "This is a test string for testing the caverphone algorithm";
 String expected1 = "tihs i2 a t3est s2tr2ng f2r t3est2 th3 caverphone a2lgoritm";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);
 }
}