import org.junit.Test;
import org.junit.Assert;

public class CaverphoneTest {

  @Test
  public void testCaverphoneWithNull() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone(null);
    Assert.assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphoneWithEmptyString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("");
    Assert.assertEquals("1111111111", result);
  }

  @Test
  public void testCaverphoneWithLowerCaseString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("test");
    Assert.assertEquals("tst1111111", result);
  }

  @Test
  public void testCaverphoneWithMixedCaseString() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("TeSt");
    Assert.assertEquals("tst1111111", result);
  }

  @Test
  public void testCaverphoneWithSpecialCharacters() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("t!e@s#t$");
    Assert.assertEquals("tst1111111", result);
  }

  @Test
  public void testCaverphoneWithStartOptions() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("coughtest");
    Assert.assertEquals("cou2fts111", result);
    result = caverphone.caverphone("roughtest");
    Assert.assertEquals("rou2fts111", result);
    result = caverphone.caverphone("toughtest");
    Assert.assertEquals("tou2fts111", result);
    result = caverphone.caverphone("enoughtest");
    Assert.assertEquals("enou2fts111", result);
    result = caverphone.caverphone("troughtest");
    Assert.assertEquals("trou2fts111", result);
    result = caverphone.caverphone("gntest");
    Assert.assertEquals("2ntest1111", result);
    result = caverphone.caverphone("mbtest");
    Assert.assertEquals("m2test1111", result);
  }

  @Test
  public void testCaverphoneWithReplacements() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.caverphone("cqtest");
    Assert.assertEquals("2qtest1111", result);
    result = caverphone.caverphone("citest");
    Assert.assertEquals("sitest1111", result);
    result = caverphone.caverphone("cetest");
    Assert.assertEquals("setest1111", result);
    result = caverphone.caverphone("cytest");
    Assert.assertEquals("sytest1111", result);
    result = caverphone.caverphone("tchtest");
    Assert.assertEquals("2chtest111", result);
    result = caverphone.caverphone("ctest");
    Assert.assertEquals("ktest1111", result);
    result = caverphone.caverphone("qtest");
    Assert.assertEquals("ktest1111", result);
    result = caverphone.caverphone("xtest");
    Assert.assertEquals("ktest1111", result);
    result = caverphone.caverphone("vtest");
    Assert.assertEquals("ftest1111", result);
    result = caverphone.caverphone("dgtest");
    Assert.assertEquals("2gtest1111", result);
    result = caverphone.caverphone("tiotest");
    Assert.assertEquals("siotest111", result);
    result = caverphone.caverphone("tiatest");
    Assert.assertEquals("siatest111", result);
    result = caverphone.caverphone("dtest");
    Assert.assertEquals("ttest1111", result);
    result = caverphone.caverphone("phtest");
    Assert.assertEquals("fhtest1111", result);
    result = caverphone.caverphone("btest");
    Assert.assertEquals("ptest1111", result);
    result = caverphone.caverphone("shtest");
    Assert.assertEquals("s2test1111", result);
    result = caverphone.caverphone("ztest");
    Assert.assertEquals("stest1111", result);
    result = caverphone.caverphone("atest");
    Assert.assertEquals("Atest11111", result);
    result = caverphone.caverphone("etest");
    Assert.assertEquals("3test11111", result);
    result = caverphone.caverphone("jtest");
    Assert.assertEquals("Ytest11111", result);
    result = caverphone.caverphone("y3test");
    Assert.assertEquals("Y3test1111", result);
    result = caverphone.caverphone("ytest");
    Assert.assertEquals("Atest11111", result);
    result = caverphone.caverphone("3test");
    Assert.assertEquals("3test11111", result);
    result = caverphone.caverphone("3gh3test");
    Assert.assertEquals("3kh3test11", result);
    result = caverphone.caverphone("ghtest");
    Assert.assertEquals("22test1111", result);
    result = caverphone.caverphone("gtest");
    Assert.assertEquals("ktest1111", result);
    result = caverphone.caverphone("sstest");
    Assert.assertEquals("Stest11111", result);
    result = caverphone.caverphone("ttest");
    Assert.assertEquals("Ttest11111", result);
    result = caverphone.caverphone("ptest");
    Assert.assertEquals("Ptest11111", result);
    result = caverphone.caverphone("ktest");
    Assert.assertEquals("Ktest11111", result);
    result = caverphone.caverphone("ftest");
    Assert.assertEquals("Ftest11111", result);
    result = caverphone.caverphone("mtest");
    Assert.assertEquals("Mtest11111", result);
    result = caverphone.caverphone("ntest");
    Assert.assertEquals("Ntest11111", result);
    result = caverphone.caverphone("w3test");
    Assert.assertEquals("W3test1111", result);
    result = caverphone.caverphone("whtest");
    Assert.assertEquals("Wh3test1111", result);
    result = caverphone.caverphone("wtest");
    Assert.assertEquals("2test11111", result);
    result = caverphone.caverphone("htest");
    Assert.assertEquals("Atest11111", result);
    result = caverphone.caverphone("r3test");
    Assert.assertEquals("R3test1111", result);
    result = caverphone.caverphone("rtest");
    Assert.assertEquals("2test11111", result);
    result = caverphone.caverphone("l3test");
    Assert.assertEquals("L3test1111", result);
    result = caverphone.caverphone("ltest");
    Assert.assertEquals("2test11111", result);
  }

  @Test
  public void testEncodeWithNonStringObject() {
    Caverphone caverphone = new Caverphone();
    try {
      Object object = new Object();
      String result = (String) caverphone.encode(object);
    } catch (EncoderException e) {
      Assert.assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
    }
  }

  @Test
  public void testEncodeWithStringObject() {
    Caverphone caverphone = new Caverphone();
    String result = caverphone.encode("test");
    Assert.assertEquals("tst1111111", result);
  }

  @Test
  public void testIsCaverphoneEqualWithEqualStrings() {
    Caverphone caverphone = new Caverphone();
    boolean result = caverphone.isCaverphoneEqual("test", "test");
    Assert.assertTrue(result);
  }

  @Test
  public void testIsCaverphoneEqualWithDifferentStrings() {
    Caverphone caverphone = new Caverphone();
    boolean result = caverphone.isCaverphoneEqual("test", "different");
    Assert.assertFalse(result);
  }
}