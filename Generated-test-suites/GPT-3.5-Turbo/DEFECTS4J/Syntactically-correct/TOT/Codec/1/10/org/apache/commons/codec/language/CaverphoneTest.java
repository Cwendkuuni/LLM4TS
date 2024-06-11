import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

  @Test
  public void testCaverphone() {
    // Typical use case
    assertEquals("1111111111", new Caverphone().caverphone(null));
    assertEquals("1111111111", new Caverphone().caverphone(""));
    assertEquals("28226222", new Caverphone().caverphone("Apache Software Foundation"));
    assertEquals("22122", new Caverphone().caverphone("Caverphone"));
    
    // Edge cases
    assertEquals("A11", new Caverphone().caverphone("a"));
    assertEquals("B11", new Caverphone().caverphone("b"));
    assertEquals("311", new Caverphone().caverphone("c"));
    assertEquals("D11", new Caverphone().caverphone("d"));
    assertEquals("E11", new Caverphone().caverphone("e"));
    assertEquals("F11", new Caverphone().caverphone("f"));
    assertEquals("G11", new Caverphone().caverphone("g"));
    assertEquals("H11", new Caverphone().caverphone("h"));
    assertEquals("311", new Caverphone().caverphone("i"));
    assertEquals("J11", new Caverphone().caverphone("j"));
    assertEquals("K11", new Caverphone().caverphone("k"));
    assertEquals("L11", new Caverphone().caverphone("l"));
    assertEquals("M11", new Caverphone().caverphone("m"));
    assertEquals("N11", new Caverphone().caverphone("n"));
    assertEquals("O11", new Caverphone().caverphone("o"));
    assertEquals("P11", new Caverphone().caverphone("p"));
    assertEquals("311", new Caverphone().caverphone("q"));
    assertEquals("R11", new Caverphone().caverphone("r"));
    assertEquals("111", new Caverphone().caverphone("s"));
    assertEquals("T11", new Caverphone().caverphone("t"));
    assertEquals("311", new Caverphone().caverphone("u"));
    assertEquals("V11", new Caverphone().caverphone("v"));
    assertEquals("W11", new Caverphone().caverphone("w"));
    assertEquals("311", new Caverphone().caverphone("x"));
    assertEquals("311", new Caverphone().caverphone("y"));
    assertEquals("311", new Caverphone().caverphone("z"));
    
    // Error scenarios
    // Test for EncoderException when object is not of type String
    try {
      new Caverphone().encode(1234);
      fail("Should throw EncoderException");
    } catch (EncoderException e) {
      // Expected
    }
  }

  @Test
  public void testEncode() {
    // Typical use case
    assertEquals("1111111111", new Caverphone().encode(null));
    assertEquals("1111111111", new Caverphone().encode(""));
    assertEquals("28226222", new Caverphone().encode("Apache Software Foundation"));
    assertEquals("22122", new Caverphone().encode("Caverphone"));
    
    // Edge cases
    assertEquals("A11", new Caverphone().encode("a"));
    assertEquals("B11", new Caverphone().encode("b"));
    assertEquals("311", new Caverphone().encode("c"));
    assertEquals("D11", new Caverphone().encode("d"));
    assertEquals("E11", new Caverphone().encode("e"));
    assertEquals("F11", new Caverphone().encode("f"));
    assertEquals("G11", new Caverphone().encode("g"));
    assertEquals("H11", new Caverphone().encode("h"));
    assertEquals("311", new Caverphone().encode("i"));
    assertEquals("J11", new Caverphone().encode("j"));
    assertEquals("K11", new Caverphone().encode("k"));
    assertEquals("L11", new Caverphone().encode("l"));
    assertEquals("M11", new Caverphone().encode("m"));
    assertEquals("N11", new Caverphone().encode("n"));
    assertEquals("O11", new Caverphone().encode("o"));
    assertEquals("P11", new Caverphone().encode("p"));
    assertEquals("311", new Caverphone().encode("q"));
    assertEquals("R11", new Caverphone().encode("r"));
    assertEquals("111", new Caverphone().encode("s"));
    assertEquals("T11", new Caverphone().encode("t"));
    assertEquals("311", new Caverphone().encode("u"));
    assertEquals("V11", new Caverphone().encode("v"));
    assertEquals("W11", new Caverphone().encode("w"));
    assertEquals("311", new Caverphone().encode("x"));
    assertEquals("311", new Caverphone().encode("y"));
    assertEquals("311", new Caverphone().encode("z"));
    
    // Error scenarios
    // Test for EncoderException when object is not of type String
    try {
      new Caverphone().encode(1234);
      fail("Should throw EncoderException");
    } catch (EncoderException e) {
      // Expected
    }
  }

  @Test
  public void testIsCaverphoneEqual() {
    // Typical use cases
    assertTrue(new Caverphone().isCaverphoneEqual(null, ""));
    assertFalse(new Caverphone().isCaverphoneEqual("Apache Software Foundation", "Caverphone"));
    assertTrue(new Caverphone().isCaverphoneEqual("caverphone", "caverphone"));
  }
}