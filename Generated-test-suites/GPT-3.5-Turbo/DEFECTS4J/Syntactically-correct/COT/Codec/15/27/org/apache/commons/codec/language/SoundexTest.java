import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {
  
  private Soundex soundex;
  
  @Before
  public void setUp() {
    soundex = new Soundex();
  }
  
  @Test
  public void testSoundex() {
    assertEquals("A260", soundex.soundex("Apache"));
    assertEquals("A260", soundex.soundex("apple"));
    assertEquals("A264", soundex.soundex("April"));
    assertEquals("B650", soundex.soundex("boy"));
  }
  
  @Test(expected = EncoderException.class)
  public void testDifferenceWithInvalidInput() throws EncoderException {
    soundex.difference("Apache", null);
  }
  
  @Test
  public void testDifference() throws EncoderException {
    assertEquals(0, soundex.difference("Apache", "Microsoft"));
    assertEquals(1, soundex.difference("Apache", "Apple"));
    assertEquals(3, soundex.difference("Sky", "Say"));
  }
  
  @Test(expected = EncoderException.class)
  public void testEncodeWithInvalidInput() throws EncoderException {
    soundex.encode(null);
  }
  
  @Test
  public void testEncode() throws EncoderException {
    assertEquals("A626", soundex.encode("Apache"));
    assertEquals("A626", soundex.encode("apache"));
    assertEquals("A264", soundex.encode("April"));
    assertEquals("B000", soundex.encode("boy"));
  }
}