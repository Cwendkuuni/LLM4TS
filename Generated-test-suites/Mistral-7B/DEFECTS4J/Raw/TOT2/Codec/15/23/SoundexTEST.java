import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

 private Soundex soundex = Soundex.US_ENGLISH;

 @Test
 public void testEncodeString() {
 String input = "Apple";
 String expected = "K123";
 String actual = soundex.encode(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEncodeNullString() {
 String input = null;
 try {
 String actual = soundex.encode(input);
 Assert.fail();
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String expected = "";
 String actual = soundex.encode(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEncodeSingleCharacterString() {
 String input = "A";
 String expected = "A000";
 String actual = soundex.encode(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEncodeVariousStrings() {
 String[] inputs = {"Alphabet", "Example", "Test", "Soundex", "Encoder", "Testing", "Java", "Apache", "Commons"};
 String[] expecteds = {"A123", "E111", "T115", "S320", "E510", "T115", "J160", "S320", "C540"};
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String expected = expecteds[i];
 String actual = soundex.encode(input);
 Assert.assertEquals(expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeInvalidCharacter() {
 char invalidCharacter = 0;
 String input = Character.toString(invalidCharacter);
 String actual = soundex.encode(input);
 // Should not reach this point
 }

 @Test
 public void testDifferenceStringsWithNoSimilarity() {
 String s1 = "Apple";
 String s2 = "Banana";
 int difference = soundex.difference(s1, s2);
 Assert.assertEquals(0, difference);
 }

 @Test
 public void testDifferenceStringsWithSomeSimilarity() {
 String s1 = "Alphabet";
 String s2 = "Algorithms";
 int difference = soundex.difference(s1, s2);
 Assert.assertEquals(3, difference);
 }

 @Test
 public void testDifferenceStringsWithStrongSimilarity() {
 String s1 = "Alphabet";
 String s2 = "Alphabetization";
 int difference = soundex.difference(s1, s2);
 Assert.assertEquals(4, difference);
 }
}