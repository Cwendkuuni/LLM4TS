import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testLowerCase() {
 String input = "HelloWorld";
 String expected = "h2l2o2l2w2r2d2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabet() {
 String input = "Hello#World";
 String expected = "h2l2w2r2d2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "HelloEnough";
 String expected = "h2l2o2l2w2r2d2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStartOptions() {
 String input = "CoughPotato";
 String expected = "cou2f2o2t2a2t2o2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceCq() {
 String input = "CatQuack";
 String expected = "kat2ak2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceCi() {
 String input = "Circle";
 String expected = "sil3el3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceJe() {
 String input = "Jellyfish";
 String expected = "jelyf3ish3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceJ() {
 String input = "Jazz";
 String expected = "jaz3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplacePh() {
 String input = "Phone";
 String expected = "fon3e";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceB() {
 String input = "Bee";
 String expected = "b3e3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceS() {
 String input = "Soup";
 String expected = "sou2p2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceGh() {
 String input = "Gopher";
 String expected = "goph2k3h3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceTio() {
 String input = "Together";
 String expected = "sio2g3e2th3r2e2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceTia() {
 String input = "Tiger";
 String expected = "tio2g3e2r2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceD() {
 String input = "Dog";
 String expected = "d3g3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplacePhWithFh() {
 String input = "Phone";
 String expected = "fon3efh3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceBWithP() {
 String input = "Bee";
 String expected = "b3p3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceShWithS2() {  // 2.0 only
 String input = "Shoes";
 String expected = "s2oes2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceZWithS() {
 String input = "Zebra";
 String expected = "s2eb3ra3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceHWithA() {
 String input = "Hello";
 String expected = "h2A2l2o2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceAEIOUWithA() {
 String input = "Apple";
 String expected = "A2p2p2l2e2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceW3WithW3() {
 String input = "Watermelon";
 String expected = "w3t33r3m3l3n3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceWhWithWh3() {
 String input = "Whisper";
 String expected = "w3h3i3s3p3r3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceL3WithL3() {
 String input = "Lemon";
 String expected = "l3m3o3n3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceR3WithR3() {
 String input = "Rose";
 String expected = "r3o3s3e3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceRWith2() {
 String input = "Rover";
 String expected = "r2o2v2e2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceLWith2() {
 String input = "Lemon";
 String expected = "l2m2o2n2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceY3WithY3() {
 String input = "Yellow";
 String expected = "y3e3l3l3o3w3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceWhyWithWhy() {
 String input = "Why";
 String expected = "w3h3i3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceWWith2() {
 String input = "Water";
 String expected = "w2a2t3e3r3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceHWithAAndAEIOUWith3() {
 String input = "Apple";
 String expected = "A3p3p3l3e3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceYWith3() {
 String input = "Yellow";
 String expected = "y3e3l3l3o3w3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplace3gh3With3kh3() {
 String input = "Gopher";
 String expected = "goph2k3h3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeObject() {
 String input = "TestInput";
 String expected = "t2e2s2t2i2n2p2";
 try {
 String result = (String) caverphone.encode(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 fail("Exception should not have been thrown");
 }
 }

 @Test
 public void testEncodeNullObject() {
 assertThrows(EncoderException.class, () -> caverphone.encode(null));
 }

 @Test
 public void testEncodeNonStringObject() {
 assertThrows(EncoderException.class, () -> caverphone.encode(1));
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "TestInput";
 String str2 = "TestInput";
 String str3 = "TestDifferent";
 boolean result1 = caverphone.isCaverphoneEqual(str1, str1);
 assertEquals(true, result1);
 boolean result2 = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(true, result2);
 boolean result3 = caverphone.isCaverphoneEqual(str1, str3);
 assertEquals(false, result3);
 }

}