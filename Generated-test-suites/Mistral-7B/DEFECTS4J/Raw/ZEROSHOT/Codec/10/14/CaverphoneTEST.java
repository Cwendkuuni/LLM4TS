import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String encoded = caverphone.caverphone("");
 Assert.assertEquals("1111111111", encoded);
 }

 @Test
 public void testSingleCharacter() {
 String input = "a";
 String expected = "a";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testLowercaseString() {
 String input = "hello";
 String expected = "HeLLo";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testSpecialCharacters() {
 String input = "!@#$%^&*()_+-={}[]|;:,.<>?/";
 String expected = "333333333333";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testStartOptions() {
 String input = "Cough";
 String expected = "cou2f";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);

 String input2 = "Rough";
 String expected2 = "rou2f";
 String encoded2 = caverphone.caverphone(input2);
 Assert.assertEquals(expected2, encoded2);

 String input3 = "Tough";
 String expected3 = "tou2f";
 String encoded3 = caverphone.caverphone(input3);
 Assert.assertEquals(expected3, encoded3);

 String input4 = "Enough";
 String expected4 = "enou2f";
 String encoded4 = caverphone.caverphone(input4);
 Assert.assertEquals(expected4, encoded4);

 String input5 = "Trough";
 String expected5 = "trou2f";
 String encoded5 = caverphone.caverphone(input5);
 Assert.assertEquals(expected5, encoded5);

 String input6 = "Gn";
 String expected6 = "2n";
 String encoded6 = caverphone.caverphone(input6);
 Assert.assertEquals(expected6, encoded6);
 }

 @Test
 public void testReplaceSequences() {
 String input = "Cq";
 String expected = "2q";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);

 String input2 = "Ci";
 String expected2 = "si";
 String encoded2 = caverphone.caverphone(input2);
 Assert.assertEquals(expected2, encoded2);

 String input3 = "Ce";
 String expected3 = "se";
 String encoded3 = caverphone.caverphone(input3);
 Assert.assertEquals(expected3, encoded3);

 String input4 = "Cy";
 String expected4 = "sy";
 String encoded4 = caverphone.caverphone(input4);
 Assert.assertEquals(expected4, encoded4);

 String input5 = "Tch";
 String expected5 = "2ch";
 String encoded5 = caverphone.caverphone(input5);
 Assert.assertEquals(expected5, encoded5);

 String input6 = "C";
 String expected6 = "k";
 String encoded6 = caverphone.caverphone(input6);
 Assert.assertEquals(expected6, encoded6);

 String input7 = "Q";
 String expected7 = "k";
 String encoded7 = caverphone.caverphone(input7);
 Assert.assertEquals(expected7, encoded7);

 String input8 = "X";
 String expected8 = "k";
 String encoded8 = caverphone.caverphone(input8);
 Assert.assertEquals(expected8, encoded8);

 String input9 = "V";
 String expected9 = "f";
 String encoded9 = caverphone.caverphone(input9);
 Assert.assertEquals(expected9, encoded9);

 String input10 = "Dg";
 String expected10 = "2g";
 String encoded10 = caverphone.caverphone(input10);
 Assert.assertEquals(expected10, encoded10);

 String input11 = "Tio";
 String expected11 = "sio";
 String encoded11 = caverphone.caverphone(input11);
 Assert.assertEquals(expected11, encoded11);

 String input12 = "Tia";
 String expected12 = "sia";
 String encoded12 = caverphone.caverphone(input12);
 Assert.assertEquals(expected12, encoded12);

 String input13 = "D";
 String expected13 = "t";
 String encoded13 = caverphone.caverphone(input13);
 Assert.assertEquals(expected13, encoded13);

 String input14 = "Ph";
 String expected14 = "fh";
 String encoded14 = caverphone.caverphone(input14);
 Assert.assertEquals(expected14, encoded14);

 String input15 = "B";
 String expected15 = "p";
 String encoded15 = caverphone.caverphone(input15);
 Assert.assertEquals(expected15, encoded15);

 String input16 = "Sh";
 String expected16 = "s2";
 String encoded16 = caverphone.caverphone(input16);
 Assert.assertEquals(expected16, encoded16);

 String input17 = "Z";
 String expected17 = "s";
 String encoded17 = caverphone.caverphone(input17);
 Assert.assertEquals(expected17, encoded17);

 String input18 = "Why";
 String expected18 = "Why"; // This should not be replaced, but it is in the original code
 String encoded18 = caverphone.caverphone(input18);
 Assert.assertEquals(expected18, encoded18); // This test may fail, but the rest should pass

 String input19 = "Wh";
 String expected19 = "Wh3";
 String encoded19 = caverphone.caverphone(input19);
 Assert.assertEquals(expected19, encoded19);

 String input20 = "W";
 String expected20 = "2";
 String encoded20 = caverphone.caverphone(input20);
 Assert.assertEquals(expected20, encoded20);
 }

 @Test
 public void testFinalE() {
 String input = "Enough";
 String expected = "enou2f";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);
 }

 @Test(expected = EncoderException.class)
 public void testNullInput() {
 Caverphone caverphone = new Caverphone();
 caverphone.caverphone(null);
 }

 @Test
 public void testReversedString() {
 String input = "Able was I ere I saw Elba";
 String expected = "32A32S32E32R32W32A32S32E32L32B32";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testReversedStringWithSpecialCharacters() {
 String input = "!@#$%^&*()_+-={}[]|;:,.<>?/";
 String expected = "333333333333";
 String encoded = caverphone.caverphone(input);
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testDecode() {
 String encoded = "122222222111111111101010101";
 String decoded = caverphone.decode(encoded);
 Assert.assertEquals("hello", decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidLength() {
 String encoded = "122222222111111";
 String decoded = caverphone.decode(encoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidInput() {
 String encoded = "1222222221111111112";
 String decoded = caverphone.decode(encoded);
 }
}