import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

  private Caverphone caverphone = new Caverphone();

  @Test
  public void testCaverphone() {
    Assert.assertEquals("3ngl3nd", caverphone.caverphone("England"));
    Assert.assertEquals("A", caverphone.caverphone("a"));
    Assert.assertEquals("1111111111", caverphone.caverphone(""));
    Assert.assertEquals("2m2r", caverphone.caverphone("Amor"));
    Assert.assertEquals("s2n", caverphone.caverphone("son"));
    Assert.assertEquals("3l3t3", caverphone.caverphone("elite"));
    Assert.assertEquals("whor3", caverphone.caverphone("whore"));
    Assert.assertEquals("2ntr3pr3n3ur", caverphone.caverphone("entrepreneur"));
  }

  @Test
  public void testEncode() throws EncoderException {
    Assert.assertEquals("3ngl3nd", caverphone.encode("England"));
    Assert.assertEquals("A", caverphone.encode("a"));
    Assert.assertEquals("1111111111", caverphone.encode(""));
    Assert.assertEquals("2m2r", caverphone.encode("Amor"));
    Assert.assertEquals("s2n", caverphone.encode("son"));
    Assert.assertEquals("3l3t3", caverphone.encode("elite"));
    Assert.assertEquals("whor3", caverphone.encode("whore"));
    Assert.assertEquals("2ntr3pr3n3ur", caverphone.encode("entrepreneur"));
  }

  @Test
  public void testIsCaverphoneEqual() {
    Assert.assertTrue(caverphone.isCaverphoneEqual("England", "England"));
    Assert.assertFalse(caverphone.isCaverphoneEqual("a", "A"));
    Assert.assertTrue(caverphone.isCaverphoneEqual("", ""));
    Assert.assertFalse(caverphone.isCaverphoneEqual("Amor", "Amor"));
    Assert.assertFalse(caverphone.isCaverphoneEqual("son", "son"));
    Assert.assertTrue(caverphone.isCaverphoneEqual("elite", "elite"));
    Assert.assertFalse(caverphone.isCaverphoneEqual("whore", "whore"));
    Assert.assertTrue(caverphone.isCaverphoneEqual("entrepreneur", "entrepreneur"));
  }

}