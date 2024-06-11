import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest {
  
  StringEncoder encoder;
  
  @Before
  public void setUp() {
    encoder = new SoundexEncoder();  // Assuming SoundexEncoder is implemented
  }
  
  @Test
  public void testClean() {
    assertEquals("ABCD", SoundexUtils.clean("  a.b-Cd  "));
  }
  
  @Test
  public void testCleanWithEmptyString() {
    assertEquals("", SoundexUtils.clean(""));
  }
  
  @Test
  public void testCleanWithNullString() {
    assertNull(SoundexUtils.clean(null));
  }

  @Test
  public void testDifference() throws EncoderException {
    assertEquals(2, SoundexUtils.difference(encoder, "Hello", "Helo"));
  }
  
  @Test
  public void testDifferenceWithNullEncoder() throws EncoderException {
    assertNull(SoundexUtils.difference(null, "Hello", "Helo"));
  }
  
  @Test
  public void testDifferenceWithNullS1() throws EncoderException {
    assertEquals(0, SoundexUtils.difference(encoder, null, "Helo"));
  }
  
  @Test
  public void testDifferenceWithNullS2() throws EncoderException {
    assertEquals(0, SoundexUtils.difference(encoder, "Hello", null));
  }
  
  @Test
  public void testDifferenceWithEmptyS1() throws EncoderException {
    assertEquals(0, SoundexUtils.difference(encoder, "", "Helo"));
  }
  
  @Test
  public void testDifferenceWithEmptyS2() throws EncoderException {
    assertEquals(0, SoundexUtils.difference(encoder, "Hello", ""));
  }
  
  @Test
  public void testDifferenceEncoded() {
    assertEquals(2, SoundexUtils.differenceEncoded("AH45", "AH56"));
  }
  
  @Test
  public void testDifferenceEncodedWithNullS1() {
    assertEquals(0, SoundexUtils.differenceEncoded(null, "AH56"));
  }
  
  @Test
  public void testDifferenceEncodedWithNullS2() {
    assertEquals(0, SoundexUtils.differenceEncoded("AH45", null));
  }
  
  @Test
  public void testDifferenceEncodedWithEmptyS1() {
    assertEquals(0, SoundexUtils.differenceEncoded("", "AH56"));
  }
  
  @Test
  public void testDifferenceEncodedWithEmptyS2() {
    assertEquals(0, SoundexUtils.differenceEncoded("AH45", ""));
  }
  
  @Test
  public void testDifferenceEncodedWithDifferentLengthStrings() {
    assertEquals(3, SoundexUtils.differenceEncoded("AH45", "AH5"));
  }
  
  @Test
  public void testDifferenceEncodedWithEqualStrings() {
    assertEquals(4, SoundexUtils.differenceEncoded("AH45", "AH45"));
  }

}