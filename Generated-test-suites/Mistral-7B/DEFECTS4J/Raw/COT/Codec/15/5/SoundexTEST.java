import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private Soundex soundex;

 @Before
 public void setup() {
 soundex = new Soundex();
 }

 // Testing constructor with US_ENGLISH_MAPPING
 @Test
 public void testSoundexWithUSENGLISHMapping() {
 soundex = new Soundex();
 String input = "Apple";
 String expected = "A231";
 assertEquals(expected, soundex.soundex(input));
 }

 // Testing constructor with custom mapping
 @Test
 public void testSoundexWithCustomMapping() {
 char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 soundex = new Soundex(customMapping);
 String input = "Apple";
 String expected = "A231"; // or any other expected value based on custom mapping
 assertEquals(expected, soundex.soundex(input));
 }

 // Testing difference method
 @Test
 public void testDifference() throws EncoderException {
 String input1 = "Apple";
 String input2 = "Orange";
 int expected = 0;
 int result = soundex.difference(input1, input2);
 assertEquals(expected, result);
 }

 // Testing encode method with String parameter
 @Test
 public void testEncodeWithStringParameter() throws EncoderException {
 String input = "Apple";
 String expected = "A231";
 String result = (String) soundex.encode(input);
 assertEquals(expected, result);
 }

 // Testing encode method with Object parameter
 @Test
 public void testEncodeWithObjectParameter() throws EncoderException {
 Object input = "Apple";
 String expected = "A231";
 String result = (String) soundex.encode(input);
 assertEquals(expected, result);
 }

 // Testing getMappingCode method with valid input
 @Test
 public void testGetMappingCodeWithValidInput() {
 String str = "Apple";
 int index = 0;
 char expected = 'A';
 char result = soundex.getMappingCode(str, index);
 assertEquals(expected, result);
 }

 // Testing getMappingCode method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeWithInvalidInput() {
 String str = "Apple";
 int index = 6;
 soundex.getMappingCode(str, index);
 }

 // Testing map method with valid input
 @Test
 public void testMapWithValidInput() {
 char ch = 'A';
 char expected = 'A';
 char result = soundex.map(ch);
 assertEquals(expected, result);
 }

 // Testing map method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testMapWithInvalidInput() {
 char ch = 'Z';
 soundex.map(ch);
 }

 // Testing soundex method with empty input
 @Test
 public void testSoundexWithEmptyInput() {
 String input = "";
 String expected = "";
 String result = soundex.soundex(input);
 assertEquals(expected, result);
 }

 // Testing soundex method with single character input
 @Test
 public void testSoundexWithSingleCharacterInput() {
 String input = "A";
 String expected = "A000";
 String result = soundex.soundex(input);
 assertEquals(expected, result);
 }

 // Testing soundex method with input containing multiple same characters
 @Test
 public void testSoundexWithInputContainingMultipleSameCharacters() {
 String input = "AAAA";
 String expected = "A200";
 String result = soundex.soundex(input);
 assertEquals(expected, result);
 }

 // Testing soundex method with input containing multiple different characters
 @Test
 public void testSoundexWithInputContainingMultipleDifferentCharacters() {
 String input = "AppleBanana";
 String expected = "A2310B162";
 String result = soundex.soundex(input);
 assertEquals(expected, result);
 }

 // Testing soundex method with input containing digits
 @Test
 public void testSoundexWithInputContainingDigits() {
 String input = "Apple123";
 String expected = "A231";
 String result = soundex.soundex(input);
 assertEquals(expected, result);
 }

 // Testing soundex method with input containing special characters
 @Test
 public void testSoundexWithInputContainingSpecialCharacters() {
 String input = "Apple!";
 String expected = "A231";
 String result = soundex.soundex(input);
 assertEquals(expected, result);
 }

}