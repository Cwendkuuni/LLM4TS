import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expected = "1111111111";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testNullString() {
 String input = null;
 try {
 String result = caverphone.encode(input);
 fail("Expected EncoderException for null input");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testSingleLetter() {
 String input = "a";
 String expected = "a";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testMultipleLetters() {
 String input = "hello";
 String expected = "h3l3o2";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testMultipleWords() {
 String input = "hello world";
 String expected = "h3l3o2w2d1";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testSpecialCharacters() {
 String input = "Hello, World!";
 String expected = "H3l3o2W2d1!";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaseInsensitive() {
 String input = "Hello, WOrld!";
 String expected = "H3l3o2W2d1!";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testReplacements() {
 String input = "tough enough";
 String expected = "tou2feno2f";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEdgeCases() {
 String input = "caverphone";
 String expected = "c3v3rph3o3n3";
 String result = caverphone.encode(input);
 assertEquals(expected, result);

 input = "Caverphone";
 expected = "C3v3rph3o3n3";
 result = caverphone.encode(input);
 assertEquals(expected, result);

 input = "Caverphone!";
 expected = "C3v3rph3o3n3!";
 result = caverphone.encode(input);
 assertEquals(expected, result);

 input = "Caverphone, test";
 expected = "C3v3rph3o3n3, t3st";
 result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testErrorScenarios() {
 String input = "12345";
 try {
 String result = caverphone.encode(input);
 fail("Expected EncoderException for non-string input");
 } catch (EncoderException e) {
 // expected
 }

 input = "abcd" + "efg";
 try {
 String result = caverphone.encode(input);
 fail("Expected EncoderException for multiple strings input");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testDecode() {
 String encoded = "h3l3o2w2d1!";
 String decoded = caverphone.decode(encoded);
 assertEquals("Hello, World!", decoded);
 }

 @Test
 public void testDecodeError() {
 String encoded = "12345";
 try {
 String decoded = caverphone.decode(encoded);
 fail("Expected DecoderException for invalid encoding");
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "hello";
 String str2 = "hello";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));

 str1 = "hello";
 str2 = "world";
 assertFalse(caverphone.isCaverphoneEqual(str1, str2));
 }
}