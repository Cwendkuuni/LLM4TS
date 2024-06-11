import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

 @Test
 public void testEmptyString() {
 String input = "";
 String expected = "1111111111";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testNullString() {
 String input = null;
 String expected = "1111111111";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testLowercaseString() {
 String input = "hello world";
 String expected = "h3l3w2o2r2d2";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testSpecialCharacters() {
 String input = "Hello, World!";
 String expected = "H3l3w2o2r2d2!";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 Object input = 5;
 try {
 new Caverphone().encode(input);
 fail("Expected EncoderException but none was thrown");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testWhiteSpace() {
 String input = "hello there   world";
 String expected = "h3l3w2o2r2d2   ";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testStartStrings() {
 String input = "cough";
 String expected = "cou2f";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "rough";
 expected = "rou2f";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "tough";
 expected = "tou2f";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "enough";
 expected = "enou2f";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "trough";
 expected = "trou2f";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testReplacements() {
 String input = "queue";
 String expected = "q2u2e2";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "circuits";
 expected = "s3i3r3c3i3t3s";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "ceiling";
 expected = "s3e3l3i3l3i3n";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "cyclone";
 expected = "s3y3c3l3o3n";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "tchatchet";
 expected = "2t2c2h2a2t2c2h2e2t";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "cucumber";
 expected = "k3u3k3u3b3m3b3r";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testHandlesRemovals() {
 String input = "aardvark";
 String expected = "a3r3v3k";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEndStrings() {
 String input = "rhythm";
 String expected = "r2h2y2t2m";
 String output = new Caverphone().caverphone(input);
 assertEquals(expected, output);

 input = "rhythmic";
 expected = "r2h2y2t2m2i2c";
 output = new Caverphone().caverphone(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneEqual() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean expected = true;
 boolean output = new Caverphone().isCaverphoneEqual(str1, str2);
 assertEquals(expected, output);
 }

 @Test
 public void testCaverphoneNotEqual() {
 String str1 = "hello world";
 String str2 = "hello universe";
 boolean expected = false;
 boolean output = new Caverphone().isCaverphoneEqual(str1, str2);
 assertEquals(expected, output);
 }
}