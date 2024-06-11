import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    // Typical use case: translating a numeric entity with a decimal value
    assertEquals("A", unescaper.translate("&#65;A".toCharArray(), 0, null));
    
    // Typical use case: translating a numeric entity with a hex value
    assertEquals("A", unescaper.translate("&#x41;A".toCharArray(), 0, null));
    
    // Edge case: translating a numeric entity with a decimal value higher than 0xFFFF
    assertEquals("𐀀", unescaper.translate("&#65536;A".toCharArray(), 0, null));
    
    // Edge case: translating a numeric entity with a hex value higher than 0xFFFF
    assertEquals("𐀀", unescaper.translate("&#x10000;A".toCharArray(), 0, null));
    
    // Error scenario: non-numeric entities should not be translated
    assertEquals(0, unescaper.translate("&amp;A".toCharArray(), 0, null));
    
    // Error scenario: incomplete numeric entity without a semi-colon
    assertEquals(0, unescaper.translate("&#65A".toCharArray(), 0, null));
    
    // Error scenario: invalid decimal value
    assertEquals(0, unescaper.translate("&#A;".toCharArray(), 0, null));
    
    // Error scenario: invalid hex value
    assertEquals(0, unescaper.translate("&#xG;".toCharArray(), 0, null));
    
    // Error scenario: invalid hex value without a semi-colon
    assertEquals(0, unescaper.translate("&#x41A".toCharArray(), 0, null));
  }
}