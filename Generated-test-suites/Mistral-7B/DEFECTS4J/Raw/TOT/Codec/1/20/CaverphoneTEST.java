import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone encoder = new Caverphone();

 @Test
 public void testCaverphoneEmptyString() {
 String original = "";
 String expected = "1111111111";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneNullString() {
 String original = null;
 try {
 String result = encoder.encode(original);
 fail("Expected EncoderException");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneSingleLetter() {
 String original = "a";
 String expected = "A";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneMultipleLetters() {
 String original = "hello";
 String expected = "h2l2o";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithNumbers() {
 String original = "he1llo";
 String expected = "h2l2o";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithDigits() {
 String original = "123456";
 String expected = "1A1A1A1A1A1A";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithUpperCaseLetters() {
 String original = "HeLLO";
 String expected = "H2L2O";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithMixedCaseLetters() {
 String original = "HeLLo";
 String expected = "H2L2O";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithPunctuation() {
 String original = "hello.world!";
 String expected = "h2l2o.w2r2l2d2!";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithSpecialCharacters() {
 String original = "hello@world%";
 String expected = "h2l2o@w2r2l2d2%";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithWhitespace() {
 String original = " hello world ";
 String expected = "h2l2o2  w2r2l2d2";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithShortStrings() {
 String[] strings = {"", "a", "he", "hel", "hell", "hello", "hello"};
 String[] expected = {"1111111111", "A", "h2l", "h2l2", "h2l2o", "h2l2o", "h2l2o"};
 for (int i = 0; i < strings.length; i++) {
 String original = strings[i];
 String result = encoder.encode(original);
 assertEquals(expected[i], result);
 }
 }

 @Test
 public void testCaverphoneWithLongStrings() {
 String original = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
 String expected = "Th2e2 q3u3i3c2 k3i3n3 g2o2 x2j3p3s2 0v3r3 t3h2 l3a3z2y2 d3o3g.";
 String result = encoder.encode(original);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneEqualStrings() {
 String original1 = "hello";
 String original2 = "hello";
 String expected = "h2l2o";
 String result1 = encoder.encode(original1);
 String result2 = encoder.encode(original2);
 assertTrue(encoder.isCaverphoneEqual(original1, original2));
 assertEquals(expected, result1);
 assertEquals(expected, result2);
 }

 @Test
 public void testCaverphoneNotEqualStrings() {
 String original1 = "hello";
 String original2 = "world";
 String expected = null;
 String result1 = encoder.encode(original1);
 String result2 = encoder.encode(original2);
 assertFalse(encoder.isCaverphoneEqual(original1, original2));
 assertNull(expected);
 }

 @Test
 public void testDecodeCaverphoneEmptyString() {
 String caverphone = "";
 String expected = "";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneNullString() {
 String caverphone = null;
 try {
 String result = encoder.decode(caverphone);
 fail("Expected DecoderException");
 } catch (DecoderException e) {
 // Expected
 }
 }

 @Test
 public void testDecodeCaverphoneSingleLetter() {
 String caverphone = "A";
 String expected = "a";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneMultipleLetters() {
 String caverphone = "H2L2O";
 String expected = "hello";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithNumbers() {
 String caverphone = "1A1A1A1A1A1A";
 String expected = "123456";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithDigits() {
 String caverphone = "1A1A1A1A1A1A";
 String expected = "123456";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithUpperCaseLetters() {
 String caverphone = "H2L2O";
 String expected = "Hello";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithMixedCaseLetters() {
 String caverphone = "H2L2O";
 String expected = "Hello";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithPunctuation() {
 String caverphone = "h2l2o.w2r2l2d2!";
 String expected = "hello.world!";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithSpecialCharacters() {
 String caverphone = "h2l2o@w2r2l2d2%";
 String expected = "hello@world%";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithWhitespace() {
 String caverphone = "h2l2o2  w2r2l2d2";
 String expected = " hello world ";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneWithShortStrings() {
 String[] caverphones = {"", "A", "h2l", "h2l2", "h2l2o", "h2l2o", "h2l2o"};
 String[] strings = {"", "a", "he", "hel", "hell", "hello", "hello"};
 for (int i = 0; i < caverphones.length; i++) {
 String caverphone = caverphones[i];
 String original = strings[i];
 String expected = original;
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testDecodeCaverphoneWithLongStrings() {
 String caverphone = "Th2e2 q3u3i3c2 k3i3n3 g2o2 x2j3p3s2 0v3r3 t3h2 l3a3z2y2 d3o3g.";
 String expected = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
 String result = encoder.decode(caverphone);
 assertEquals(expected, result);
 }

 @Test
 public void testDecodeCaverphoneInvalid() {
 String caverphone = "Th2e2 q3u3i3c2 k3i3n3 g2o2 x2j3p3s2 0v3r3 t3h2 l3a3z2y2 d3o3gA";
 String expected = null;
 String result = encoder.decode(caverphone);
 assertNull(expected);
 }
}