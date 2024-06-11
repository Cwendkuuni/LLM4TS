import org.junit.Test;
import org.junit.Assert;

public class SoundexTest {

  @Test
  public void testEncode() {
    Soundex soundex = new Soundex();
    
    String result = soundex.encode("Apache");
    
    Assert.assertEquals("A120", result);
  }
  
  @Test
  public void testDifference() throws EncoderException {
    Soundex soundex = new Soundex();
    
    int result = soundex.difference("Apache", "Abache");
    
    Assert.assertEquals(3, result);
  }
  
  @Test
  public void testSoundex() {
    Soundex soundex = new Soundex();
    
    String result = soundex.soundex("Apache");
    
    Assert.assertEquals("A120", result);
  }
    
}