import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private final StringEncoder caverphoneEncoder = new Caverphone();
 private final StringDecoder caverphoneDecoder = new StringDecoder(caverphoneEncoder);

 @Test
 public void testCaverphoneEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String actualOutput = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "hello world";
 String expectedOutput = "h2l2o2w2or2ld";
 String actualOutput = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneMiddleCase() {
 String input = "HeLlO wOrLd";
 String expectedOutput = "H2l2o2W2or2ld";
 String actualOutput = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneUpperCase() {
 String input = "HELLO WORLD";
 String expectedOutput = "H2l2o2W2or2LD";
 String actualOutput = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneSpecialCharacters() {
 String input = "Hello, 世界!";
 String expectedOutput = "H2l2o222W2or2ld";
 String actualOutput = caverphoneEncoder.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 assertThrows(EncoderException.class, () -> caverphoneEncoder.encode(null));
 }

 @Test
 public void testCaverphoneInvalidInput() {
 assertThrows(EncoderException.class, () -> caverphoneEncoder.encode(123));
 }

 @Test
 public void testCaverphoneDecodeEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String actualOutput = caverphoneDecoder.decode(expectedOutput);
 assertEquals(input, actualOutput);
 }

 @Test
 public void testCaverphoneDecodeNotNull() {
 String input = "hello world";
 String encodedInput = caverphoneEncoder.encode(input);
 String expectedOutput = input;
 String actualOutput = caverphoneDecoder.decode(encodedInput);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneDecodeNull() {
 assertThrows(DecoderException.class, () -> caverphoneDecoder.decode(null));
 }

 @Test
 public void testCaverphoneDecodeInvalidInput() {
 assertThrows(DecoderException.class, () -> caverphoneDecoder.decode(""));
 }

 @Test
 public void testCaverphoneEqualStrings() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean expectedOutput = true;
 boolean actualOutput = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneNotEqualStrings() {
 String str1 = "hello world";
 String str2 = "hello world!";
 boolean expectedOutput = false;
 boolean actualOutput = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneNotEqualEncodedStrings() {
 String str1Encoded = "h2l2o2w2or2ld";
 String str2Encoded = "h2l2o2w2or2ld1";
 boolean expectedOutput = false;
 boolean actualOutput = caverphoneEncoder.isCaverphoneEqual(str1Encoded, str2Encoded);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneNotEqualStringsDifferentLengths() {
 String str1 = "hello world";
 String str2 = "hello world!";
 boolean expectedOutput = false;
 boolean actualOutput = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCaverphoneNullStringComparison() {
 String str1 = null;
 String str2 = "hello world";
 boolean expectedOutput = false;
 boolean actualOutput = caverphoneEncoder.isCaverphoneEqual(str1, str2);
 assertFalse(actualOutput);
 }

 @Test
 public void testCaverphoneNullEncodedStringComparison() {
 String encodedStr1 = null;
 String encodedStr2 = "h2l2o2w2or2ld";
 assertFalse(caverphoneEncoder.isCaverphoneEqual(encodedStr1, encodedStr2));
 }
}