import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testNullString() {
 String input = null;
 try {
 String output = caverphone.encode(input);
 fail("Expected EncoderException: Parameter supplied to Caverphone encode is null");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSingleLetterString() {
 String input = "a";
 String expectedOutput = "A2";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testMultipleLettersString() {
 String input = "hello";
 String expectedOutput = "h2l2o2";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithDigits() {
 String input = "hello123";
 String expectedOutput = "h2l2o23";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithUpperCaseLetters() {
 String input = "Hello World";
 String expectedOutput = "H2l2o2 w2r2d";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testJUnitTestString() {
 String input = "JUnitTest";
 String expectedOutput = "j2u2n2i2t2 2t2ester";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithDoubleLetters() {
 String input = "eeeee";
 String expectedOutput = "A5";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String input = "!@#$%^&*()_+-=[]{}|;:,.<>?";
 String expectedOutput = "";
 try {
 String output = caverphone.encode(input);
 fail("Expected EncoderException: Version 1.0 of Caverphone is easily derivable from this code by commenting out the 2.0 lines and adding in the 1.0 lines");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testStringWithWhitespaces() {
 String input = "This is a test string";
 String expectedOutput = "t2h2 i2 s2 a2 t2 s2 t2 r2 i2 g2 t2 h2 e2 n2 t2 s2 t2 r2 i2 g2 t2 s2 t2 r2 i2 g2 t2 h2 e2 n2 t2";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEqual() {
 String str1 = "hello";
 String str2 = "hello";
 String str3 = "world";
 String str4 = "world";
 String str5 = "hello";
 String str6 = "hello123";
 String str7 = "hello World";
 String str8 = "JUnitTest";
 String str9 = "JUnitTest";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 assertFalse(caverphone.isCaverphoneEqual(str1, str3));
 assertFalse(caverphone.isCaverphoneEqual(str1, str4));
 assertFalse(caverphone.isCaverphoneEqual(str1, str5));
 assertFalse(caverphone.isCaverphoneEqual(str1, str6));
 assertFalse(caverphone.isCaverphoneEqual(str1, str7));
 assertTrue(caverphone.isCaverphoneEqual(str8, str9));
 }

 @Test
 public void testCaverphoneEncodeException() {
 String input = "";
 try {
 String output = caverphone.encode(input);
 fail("Expected EncoderException: Parameter supplied to Caverphone encode is null");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneEncodeNullString() {
 String input = null;
 try {
 String output = caverphone.encode(input);
 fail("Expected EncoderException: Parameter supplied to Caverphone encode is null");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneEncodeEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEncodeBasic() {
 String input = "abc";
 String expectedOutput = "a2 c2 b2";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEncodeMiddleCase() {
 String input = "HeLlO";
 String expectedOutput = "H2l2 O2";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEncodeOneLetter() {
 String input = "a";
 String expectedOutput = "A2";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEncodeSpace() {
 String input = "hello world";
 String expectedOutput = "h2l2 o2 w2r2d";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEncodeDigits() {
 String input = "12345";
 String expectedOutput = "12345";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEncodeError() {
 String input = "test@123";
 try {
 String output = caverphone.encode(input);
 fail("Expected EncoderException: Version 1.0 of Caverphone is easily derivable from this code by commenting out the 2.0 lines and adding in the 1.0 lines");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneEncodeLongString() {
 String input = StringUtils.repeat("test", 100);
 String expectedOutput = StringUtils.repeat("t2", 100);
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }
}