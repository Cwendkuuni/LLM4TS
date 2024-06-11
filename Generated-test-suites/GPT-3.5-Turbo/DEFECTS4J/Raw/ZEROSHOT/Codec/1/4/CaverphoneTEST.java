import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {
  
  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();
    
    // Test empty string
    String result = caverphone.caverphone("");
    Assert.assertEquals("1111111111", result);
    
    // Test lowercase conversion
    result = caverphone.caverphone("AbcDeFg");
    Assert.assertEquals("Abcdefg111", result);
    
    // Test removing non-alphabetic characters
    result = caverphone.caverphone("abc123def456");
    Assert.assertEquals("abcdef1111", result);
    
    // Test removing final "e"
    result = caverphone.caverphone("applee");
    Assert.assertEquals("appl111111", result);
    
    // Test start options
    result = caverphone.caverphone("coughtest");
    Assert.assertEquals("cou2ftst11", result);
    
    result = caverphone.caverphone("roughtest");
    Assert.assertEquals("rou2ftst11", result);
    
    result = caverphone.caverphone("toughtest");
    Assert.assertEquals("tou2ftst11", result);
    
    result = caverphone.caverphone("enoughtest");
    Assert.assertEquals("enou2ftst11", result);
    
    result = caverphone.caverphone("gnword");
    Assert.assertEquals("2nword1111", result);
    
    result = caverphone.caverphone("mbword");
    Assert.assertEquals("m2word1111", result);
    
    // Test replacements
    result = caverphone.caverphone("cqsentence");
    Assert.assertEquals("2qsntnc1111", result);
    
    result = caverphone.caverphone("cisecond");
    Assert.assertEquals("sisnd11111", result);
    
    result = caverphone.caverphone("cestreet");
    Assert.assertEquals("sestrt11111", result);
    
    result = caverphone.caverphone("cystreet");
    Assert.assertEquals("systrt11111", result);
    
    result = caverphone.caverphone("tcheese");
    Assert.assertEquals("2chss11111", result);
    
    result = caverphone.caverphone("ccamel");
    Assert.assertEquals("kkm2l11111", result);
    
    result = caverphone.caverphone("qquad");
    Assert.assertEquals("kkwd111111", result);
    
    result = caverphone.caverphone("xlax");
    Assert.assertEquals("kkks111111", result);
    
    result = caverphone.caverphone("vvalley");
    Assert.assertEquals("fll11111111", result);
    
    result = caverphone.caverphone("dgood");
    Assert.assertEquals("tgd11111111", result);
    
    result = caverphone.caverphone("tioparty");
    Assert.assertEquals("siprt111111", result);
    
    result = caverphone.caverphone("tiaparty");
    Assert.assertEquals("siaprty1111", result);
    
    result = caverphone.caverphone("dword");
    Assert.assertEquals("tword111111", result);
    
    result = caverphone.caverphone("phword");
    Assert.assertEquals("fhword11111", result);
    
    result = caverphone.caverphone("bbrace");
    Assert.assertEquals("pr2c1111111", result);
    
    result = caverphone.caverphone("shoe");
    Assert.assertEquals("s2e11111111", result);
    
    result = caverphone.caverphone("zoo");
    Assert.assertEquals("so111111111", result);
    
    result = caverphone.caverphone("apple");
    Assert.assertEquals("A1111111111", result);
    
    result = caverphone.caverphone("cat");
    Assert.assertEquals("3t111111111", result);
    
    result = caverphone.caverphone("joystick");
    Assert.assertEquals("y3st3k11111", result);
    
    result = caverphone.caverphone("y3ard");
    Assert.assertEquals("Y3rd1111111", result);
    
    result = caverphone.caverphone("young");
    Assert.assertEquals("A1111111111", result);
    
    result = caverphone.caverphone("yellow");
    Assert.assertEquals("31111111111", result);
    
    result = caverphone.caverphone("3ght3");
    Assert.assertEquals("3kh3111111", result);
    
    result = caverphone.caverphone("ghoul");
    Assert.assertEquals("22ul1111111", result);
    
    result = caverphone.caverphone("grape");
    Assert.assertEquals("krap1111111", result);
    
    result = caverphone.caverphone("sheep");
    Assert.assertEquals("s2p11111111", result);
    
    result = caverphone.caverphone("teet");
    Assert.assertEquals("t1111111111", result);
    
    result = caverphone.caverphone("peep");
    Assert.assertEquals("p1111111111", result);
    
    result = caverphone.caverphone("keys");
    Assert.assertEquals("k1111111111", result);
    
    result = caverphone.caverphone("food");
    Assert.assertEquals("f1111111111", result);
    
    result = caverphone.caverphone("mees");
    Assert.assertEquals("m1111111111", result);
    
    result = caverphone.caverphone("neen");
    Assert.assertEquals("n1111111111", result);
    
    result = caverphone.caverphone("woo");
    Assert.assertEquals("w3111111111", result);
    
    result = caverphone.caverphone("whip");
    Assert.assertEquals("Wh311111111", result);
    
    result = caverphone.caverphone("now");
    Assert.assertEquals("31211111111", result);
    
    result = caverphone.caverphone("wo");
    Assert.assertEquals("32111111111", result);
    
    result = caverphone.caverphone("humble");
    Assert.assertEquals("A2bl1111111", result);
    
    result = caverphone.caverphone("house");
    Assert.assertEquals("2s111111111", result);
    
    result = caverphone.caverphone("read");
    Assert.assertEquals("R1111111111", result);
    
    result = caverphone.caverphone("rack");
    Assert.assertEquals("2k111111111", result);
    
    result = caverphone.caverphone("leek");
    Assert.assertEquals("L1111111111", result);
    
    result = caverphone.caverphone("lake");
    Assert.assertEquals("21111111111", result);
  }
  
  @Test
  public void testEncode() {
    Caverphone caverphone = new Caverphone();
    
    // Test empty string
    String result = caverphone.encode("");
    Assert.assertEquals("1111111111", result);
    
    // Test lowercase conversion
    result = caverphone.encode("AbcDeFg");
    Assert.assertEquals("Abcdefg111", result);
  }
  
  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();
    
    boolean result = caverphone.isCaverphoneEqual("apple", "aple");
    Assert.assertTrue(result);
    
    result = caverphone.isCaverphoneEqual("apple", "orange");
    Assert.assertFalse(result);
  }
  
  @Test(expected = EncoderException.class)
  public void testEncodeInvalidObject() throws EncoderException {
    Caverphone caverphone = new Caverphone();
    
    caverphone.encode(1234);
  }
}