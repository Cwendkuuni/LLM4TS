import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {
  
  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();
    
    String result = caverphone.caverphone("example");
    
    Assert.assertEquals("1XB1111111", result);
  }
  
  @Test(expected = EncoderException.class)
  public void testEncodeWithInvalidType() throws EncoderException {
    Caverphone caverphone = new Caverphone();
    
    Object object = new Object();
    caverphone.encode(object);
  }
  
  @Test
  public void testEncode() {
    Caverphone caverphone = new Caverphone();
    
    String result = caverphone.encode("example");
    
    Assert.assertEquals("1XB1111111", result);
  }
  
  @Test
  public void testIsCaverphoneEqualWithEqualStrings() {
    Caverphone caverphone = new Caverphone();
    
    boolean result = caverphone.isCaverphoneEqual("example", "example");
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsCaverphoneEqualWithDifferentStrings() {
    Caverphone caverphone = new Caverphone();
    
    boolean result = caverphone.isCaverphoneEqual("example1", "example2");
    
    Assert.assertFalse(result);
  }
}