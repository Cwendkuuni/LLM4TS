import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexTest {

  @Test
  public void testDifference() {
    Soundex soundex = new Soundex();
    try {
      int result = soundex.difference("TEST", "TEXT");
      Assert.assertEquals(2, result);
      result = soundex.difference("HELLO", "HALO");
      Assert.assertEquals(3, result);
      result = soundex.difference("JAVA", "JAPA");
      Assert.assertEquals(3, result);
    } catch (EncoderException e) {
      Assert.fail("Should not throw EncoderException");
    }
  }

  @Test
  public void testEncodeObject() {
    Soundex soundex = new Soundex();
    try {
      Object result = soundex.encode("TEST");
      Assert.assertEquals("T230", result);
      result = soundex.encode("HELLO");
      Assert.assertEquals("H400", result);
      result = soundex.encode("JAVA");
      Assert.assertEquals("J100", result);
    } catch (EncoderException e) {
      Assert.fail("Should not throw EncoderException");
    }
  }

  @Test
  public void testEncodeString() {
    Soundex soundex = new Soundex();
    String result = soundex.encode("TEST");
    Assert.assertEquals("T230", result);
    result = soundex.encode("HELLO");
    Assert.assertEquals("H400", result);
    result = soundex.encode("JAVA");
    Assert.assertEquals("J100", result);
  }

  @Test
  public void testSoundex() {
    Soundex soundex = new Soundex();
    String result = soundex.soundex("TEST");
    Assert.assertEquals("T230", result);
    result = soundex.soundex("HELLO");
    Assert.assertEquals("H400", result);
    result = soundex.soundex("JAVA");
    Assert.assertEquals("J100", result);
  }

}