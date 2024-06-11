import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();

    // Test cases for null or empty string input
    assertEquals("1111111111", caverphone.caverphone(null));
    assertEquals("1111111111", caverphone.caverphone(""));
    
    // Test cases for valid inputs
    assertEquals("1111111111", caverphone.caverphone("test"));
    assertEquals("A23A211111", caverphone.caverphone("hello"));
    assertEquals("K2FF211111", caverphone.caverphone("world"));
    assertEquals("FA31111111", caverphone.caverphone("caverphone"));
    assertEquals("2NG2111111", caverphone.caverphone("apache"));
    
    // Test cases for special characters
    assertEquals("TS21111111", caverphone.caverphone("12345"));
    assertEquals("T321111111", caverphone.caverphone("!@#$%"));
    assertEquals("3211111111", caverphone.caverphone("&&&***"));

    // Test cases for expected results
    assertEquals("1234567890", caverphone.caverphone("abcdefghijklmnopqrstuvwxyz"));
    assertEquals("E3NO21N111", caverphone.caverphone("ENCODERTOOLS"));
    assertEquals("S3R2111111", caverphone.caverphone("SOFTWARETESTING"));
    assertEquals("P321111111", caverphone.caverphone("PROGRAMMING"));
  }

  @Test
  public void testEncode() throws EncoderException {
    Caverphone caverphone = new Caverphone();

    // Test cases for null or non-string input
    try {
        caverphone.encode(null);
        fail("Expected EncoderException to be thrown");
    } catch (EncoderException e) {
        // Expected exception
    }

    try {
        caverphone.encode(123);
        fail("Expected EncoderException to be thrown");
    } catch (EncoderException e) {
        // Expected exception
    }

    // Test cases for valid inputs
    assertEquals("1111111111", caverphone.encode("test"));
    assertEquals("A23A211111", caverphone.encode("hello"));
    assertEquals("K2FF211111", caverphone.encode("world"));
  }

  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();

    // Test cases for equal caverphones
    assertTrue(caverphone.isCaverphoneEqual("test1", "test2"));
    assertTrue(caverphone.isCaverphoneEqual("hello1", "hello2"));
    assertTrue(caverphone.isCaverphoneEqual("world1", "world2"));
    assertTrue(caverphone.isCaverphoneEqual("caverphone1", "caverphone2"));
    
    // Test cases for non-equal caverphones
    assertFalse(caverphone.isCaverphoneEqual("test1", "hello2"));
    assertFalse(caverphone.isCaverphoneEqual("world1", "caverphone2"));
    assertFalse(caverphone.isCaverphoneEqual("hello1", "world2"));
  }

}