import org.junit.Test;

public class SoundexTest {

   @Test
   public void testDifference() throws Exception {
      Soundex soundex = new Soundex();
      String s1 = "abc";
      String s2 = "def";
      int result = soundex.difference(s1, s2);
      assertEquals(0, result);
   }

   @Test
   public void testEncode() throws Exception {
      Soundex soundex = new Soundex();
      String input = "hello";
      String result = soundex.encode(input);
      assertEquals("H400", result);
   }

   @Test
   public void testSoundex() throws Exception {
      Soundex soundex = new Soundex();
      String input = "testing";
      String result = soundex.soundex(input);
      assertEquals("T234", result);
   }

}