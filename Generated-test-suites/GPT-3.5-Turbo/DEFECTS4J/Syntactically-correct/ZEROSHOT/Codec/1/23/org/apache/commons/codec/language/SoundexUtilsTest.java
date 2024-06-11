import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {
  
  // Test clean() method
  
  @Test
  public void testCleanEmptyString() {
    assertEquals("", SoundexUtils.clean(""));
  }
  
  @Test
  public void testCleanNullString() {
    assertNull(SoundexUtils.clean(null));
  }
  
  @Test
  public void testCleanAllLowerCase() {
    assertEquals("TEST", SoundexUtils.clean("test"));
  }
  
  @Test
  public void testCleanMixedCase() {
    assertEquals("TEST", SoundexUtils.clean("TeSt"));
  }
  
  @Test
  public void testCleanSpecialCharacters() {
    assertEquals("TEST", SoundexUtils.clean("T!@#$%^&*,.;{}[]()eSt"));
  }
 
  // Test difference() method
  
  @Test
  public void testDifferenceSameString() throws EncoderException {
    StringEncoder encoder = new Soundex();
    assertEquals(4, SoundexUtils.difference(encoder, "test", "test"));
  }
  
  @Test
  public void testDifferenceDifferentString() throws EncoderException {
    StringEncoder encoder = new Soundex();
    assertEquals(2, SoundexUtils.difference(encoder, "test", "best"));
  }
  
  @Test(expected = EncoderException.class)
  public void testDifferenceInvalidString() throws EncoderException {
    StringEncoder encoder = new Soundex();
    SoundexUtils.difference(encoder, "test1", "test2");
  }
  
  // Test differenceEncoded() method
  
  @Test
  public void testDifferenceEncodedSameString() {
    assertEquals(4, SoundexUtils.differenceEncoded("T234", "T234"));
  }
  
  @Test
  public void testDifferenceEncodedDifferentString() {
    assertEquals(2, SoundexUtils.differenceEncoded("T234", "B234"));
  }
  
  @Test
  public void testDifferenceEncodedDifferentLength() {
    assertEquals(3, SoundexUtils.differenceEncoded("T234", "T23"));
  }
  
  @Test
  public void testDifferenceEncodedNullString() {
    assertEquals(0, SoundexUtils.differenceEncoded(null, "T234"));
  }
  
  // Additional tests
  
  @Test
  public void testCleanNoLetters() {
    assertEquals("", SoundexUtils.clean("!@#$%^&*()1234567890"));
  }
  
  @Test
  public void testDifferenceEncodedEmptyString() {
    assertEquals(0, SoundexUtils.differenceEncoded("", "T234"));
  }
  
  @Test
  public void testCleanUpperCase() {
    assertEquals("TEST", SoundexUtils.clean("TEST"));
  }
  
  @Test
  public void testDifferenceEncodedEmptyStrings() {
    assertEquals(0, SoundexUtils.differenceEncoded("", ""));
  }
  
  // Add more tests if needed
  
}