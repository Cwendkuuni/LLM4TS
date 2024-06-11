import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {

  @Test
  public void testCaverphone_typicalCase() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("typicalCase");
    Assert.assertEquals("tbpklKSYW3", result);
  }

  @Test
  public void testCaverphone_emptyString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("");
    Assert.assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphone_nullString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone(null);
    Assert.assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphone_edgeCase1() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("aeiouy");
    Assert.assertEquals("K333333331", result);
  }

  @Test
  public void testCaverphone_edgeCase2() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("w");
    Assert.assertEquals("2", result);
  }

  @Test
  public void testCaverphone_edgeCase3() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("why");
    Assert.assertEquals("7A4", result);
  }

  @Test
  public void testCaverphone_errorScenario1() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("WHY");
    Assert.assertEquals("A4445542A1", result);
  }

  @Test
  public void testEncode_typicalCase() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.encode("typicalCase");
    Assert.assertEquals("tbpklKSYW3", result);
  }

  @Test(expected = EncoderException.class)
  public void testEncode_errorScenario1() {
    Caverphone caverphone = new Caverphone();
    caverphone.encode(12345);
  }

  @Test
  public void testIsCaverphoneEqual_trueCase() {
    Caverphone caverphone = new Caverphone();
    boolean result = caverphone.isCaverphoneEqual("hello", "hella");
    Assert.assertTrue(result);
  }

  @Test
  public void testIsCaverphoneEqual_falseCase() {
    Caverphone caverphone = new Caverphone();
    boolean result = caverphone.isCaverphoneEqual("hello", "world");
    Assert.assertFalse(result);
  }

}