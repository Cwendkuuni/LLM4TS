import org.junit.Test;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private final StringEncoder encoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expected = "1111111111";
 try {
 String result = (String) encoder.encode(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 // Empty string should not throw an exception
 }
 }

 @Test
 public void testNullString() {
 String input = null;
 try {
 String result = (String) encoder.encode(input);
 fail("Null string should throw an exception");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSingleCharacter() {
 String input = "a";
 String expected = "a";
 try {
 String result = (String) encoder.encode(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 fail("Single character should not throw an exception");
 }
 }

 @Test
 public void testMultipleCharacters() {
 String input = "hello";
 String expected = "h3l3o2";
 try {
 String result = (String) encoder.encode(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 fail("Multiple characters should not throw an exception");
 }
 }

 @Test
 public void testSpecialCharacters() {
 String input = "!@#$%^&*()_+-=[]{}|;:,.<>?";
 String expected = "";
 try {
 String result = (String) encoder.encode(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 // Special characters are not supported
 }
 }

 @Test
 public void testCapitalLetters() {
 String input = "Hello";
 String expected = "h3l3o2";
 try {
 String result = (String) encoder.encode(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 fail("Capital letters should not throw an exception");
 }
 }

 @Test
 public void testLongString() {
 String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
 String expected = "L2r33m2320i3p3s3u2m3320d3o3l3o3r320s3i3t320a3m3e3t320c3o3n3s3e3c3t3e3t3u3r320a3d3i3p3i3s3c3i3n3g320e3l3i3t320s3e3d3d3o3r320t3i3m3p3o3r320i3n3c3i3d3d3u3t320u3t320l3a3b3o3r320e3t320d3o3l3o3r320m3a3g3n3a3.";
 try {
 String result = (String) encoder.encode(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 fail("Long string should not throw an exception");
 }
 }

 @Test
 public void testIdenticalStrings() {
 String input1 = "hello";
 String input2 = "hello";
 String expected = "h3l3o2";
 try {
 String result1 = (String) encoder.encode(input1);
 String result2 = (String) encoder.encode(input2);
 assertEquals(expected, result1);
 assertEquals(expected, result2);
 assertTrue(encoder.isCaverphoneEqual(input1, input2));
 } catch (EncoderException e) {
 fail("Identical strings should not throw an exception");
 }
 }

 @Test
 public void testDifferentStrings() {
 String input1 = "hello";
 String input2 = "world";
 String expected = "h3l3o2";
 String result1 = "";
 String result2 = "";
 try {
 result1 = (String) encoder.encode(input1);
 result2 = (String) encoder.encode(input2);
 } catch (EncoderException e) {
 // Throws exception as expected
 }
 assertNotEquals(expected, result1);
 assertNotEquals(expected, result2);
 assertFalse(encoder.isCaverphoneEqual(input1, input2));
 }
}