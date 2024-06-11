import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

   @Test
   public void testClean() {
      // Test with empty string
      String str = "";
      String expected = "";
      String result = SoundexUtils.clean(str);
      assertEquals(expected, result);
      
      // Test with string containing only non-letter characters
      str = "123%$#";
      expected = "";
      result = SoundexUtils.clean(str);
      assertEquals(expected, result);
      
      // Test with string containing upper and lower case letters
      str = "abCDeFgH";
      expected = "ABCDEFGH";
      result = SoundexUtils.clean(str);
      assertEquals(expected, result);
      
      // Test with string containing spaces and special characters
      str = "!@#$%^&*() Hello, World!";
      expected = "HELLOWORLD";
      result = SoundexUtils.clean(str);
      assertEquals(expected, result);
   }
   
   @Test
   public void testDifference() throws EncoderException {
      StringEncoder encoder = new Soundex();
      
      // Test with two equal strings
      String s1 = "hello";
      String s2 = "hello";
      int expected = 4;
      int result = SoundexUtils.difference(encoder, s1, s2);
      assertEquals(expected, result);
      
      // Test with two similar strings
      s1 = "hello";
      s2 = "hall";
      expected = 1;
      result = SoundexUtils.difference(encoder, s1, s2);
      assertEquals(expected, result);
      
      // Test with two completely different strings
      s1 = "hello";
      s2 = "world";
      expected = 0;
      result = SoundexUtils.difference(encoder, s1, s2);
      assertEquals(expected, result);
      
      // Test with empty strings
      s1 = "";
      s2 = "";
      expected = 0;
      result = SoundexUtils.difference(encoder, s1, s2);
      assertEquals(expected, result);
   }
   
   @Test
   public void testDifferenceEncoded() {
      // Test with two equal encoded strings
      String es1 = "H400";
      String es2 = "H400";
      int expected = 4;
      int result = SoundexUtils.differenceEncoded(es1, es2);
      assertEquals(expected, result);
      
      // Test with two similar encoded strings
      es1 = "H400";
      es2 = "H401";
      expected = 3;
      result = SoundexUtils.differenceEncoded(es1, es2);
      assertEquals(expected, result);
      
      // Test with two completely different encoded strings
      es1 = "H400";
      es2 = "W500";
      expected = 0;
      result = SoundexUtils.differenceEncoded(es1, es2);
      assertEquals(expected, result);
      
      // Test with empty strings
      es1 = "";
      es2 = "";
      expected = 0;
      result = SoundexUtils.differenceEncoded(es1, es2);
      assertEquals(expected, result);
   }

}