import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {
  
  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();
    
    // Test empty string
    assertEquals("1111111111", caverphone.caverphone(""));
    
    // Test lowercase conversion
    assertEquals("test", caverphone.caverphone("TEST"));
    
    // Test removal of non-alphabetic characters
    assertEquals("foobar", caverphone.caverphone("foo bar!"));
    
    // Add more test cases here
    
  }
  
  @Test
  public void testEncode() {
    Caverphone caverphone = new Caverphone();
    
    // Test encoding object of type other than String
    try {
      caverphone.encode(123);
      fail("Should throw EncoderException");
    } catch (EncoderException e) {
      // Expected exception
    }
    
    // Add more test cases here
    
  }
  
  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();
    
    // Test equal caverphones
    assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
    
    // Test unequal caverphones
    assertFalse(caverphone.isCaverphoneEqual("foo", "bar"));
    
    // Add more test cases here
    
  }
}