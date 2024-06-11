import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
   
   @Test
   public void testEncode() {
      Soundex soundex = new Soundex();
      
      // Basic test case
      String input1 = "John";
      String expectedOutput1 = "J500";
      assertEquals(expectedOutput1, soundex.encode(input1));
      
      // Test case with no mappings
      String input2 = "XYZ";
      String expectedOutput2 = "X000";
      assertEquals(expectedOutput2, soundex.encode(input2));
      
      // Test case with multiple consecutive characters that map to the same code
      String input3 = "BBB";
      String expectedOutput3 = "B000";
      assertEquals(expectedOutput3, soundex.encode(input3));
      
      // Test case with special characters
      String input4 = "!@#$";
      String expectedOutput4 = "!000";
      assertEquals(expectedOutput4, soundex.encode(input4));
   }
   
   @Test
   public void testDifference() throws EncoderException {
      Soundex soundex = new Soundex();
      
      // Basic test case with similar strings
      String input1 = "John";
      String input2 = "Jon";
      int expectedOutput1 = 3;
      assertEquals(expectedOutput1, soundex.difference(input1, input2));
      
      // Test case with no similar characters
      String input3 = "ABC";
      String input4 = "DEF";
      int expectedOutput2 = 0;
      assertEquals(expectedOutput2, soundex.difference(input3, input4));
      
      // Test case with all similar characters
      String input5 = "AAA";
      String input6 = "AAA";
      int expectedOutput3 = 4;
      assertEquals(expectedOutput3, soundex.difference(input5, input6));
   }
   
   @Test(expected = EncoderException.class)
   public void testEncodeWithNonStringInput() throws EncoderException {
      Soundex soundex = new Soundex();
      Integer input = 123;
      soundex.encode(input);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testGetMappingCodeWithInvalidCharacter() {
      Soundex soundex = new Soundex();
      char invalidChar = ' ';
      soundex.getMappingCode("TEST", 0);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testMapWithUnmappedCharacter() {
      Soundex soundex = new Soundex();
      char unmappedChar = '!';
      soundex.map(unmappedChar);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testSoundexWithInvalidCharacter() {
      Soundex soundex = new Soundex();
      String input = "AB@C";
      soundex.soundex(input);
   }
}