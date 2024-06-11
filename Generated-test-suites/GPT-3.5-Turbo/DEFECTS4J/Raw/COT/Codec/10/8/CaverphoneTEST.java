import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {
  
  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("example");
    Assert.assertEquals("3mxmpl1111", result);
  }
  
  @Test(expected = NullPointerException.class)
  public void testCaverphoneNull() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone(null);
  }

  @Test
  public void testCaverphoneEmpty() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("");
    Assert.assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphoneLowerCase() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("EXAMPLE");
    Assert.assertEquals("3mxmpl1111", result);
  }
  
  @Test
  public void testEncode() throws EncoderException {
    Caverphone caverphone = new Caverphone();
    String result = (String) caverphone.encode("example");
    Assert.assertEquals("3mxmpl1111", result);
  }
  
  @Test(expected = EncoderException.class)
  public void testEncodeInvalidType() throws EncoderException {
    Caverphone caverphone = new Caverphone();
    Integer result = (Integer) caverphone.encode(123);
  }
  
  @Test
  public void testEncodeString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.encode("example");
    Assert.assertEquals("3mxmpl1111", result);
  }
  
  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();
    boolean result = caverphone.isCaverphoneEqual("example1", "example2");
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsCaverphoneNotEqual() {
    Caverphone caverphone = new Caverphone();
    boolean result = caverphone.isCaverphoneEqual("example1", "example3");
    Assert.assertFalse(result);
  }
  
}