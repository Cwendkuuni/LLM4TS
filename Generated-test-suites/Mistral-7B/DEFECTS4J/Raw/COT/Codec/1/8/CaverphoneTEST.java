import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private Caverphone caverphoneInstance = new Caverphone();

 @Test
 public void testCaverphoneNullInput() {
 String result = caverphoneInstance.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String result = caverphoneInstance.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "hello";
 String expected = "h3l2o";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String input = "Hello123World!";
 String expected = "hlwr";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "Apple";
 String expected = "Apple2";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String input = "Cough";
 String expected = "cou2f";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);

 String input1 = "Rough";
 String expected1 = "rou2f";
 String result1 = caverphoneInstance.caverphone(input1);
 assertEquals(expected1, result1);

 String input2 = "Tough";
 String expected2 = "tou2f";
 String result2 = caverphoneInstance.caverphone(input2);
 assertEquals(expected2, result2);

 String input3 = "Enough";
 String expected3 = "enou2f";
 String result3 = caverphoneInstance.caverphone(input3);
 assertEquals(expected3, result3);

 String input4 = "Trough";
 String expected4 = "trou2f";
 String result4 = caverphoneInstance.caverphone(input4);
 assertEquals(expected4, result4);
 }

 @Test
 public void testCaverphoneReplacements() {
 String input = "Cquiry";
 String expected = "2q";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);

 String input1 = "Ciagon";
 String expected1 = "si";
 String result1 = caverphoneInstance.caverphone(input1);
 assertEquals(expected1, result1);

 String input2 = "Ceiling";
 String expected2 = "se";
 String result2 = caverphoneInstance.caverphone(input2);
 assertEquals(expected2, result2);

 String input3 = "Cypress";
 String expected3 = "sy";
 String result3 = caverphoneInstance.caverphone(input3);
 assertEquals(expected3, result3);

 String input4 = "Tchristmas";
 String expected4 = "2ch";
 String result4 = caverphoneInstance.caverphone(input4);
 assertEquals(expected4, result4);

 String input5 = "Candy";
 String expected5 = "k";
 String result5 = caverphoneInstance.caverphone(input5);
 assertEquals(expected5, result5);

 String input6 = "Question";
 String expected6 = "k";
 String result6 = caverphoneInstance.caverphone(input6);
 assertEquals(expected6, result6);

 String input7 = "Xmas";
 String expected7 = "k";
 String result7 = caverphoneInstance.caverphone(input7);
 assertEquals(expected7, result7);

 String input8 = "David";
 String expected8 = "d";
 String result8 = caverphoneInstance.caverphone(input8);
 assertEquals(expected8, result8);

 String input9 = "Phone";
 String expected9 = "fh";
 String result9 = caverphoneInstance.caverphone(input9);
 assertEquals(expected9, result9);

 String input10 = "Bobby";
 String expected10 = "p";
 String result10 = caverphoneInstance.caverphone(input10);
 assertEquals(expected10, result10);

 String input11 = "Shoes";
 String expected11 = "s2";
 String result11 = caverphoneInstance.caverphone(input11);
 assertEquals(expected11, result11);

 String input12 = "Zebra";
 String expected12 = "s";
 String result12 = caverphoneInstance.caverphone(input12);
 assertEquals(expected12, result12);

 String input13 = "Why";
 String expected13 = "A";
 String result13 = caverphoneInstance.caverphone(input13);
 assertEquals(expected13, result13);

 String input14 = "Why3";
 String expected14 = "Why3";
 String result14 = caverphoneInstance.caverphone(input14);
 assertEquals(expected14, result14);

 String input15 = "Why";
 String expected15 = "A";
 String result15 = caverphoneInstance.caverphone(input15);
 assertEquals(expected15, result15);

 String input16 = "Why";
 String expected16 = "3";
 String result16 = caverphoneInstance.caverphone(input16);
 assertEquals(expected16, result16);

 String input17 = "Why3";
 String expected17 = "Why3";
 String result17 = caverphoneInstance.caverphone(input17);
 assertEquals(expected17, result17);

 String input18 = "Why";
 String expected18 = "3";
 String result18 = caverphoneInstance.caverphone(input18);
 assertEquals(expected18, result18);

 String input19 = "Why3";
 String expected19 = "Why3";
 String result19 = caverphoneInstance.caverphone(input19);
 assertEquals(expected19, result19);
 }

 @Test
 public void testCaverphone3gh3() {
 String input = "Thing3gh3";
 String expected = "3kh3";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneG() {
 String input = "Ghost";
 String expected = "k";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneS() {
 String input = "Silly";
 String expected = "S";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneT() {
 String input = "Tennis";
 String expected = "T";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneP() {
 String input = "Pizza";
 String expected = "P";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneK() {
 String input = "Kangaroo";
 String expected = "K";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneF() {
 String input = "Flower";
 String expected = "F";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneM() {
 String input = "Melon";
 String expected = "M";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneN() {
 String input = "Night";
 String expected = "N";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneW3() {
 String input = "Water3";
 String expected = "W3";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneR3() {
 String input = "Raspberry3";
 String expected = "R3";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneR() {
 String input = "Rose";
 String expected = "2";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneL3() {
 String input = "Lemon3";
 String expected = "L3";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneL() {
 String input = "Lemon";
 String expected = "2";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneJ() {
 String input = "Jolly";
 String expected = "y";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneY3() {
 String input = "Yesterday3";
 String expected = "Y3";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneY() {
 String input = "Yesterday";
 String expected = "A";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneY() {
 String input = "Yesterday";
 String expected = "3";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphone3() {
 String input = "Three";
 String expected = "";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphone3$() {
 String input = "Three$";
 String expected = "A";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonStringInput() {
 caverphoneInstance.caverphone(new Object());
 }

}