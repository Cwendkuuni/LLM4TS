import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

  @Test
  public void testCaverphoneEmptyString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("");
    Assert.assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphoneLowercase() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("TEST");
    Assert.assertEquals("TST1111111", result);
  }

  @Test
  public void testCaverphoneRemoveNonAlpha() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("@#te$st%");
    Assert.assertEquals("TST1111111", result);
  }

  // Additional test methods for other Caverphone functionalities

  @Test
  public void testEncodeString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.encode("test");
    Assert.assertEquals("TST1111111", result);
  }

  @Test
  public void testEncodeObject() throws EncoderException {
    Caverphone caverphone = new Caverphone();
    String result = (String) caverphone.encode((Object) "test");
    Assert.assertEquals("TST1111111", result);
  }

  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();
    boolean result = caverphone.isCaverphoneEqual("test", "TEST");
    Assert.assertTrue(result);
  }
}