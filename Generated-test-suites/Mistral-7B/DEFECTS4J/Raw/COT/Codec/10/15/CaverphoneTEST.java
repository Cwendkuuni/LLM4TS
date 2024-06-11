import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEncodeNullInput() {
 String input = null;
 try {
 String output = caverphone.encode(input);
 FailureException.fail("Expected EncoderException for null input but got: " + output);
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testEncodeEmptyInput() {
 String input = "";
 String expectedOutput = "1111111111";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeLowerCase() {
 String input = "hello";
 String expectedOutput = "h2l2o2o";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeWithoutE() {
 String input = "banana";
 String expectedOutput = "b2n2n2a";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeSpecialChars() {
 String input = "C0ff33 cAk3!";
 String expectedOutput = "C0k3f3 2Ak3!";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeStartOptions() {
 String input = "CoughCancer";
 String expectedOutput = "cou2fAnc2r";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleStartOptions() {
 String input = "ToughEnough";
 String expectedOutput = "tou2fEnou2f";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleStartOptionsWithY() {
 String input = "CrazyYellow";
 String expectedOutput = "crAzYel2o";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeG() {
 String input = "Ghost";
 String expectedOutput = "gh2st";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeDg() {
 String input = "Dog";
 String expectedOutput = "2g2g";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleG() {
 String input = "DangerousGifts";
 String expectedOutput = "d2n2g2r2u2s2f2t2s";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodePh() {
 String input = "Phone";
 String expectedOutput = "fh2e";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeB() {
 String input = "Baby";
 String expectedOutput = "b2b2y";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeT() {
 String input = "ThisIsATest";
 String expectedOutput = "t2h2i2s2i2a2t2e2s2t";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeS() {
 String input = "Seventeen";
 String expectedOutput = "s2v2e17t";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeL() {
 String input = "HelloWorld";
 String expectedOutput = "h2l2o2l2o";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleL() { 
 String input = "HelloLollyLolly";
 String expectedOutput = "h2l2o2l2o2l2o2l2o";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeR() {
 String input = "Rover";
 String expectedOutput = "r2v2e2r";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleR() {
 String input = "RabbitRabbit";
 String expectedOutput = "r2b2i2t2r2b2i2t";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleW() {
 String input = "WaterWater";
 String expectedOutput = "w2t2e2r2w2t2e2r";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleWh() {
 String input = "WhaleWhale";
 String expectedOutput = "w2h2a2l2e2w2h2a2l2e";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeM() { 
 String input = "Mum";
 String expectedOutput = "m2m2m";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleM() {
 String input = "MotherMother";
 String expectedOutput = "m2th2r2m2th2r";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeN() {
 String input = "Nine";
 String expectedOutput = "n2n2n2n";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleN() {
 String input = "NineNine";
 String expectedOutput = "n2n2n2n2n2n2n";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeF() {
 String input = "Five";
 String expectedOutput = "f2f2f2f";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleF() {
 String input = "FiveFive";
 String expectedOutput = "f2f2f2f2f2f2f2f";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeK() {
 String input = "Kangaroo";
 String expectedOutput = "k2a2n2g2r2o2o";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleK() {
 String input = "KangarooKangaroo";
 String expectedOutput = "k2a2n2g2r2o2ok2a2n2g2r2o2o";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeP() {
 String input = "Pizza";
 String expectedOutput = "p2p2z2z2a";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeMultipleP() {
 String input = "PepperoniPepperoni";
 String expectedOutput = "p2p2p2p2r2o2n2i2p2p2p2p2r2o2n2i2";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeIsCaverphoneEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld";
 String str3 = "HelloWorlds";
 boolean result1 = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result1);
 boolean result2 = caverphone.isCaverphoneEqual(str1, str3);
 assertFalse(result2);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidInput() {
 String input = "123456";
 String expectedOutput = "";
 String output = caverphone.decode(input);
 FailureException.fail("Expected DecoderException for invalid input but got: " + output);
 }

}