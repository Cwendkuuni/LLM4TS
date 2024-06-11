import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

 private Soundex soundexUSEnglish = Soundex.US_ENGLISH;

 @Test
 public void testUSEnglishSoundexEncoding() throws EncoderException {
 String input = "apple";
 String expectedOutput = "A111";
 String output = soundexUSEnglish.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithNullInput() throws EncoderException {
 String input = null;
 String expectedOutput = null;
 String output = soundexUSEnglish.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithEmptyInput() throws EncoderException {
 String input = "";
 String expectedOutput = "";
 String output = soundexUSEnglish.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithMultipleStrings() throws EncoderException {
 String input1 = "apple";
 String input2 = "banana";
 String expectedOutput1 = "A111";
 String expectedOutput2 = "B110";
 String[] inputs = {input1, input2};
 String[] outputs = new String[inputs.length];
 for (int i = 0; i < inputs.length; i++) {
 outputs[i] = soundexUSEnglish.encode(inputs[i]);
 }
 assertArrayEquals(expectedOutputs(expectedOutput1, expectedOutput2), outputs);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithCustomMapping() throws EncoderException {
 char[] customMapping = {'0', '1', '0', '2', '0', '2', '0', '3', '0', '1', '0', '1', '0', '1', '0', '1', '0', '1', '0', '2'};
 Soundex soundexWithCustomMapping = new Soundex(customMapping);
 String input = "apple";
 String expectedOutput = "A111";
 String output = soundexWithCustomMapping.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testUSEnglishSoundexEncodingWithInvalidCharacter() throws EncoderException {
 char[] customMapping = {'0', '1', '0', '2', '0', '2', '0', '3', '0', '1', '0', '1', '0', '1', '0', '1', '0', '1', '0', '2'};
 Soundex soundexWithCustomMapping = new Soundex(customMapping);
 String input = "äpple";
 String output = soundexWithCustomMapping.encode(input);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithDifference() throws EncoderException {
 String input1 = "apple";
 String input2 = "banana";
 int expectedOutput = 2;
 int output = soundexUSEnglish.difference(input1, input2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithNullInput1() throws EncoderException {
 String input1 = null;
 String input2 = "banana";
 int expectedOutput = 0;
 int output = soundexUSEnglish.difference(input1, input2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithNullInput2() throws EncoderException {
 String input1 = "apple";
 String input2 = null;
 int expectedOutput = 0;
 int output = soundexUSEnglish.difference(input1, input2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithEmptyInput1() throws EncoderException {
 String input1 = "";
 String input2 = "banana";
 int expectedOutput = 0;
 int output = soundexUSEnglish.difference(input1, input2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUSEnglishSoundexEncodingWithEmptyInput2() throws EncoderException { String input1 = "apple"; String input2 = ""; int expectedOutput = 0; int output = soundexUSEnglish.difference(input1, input2); assertEquals(expectedOutput, output); }

 private String[] expectedOutputs(String... expectedOutputs) { return expectedOutputs; }

}