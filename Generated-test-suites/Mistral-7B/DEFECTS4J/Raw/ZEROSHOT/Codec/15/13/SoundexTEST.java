import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 @Test
 public void testSoundexUSEnglish() {
 Soundex soundex = Soundex.US_ENGLISH;
 String input = "Apple, Apple Sauce, Appleseed";
 String expected = "A100, A100, A100";
 String[] inputs = input.split(", ");
 String[] outputs = new String[inputs.length];
 for (int i = 0; i < inputs.length; i++) {
 outputs[i] = soundex.soundex(inputs[i]);
 }
 assertArrayEquals(expected.split(", "), outputs);
 }

 @Test
 public void testSoundexCustomMapping() {
 char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
 Soundex soundex = new Soundex(customMapping);
 String input = "Apple, Apple Sauce, Appleseed";
 String expected = "A100, A100, A100";
 String[] inputs = input.split(", ");
 String[] outputs = new String[inputs.length];
 for (int i = 0; i < inputs.length; i++) {
 outputs[i] = soundex.soundex(inputs[i]);
 }
 assertArrayEquals(expected.split(", "), outputs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSoundexWithInvalidCharacter() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String input = "Apple, Äppel, Äpfel";
 String[] inputs = input.split(", ");
 String[] outputs = new String[inputs.length];
 for (int i = 1; i < inputs.length; i++) {
 outputs[i] = soundex.soundex(inputs[i]);
 }
 fail("Expected IllegalArgumentException but no exception was thrown");
 }
}