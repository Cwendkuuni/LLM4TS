import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();

    // Test case 1
    assertEquals("1111111111", caverphone.caverphone(null));

    // Test case 2
    assertEquals("1111111111", caverphone.caverphone(""));

    // Test case 3
    assertEquals("r232k232", caverphone.caverphone("rocks"));

    // Test case 4
    assertEquals("prryP3jpad", caverphone.caverphone("PerryPod"));

    // Test case 5
    assertEquals("kastin", caverphone.caverphone("casting"));

    // Test case 6
    assertEquals("AksAto", caverphone.caverphone("accord"));

    // Test case 7
    assertEquals("T2ka", caverphone.caverphone("tokyo"));

    // Test case 8
    assertEquals("322", caverphone.caverphone("yeah"));

    // Test case 9
    assertEquals("Wh3rk", caverphone.caverphone("work"));

    // Test case 10
    assertEquals("2212213rk2lh2m3", caverphone.caverphone("automobile"));

    // Test case 11
    assertEquals("3323", caverphone.caverphone("tree"));

    // Test case 12
    assertEquals("3kor3ks3", caverphone.caverphone("gorgeous"));

    // Test case 13
    assertEquals("221221", caverphone.caverphone("love"));

    // Test case 14
    assertEquals("12lAn", caverphone.caverphone("lion"));

    // Test case 15
    assertEquals("AchA", caverphone.caverphone("uck"));

    // Test case 16
    assertEquals("1323rfln3r", caverphone.caverphone("unfortunate"));

    // Test case 17
    assertEquals("st2dy", caverphone.caverphone("study"));

  }

  @Test
  public void testEncode() {
    Caverphone caverphone = new Caverphone();

    // Test case 1
    assertEquals("r232k232", caverphone.encode("rocks"));

    // Test case 2
    assertEquals("prryP3jpad", caverphone.encode("PerryPod"));

    // Test case 3
    assertEquals("kastin", caverphone.encode("casting"));

    // Test case 4
    assertEquals("AksAto", caverphone.encode("accord"));

    // Test case 5
    assertEquals("T2ka", caverphone.encode("tokyo"));

    // Test case 6
    assertEquals("322", caverphone.encode("yeah"));

    // Test case 7
    assertEquals("Wh3rk", caverphone.encode("work"));

    // Test case 8
    assertEquals("2212213rk2lh2m3", caverphone.encode("automobile"));

    // Test case 9
    assertEquals("3323", caverphone.encode("tree"));

    // Test case 10
    assertEquals("3kor3ks3", caverphone.encode("gorgeous"));

    // Test case 11
    assertEquals("221221", caverphone.encode("love"));

    // Test case 12
    assertEquals("12lAn", caverphone.encode("lion"));

    // Test case 13
    assertEquals("AchA", caverphone.encode("uck"));

    // Test case 14
    assertEquals("1323rfln3r", caverphone.encode("unfortunate"));

    // Test case 15
    assertEquals("st2dy", caverphone.encode("study"));

  }

  @Test(expected = EncoderException.class)
  public void testEncodeWithInvalidObject() throws EncoderException {
    Caverphone caverphone = new Caverphone();
    caverphone.encode(12345);
  }

  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();

    // Test case 1
    assertTrue(caverphone.isCaverphoneEqual("rocks", "rocks"));

    // Test case 2
    assertTrue(caverphone.isCaverphoneEqual("PerryPod", "p3rryP2d"));

    // Test case 3
    assertFalse(caverphone.isCaverphoneEqual("casting", "cast"));

    // Test case 4
    assertFalse(caverphone.isCaverphoneEqual("work", "worth"));

    // Test case 5
    assertTrue(caverphone.isCaverphoneEqual("automobile", "automobile"));
  }
}