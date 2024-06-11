import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

public class CaverphoneTest {

  private Caverphone caverphone;

  @Before
  public void setUp() {
    caverphone = new Caverphone();
  }

  @Test
  public void testCaverphone_withNullInput_shouldReturn1111111111() {
    String result = caverphone.caverphone(null);
    assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphone_withEmptyInput_shouldReturn1111111111() {
    String result = caverphone.caverphone("");
    assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphone_withLowerCaseInput_shouldReturnCaverphoneCode() {
    String result = caverphone.caverphone("hello");
    assertEquals("HL11111111", result);
  }
  
  // Edge Case: Minimum input length
  @Test
  public void testCaverphone_withSingleCharacterInput_shouldReturnCaverphoneCode() {
    String result = caverphone.caverphone("a");
    assertEquals("A111111111", result);
  }
  
  // Edge Case: Maximum input length
  @Test
  public void testCaverphone_withMaximumLengthInput_shouldReturnCaverphoneCode() {
    String input = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
    String result = caverphone.caverphone(input);
    assertEquals("2211111111", result);
  }

  @Test(expected = EncoderException.class)
  public void testEncode_withNonStringObject_shouldThrowEncoderException() throws EncoderException {
    caverphone.encode(Integer.valueOf(123));
  }

  @Test
  public void testEncode_withStringInput_shouldReturnCaverphoneCode() throws EncoderException {
    String result = caverphone.encode("hello");
    assertEquals("HL11111111", result);
  }

  @Test
  public void testIsCaverphoneEqual_withEqualStrings_shouldReturnTrue() {
    boolean result = caverphone.isCaverphoneEqual("hello", "HELLO");
    assertTrue(result);
  }

  @Test
  public void testIsCaverphoneEqual_withDifferentStrings_shouldReturnFalse() {
    boolean result = caverphone.isCaverphoneEqual("hello", "goodbye");
    assertFalse(result);
  }
}