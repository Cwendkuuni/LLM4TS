import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private final StringEncoder encoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String result = encoder.encode(input);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testNullString() {
 String input = null;
 String expectedExceptionMessage = "Parameter supplied to Caverphone encode is null";
 try {
 encoder.encode(input);
 fail("Expected EncoderException to be thrown");
 } catch (EncoderException e) {
 String actualExceptionMessage = e.getMessage();
 assertEquals(expectedExceptionMessage, actualExceptionMessage);
 }
 }

 @Test
 public void testSingleLetter() {
 String input = "a";
 String expectedOutput = "A";
 String result = encoder.encode(input);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testMultipleLetters() {
 String input = "hello";
 String expectedOutput = "H2L2O";
 String result = encoder.encode(input);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testMultipleWords() {
 String input = "hello world";
 String expectedOutput = "H2L2O2R2L2D";
 String result = encoder.encode(input);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testCaverphoneEqual() {
 String input1 = "the quick brown fox jumps over the lazy dog";
 String input2 = "the quick brown fox jumps over the lazy dog";
 String expectedOutput = "T2H2I2Q2I2C2K2I2D2O2X2J2O2T2H2L2Z2Y2G2V2F2S2O2R2T2L2D2G2";
 String result1 = encoder.encode(input1);
 String result2 = encoder.encode(input2);
 assertTrue(encoder.isCaverphoneEqual(input1, input2));
 assertEquals(expectedOutput, result1);
 assertEquals(expectedOutput, result2);
 }

 @Test
 public void testCaverphoneNotEqual() {
 String input1 = "the quick brown fox jumps over the lazy dog";
 String input2 = "the quick brown fox jumps over the cat";
 String expectedOutput1 = "T2H2I2Q2I2C2K2I2D2O2X2J2O2T2H2L2Z2Y2G2V2F2S2O2R2T2L2D2G2";
 String expectedOutput2 = "T2H2I2Q2I2C2K2I2D2O2X2J2O2T2H2L2Z2Y2F2C2T";
 String result1 = encoder.encode(input1);
 String result2 = encoder.encode(input2);
 assertFalse(encoder.isCaverphoneEqual(input1, input2));
 assertNotEquals(expectedOutput1, expectedOutput2);
 assertNotEquals(result1, result2);
 }

 @Test
 public void testInvalidInput() {
 String input = "12345";
 String expectedExceptionMessage = "Parameter supplied to Caverphone encode is not a valid String";
 try {
 encoder.encode(input);
 fail("Expected EncoderException to be thrown");
 } catch (EncoderException e) {
 String actualExceptionMessage = e.getMessage();
 assertEquals(expectedExceptionMessage, actualExceptionMessage);
 }
 }

 @Test
 public void testCaverphoneDecode() {
 String input = "H2L2O2R2L2D2G2";
 String expectedOutput = "the quick brown fox jumps over the lazy dog";
 String result = encoder.decode(input);
 assertEquals(expectedOutput, result);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidInput() {
 String input = "12345";
 encoder.decode(input);
 fail("Expected DecoderException to be thrown");
 }

 @Test
 public void testDecodeEmptyString() {
 String input = "";
 String expectedOutput = "";
 String result = encoder.decode(encoder.encode(input));
 assertEquals(expectedOutput, result);
 }
}