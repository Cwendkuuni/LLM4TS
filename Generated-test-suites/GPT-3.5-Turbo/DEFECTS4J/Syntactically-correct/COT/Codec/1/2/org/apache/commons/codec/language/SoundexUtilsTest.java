import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest {

  private static final StringEncoder soundexEncoder = new Soundex();
  private static final StringEncoder refinedSoundexEncoder = new RefinedSoundex();

  @Test
  public void testClean() {
    // Basic test
    assertEquals("CLEAN", SoundexUtils.clean("clean"));
    
    // Test with digits and special characters
    assertEquals("STRIPDIGITANDSPECIALCHARACTERS", SoundexUtils.clean("s$tr9&&%ip d!g1&ts"));
    
    // Test with non-English characters
    assertEquals("NONENGLISHCHARACTERS", SoundexUtils.clean("nøn-êñglîsħ chæř∂çtⱥè®s"));
    
    // Test with empty string
    assertEquals("", SoundexUtils.clean(""));
    
    // Test with null input
    assertNull(SoundexUtils.clean(null));
  }

  @Test
  public void testDifference() throws EncoderException {
    // Basic test
    assertEquals(4, SoundexUtils.difference(soundexEncoder, "hello", "hello"));

    // Test with different strings
    assertEquals(0, SoundexUtils.difference(soundexEncoder, "abcde", "fghij"));

    // Test with refined soundex
    assertEquals(7, SoundexUtils.difference(refinedSoundexEncoder, "hello", "hello"));

    // Test with empty strings
    assertEquals(0, SoundexUtils.difference(soundexEncoder, "", ""));

    // Test with null input
    try {
      SoundexUtils.difference(soundexEncoder, null, "hello");
      fail("Expected EncoderException");
    } catch (EncoderException e) {
      // Expected exception
    }
  }

  @Test
  public void testDifferenceEncoded() {
    // Basic test
    assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
    
    // Test with different strings
    assertEquals(0, SoundexUtils.differenceEncoded("1234", "5678"));
    
    // Test with empty strings
    assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    
    // Test with null input
    assertEquals(0, SoundexUtils.differenceEncoded(null, null));
  }

}