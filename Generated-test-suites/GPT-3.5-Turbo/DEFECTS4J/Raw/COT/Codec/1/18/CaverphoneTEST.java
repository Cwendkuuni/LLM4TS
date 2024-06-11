import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

  @Test
  public void testCaverphone() {
    Caverphone caverphone = new Caverphone();
    
    // Null value
    assertEquals("1111111111", caverphone.caverphone(null));
    
    // Empty string
    assertEquals("1111111111", caverphone.caverphone(""));
    
    // Basic functionality
    assertEquals("6262626262", caverphone.caverphone("apache"));
    assertEquals("6565656571", caverphone.caverphone("commons"));
    
    // Remove final e
    assertEquals("6565656571", caverphone.caverphone("commons"));
    assertEquals("6262626262", caverphone.caverphone("apachee"));
    
    // Handle various start options
    assertEquals("2n3p", caverphone.caverphone("gnp"));
    assertEquals("Aou2f", caverphone.caverphone("enough"));
    assertEquals("2nrou2fv", caverphone.caverphone("troughv"));
    
    // Handle replacements
    assertEquals("2qu", caverphone.caverphone("cq"));
    assertEquals("si", caverphone.caverphone("ci"));
    assertEquals("se", caverphone.caverphone("ce"));
    assertEquals("sy", caverphone.caverphone("cy"));
    assertEquals("2ch", caverphone.caverphone("tch"));
    assertEquals("k", caverphone.caverphone("c"));
    assertEquals("k", caverphone.caverphone("q"));
    assertEquals("k", caverphone.caverphone("x"));
    assertEquals("f", caverphone.caverphone("v"));
    assertEquals("2g", caverphone.caverphone("dg"));
    assertEquals("sio", caverphone.caverphone("tio"));
    assertEquals("sia", caverphone.caverphone("tia"));
    assertEquals("t", caverphone.caverphone("d"));
    assertEquals("fh", caverphone.caverphone("ph"));
    assertEquals("p", caverphone.caverphone("b"));
    assertEquals("s2", caverphone.caverphone("sh"));
    assertEquals("s", caverphone.caverphone("z"));
    assertEquals("A", caverphone.caverphone("a"));
    assertEquals("3", caverphone.caverphone("e"));
    assertEquals("y", caverphone.caverphone("j"));
    assertEquals("Y3", caverphone.caverphone("y3"));
    assertEquals("A", caverphone.caverphone("y"));
    assertEquals("3", caverphone.caverphone("y"));
    assertEquals("3kh3", caverphone.caverphone("3gh3"));
    assertEquals("22", caverphone.caverphone("gh"));
    assertEquals("k", caverphone.caverphone("g"));
    assertEquals("S", caverphone.caverphone("s"));
    assertEquals("T", caverphone.caverphone("t"));
    assertEquals("P", caverphone.caverphone("p"));
    assertEquals("K", caverphone.caverphone("k"));
    assertEquals("F", caverphone.caverphone("f"));
    assertEquals("M", caverphone.caverphone("m"));
    assertEquals("N", caverphone.caverphone("n"));
    assertEquals("W3", caverphone.caverphone("w3"));
    assertEquals("Wh3", caverphone.caverphone("wh3"));
    assertEquals("3", caverphone.caverphone("w"));
    assertEquals("A", caverphone.caverphone("h"));
    assertEquals("2", caverphone.caverphone("r"));
    assertEquals("3", caverphone.caverphone("r3"));
    assertEquals("2", caverphone.caverphone("l"));
    assertEquals("2", caverphone.caverphone("j"));
    assertEquals("3", caverphone.caverphone("y3"));
    assertEquals("2", caverphone.caverphone("y"));
    
    // Handle removals
    assertEquals("", caverphone.caverphone("2"));
    assertEquals("A", caverphone.caverphone("3"));
    assertEquals("", caverphone.caverphone("3"));
    
    // Full functionality
    assertEquals("A", caverphone.caverphone("apache commons codec"));
    assertEquals("3", caverphone.caverphone("Apache Commons Codec"));
  }
  
  @Test
  public void testEncode() {
    Caverphone caverphone = new Caverphone();
    
    // Null value
    try {
      caverphone.encode(null);
      fail("Exception not thrown");
    } catch (EncoderException e) {
      assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
    }
    
    // Basic functionality
    assertEquals("6262626262", caverphone.encode("apache"));
    assertEquals("6565656571", caverphone.encode("commons"));
  }
  
  @Test
  public void testIsCaverphoneEqual() {
    Caverphone caverphone = new Caverphone();
    
    // Basic functionality
    assertTrue(caverphone.isCaverphoneEqual("apache", "apache"));
    assertFalse(caverphone.isCaverphoneEqual("apache", "commons"));
  }


}