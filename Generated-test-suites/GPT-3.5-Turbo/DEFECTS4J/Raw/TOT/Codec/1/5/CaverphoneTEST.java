import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();

    // Typical use case
    assertEquals("R111111111", caverphone.caverphone("Robert"));
    
    // Edge case: Empty string
    assertEquals("1111111111", caverphone.caverphone(""));
    
    // Edge case: Null input
    assertEquals("1111111111", caverphone.caverphone(null));
    
    // Error scenario: String with non-alphabetic characters
    assertEquals("S111111111", caverphone.caverphone("1234!@#$%^&*()"));
    
    // Error scenario: String containing numbers
    assertEquals("M111111111", caverphone.caverphone("James1234"));
    
    // Error scenario: String containing special characters
    assertEquals("T111111111", caverphone.caverphone("John$#"));
    
    // Edge case: Maximum string length
    assertEquals("R111111111", caverphone.caverphone("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    
    // Edge case: Single character string
    assertEquals("A111111111", caverphone.caverphone("a"));
  }

  @Test
  public void testEncode() {
    Caverphone caverphone = new Caverphone();

    try {
      // Typical use case
      assertEquals("R111111111", caverphone.encode("Robert"));
      
      // Error scenario: Object of type other than String
      caverphone.encode(1234);
      fail("Expected EncoderException");
    } catch (EncoderException e) {
      // Exception expected, do nothing
    }
  }

  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();

    // Typical use case
    assertTrue(caverphone.isCaverphoneEqual("Robert", "Robert"));
    
    // Typical use case with different inputs
    assertFalse(caverphone.isCaverphoneEqual("Robert", "John"));
    
    // Edge case: Empty input
    assertTrue(caverphone.isCaverphoneEqual("", ""));
    
    // Error scenario: Null input
    assertFalse(caverphone.isCaverphoneEqual(null, "John"));
  }

}