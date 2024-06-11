import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 @Test
 public void testUSEnglishSoundex() {
 Soundex soundex = Soundex.US_ENGLISH;
 String input1 = "Apple";
 String input2 = "Appetizer";
 String input3 = "Application";
 String input4 = "An Apple";
 String input5 = "";

 // Test encode method
 String expected1 = "A133";
 String expected2 = "A130133";
 String expected3 = "A130133";
 String expected4 = "A133";
 String expected5 = "";

 String result1 = soundex.encode(input1);
 String result2 = soundex.encode(input2);
 String result3 = soundex.encode(input3);
 String result4 = soundex.encode(input4);
 String result5 = soundex.encode(input5);

 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 assertEquals(expected3, result3);
 assertEquals(expected4, result4);
 assertEquals(expected5, result5);

 // Test difference method
 int difference = soundex.difference(input1, input2);
 int differenceExpected = 2;

 assertEquals(differenceExpected, difference);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidInput() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String input = new String(new char[0]);

 soundex.encode(input);
 }

 @Test
 public void testCustomSoundexMapping() {
 Soundex soundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'});
 String input1 = "Apple";
 String input2 = "Appetizer";
 String input3 = "Application";
 String input4 = "An Apple";
 String input5 = "";

 String expected1 = "A133";
 String expected2 = "A130133";
 String expected3 = "A130133";
 String expected4 = "A133";
 String expected5 = "";

 String result1 = soundex.encode(input1);
 String result2 = soundex.encode(input2);
 String result3 = soundex.encode(input3);
 String result4 = soundex.encode(input4);
 String result5 = soundex.encode(input5);

 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 assertEquals(expected3, result3);
 assertEquals(expected4, result4);
 assertEquals(expected5, result5);
 }
}