import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 String expected = "1111111111";
 assertEquals(expected, result);
 }

 @Test
 public void testNullString() {
 try {
 caverphone.caverphone(null);
 fail("Should have thrown EncoderException for null input");
 } catch (EncoderException e) {
 // Pass
 }
 }

 @Test
 public void testSimpleString() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h2l2o2o2o2o2o2o3";
 assertEquals(expected, result);
 }

 @Test
 public void testComplexString() {
 String input = "this is a test of the caverphone algorithm";
 String result = caverphone.caverphone(input);
 String expected = "t2h2i2s2i2s2a2t2o2t2h2a2l2g2o2r2i2t2h2a2l2g2o2r2i2t2h2a2l2g";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testStringWithNonAlphabeticCharacters() {
 String input = "12345hello";
 try {
 caverphone.caverphone(input);
 fail("Should have thrown EncoderException for non-alphabetic characters");
 } catch (EncoderException e) {
 // Pass
 }
 }

 @Test(expected = EncoderException.class)
 public void testStringWithLeadingDigits() {
 String input = "123hello";
 try {
 caverphone.caverphone(input);
 fail("Should have thrown EncoderException for leading digits");
 } catch (EncoderException e) {
 // Pass
 }
 }

 @Test
 public void testStringWithMultipleStartOptions() {
 String input = "enough cough enough tough enough enought rough";
 String result = caverphone.caverphone(input);
 String expected = "enou2f cou2f tou2f enou2f rou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithMultipleReplacements() {
 String input = "action tch action cq action tch action ce";
 String result = caverphone.caverphone(input);
 String expected = "act2f act2q act2f act2q act2s";
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithMultipleRemovals() {
 String input = "hello 123 hello 123 hello 123";
 String result = caverphone.caverphone(input);
 String expected = "hellohello";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testStringWithTooManyRemovals() {
 String input = "1111111111";
 try {
 caverphone.caverphone(input);
 fail("Should have thrown EncoderException for no characters left");
 } catch (EncoderException e) {
 // Pass
 }
 }

 @Test
 public void testEncodeAndDecode() {
 String input = "this is a test of the caverphone algorithm";
 String encoded = caverphone.caverphone(input);
 String decoded = new Caverphone().decode(encoded);
 assertEquals(input, decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidInput() {
 String encoded = "1111111111111111111111111111111A";
 try {
 new Caverphone().decode(encoded);
 fail("Should have thrown DecoderException for invalid input");
 } catch (DecoderException e) {
 // Pass
 }
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "this is a test of the caverphone algorithm";
 String str2 = "this is a test of the caverphone algorithm";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testIsCaverphoneNotEqual() {
 String str1 = "this is a test of the caverphone algorithm";
 String str2 = "this is not a test of the caverphone algorithm";// different string
 assertFalse(caverphone.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testIsCaverphoneEqualWithEmptyStrings() {
 String str1 = "";
 String str2 = "";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testIsCaverphoneEqualWithNullStrings() {
 String str1 = null;
 String str2 = null;
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 }
}