import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the Soundex class.
 */
public class SoundexTest {

 @Test
 public void testUSEnglishSoundex() {
 Soundex soundex = Soundex.US_ENGLISH;
 String input1 = "Apple";
 String input2 = "Application";
 String input3 = "Anna";
 String input4 = "Anton";
 String input5 = "Elephant";
 String input6 = "Eliza";
 String input7 = "Eins, Zwei, Drei";

 // Test encode method
 String output1 = soundex.encode(input1);
 Assert.assertEquals("APL", output1);

 String output2 = soundex.encode(input2);
 Assert.assertEquals("APLION", output2);

 String output3 = soundex.encode(input3);
 Assert.assertEquals("A11", output3);

 String output4 = soundex.encode(input4);
 Assert.assertEquals("A10", output4);

 String output5 = soundex.encode(input5);
 Assert.assertEquals("E150", output5);

 String output6 = soundex.encode(input6);
 Assert.assertEquals("E160", output6);

 String output7 = soundex.encode(input7);
 Assert.assertEquals("I110", output7);

 // Test difference method
 int difference = soundex.difference(input1, input2);
 Assert.assertEquals(3, difference);

 difference = soundex.difference(input1, input3);
 Assert.assertEquals(1, difference);

 difference = soundex.difference(input1, input4);
 Assert.assertEquals(2, difference);

 difference = soundex.difference(input1, input5);
 Assert.assertEquals(2, difference);

 difference = soundex.difference(input1, input6);
 Assert.assertEquals(3, difference);

 difference = soundex.difference(input1, input7);
 Assert.assertEquals(4, difference);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInput() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String input = "!Apple";
 String output = soundex.encode(input);
 }
}