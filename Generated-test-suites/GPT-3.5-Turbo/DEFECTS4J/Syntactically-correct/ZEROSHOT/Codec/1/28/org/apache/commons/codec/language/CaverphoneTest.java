import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();

    // Test empty string
    String result1 = caverphone.caverphone("");
    assertEquals("1111111111", result1);

    // Test string with only digits
    String result2 = caverphone.caverphone("1234");
    assertEquals("1111111111", result2);

    // Test string with only special characters
    String result3 = caverphone.caverphone("!@#$");
    assertEquals("1111111111", result3);

    // Test string with only uppercase letters
    String result4 = caverphone.caverphone("ABCDEFG");
    assertEquals("1111111111", result4);

    // Test string with all lowercase letters
    String result5 = caverphone.caverphone("hello");
    assertEquals("HL11111111", result5);

    // Test string with mixed case letters
    String result6 = caverphone.caverphone("HelLO");
    assertEquals("HL11111111", result6);

    // Test string with special characters and numbers
    String result7 = caverphone.caverphone("Hello123!");
    assertEquals("HL11111111", result7);

    // Test string with various start options
    String result8 = caverphone.caverphone("coughabc");
    assertEquals("C2FABC1111", result8);

    String result9 = caverphone.caverphone("roughabc");
    assertEquals("RU2FABC111", result9);

    String result10 = caverphone.caverphone("toughabc");
    assertEquals("TO2FABC111", result10);
  }

  @Test
  public void testEncode() {
    Caverphone caverphone = new Caverphone();

    // Test invalid input type
    try {
      caverphone.encode(1234);
      fail("EncoderException should have been thrown");
    } catch (EncoderException e) {
      // Expected exception
    }
    
    // Test valid input type
    String result = caverphone.encode("hello");
    assertEquals("HL11111111", result);
  }

  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();

    // Test equal strings
    boolean result1 = caverphone.isCaverphoneEqual("hello", "hello");
    assertTrue(result1);

    // Test different strings
    boolean result2 = caverphone.isCaverphoneEqual("hello", "world");
    assertFalse(result2);

    // Test equal strings with different cases
    boolean result3 = caverphone.isCaverphoneEqual("hello", "HeLlO");
    assertTrue(result3);
  }

}