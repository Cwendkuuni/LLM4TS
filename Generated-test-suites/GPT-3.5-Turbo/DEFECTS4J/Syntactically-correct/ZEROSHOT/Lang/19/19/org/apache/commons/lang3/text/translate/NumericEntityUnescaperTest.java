import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    // Test input with decimal entity
    assertEquals(5, unescaper.translate("&#12345;", 0, null));
    assertEquals(6, unescaper.translate("&#12345;", 0, null));
    
    // Test input with hexadecimal entity
    assertEquals(4, unescaper.translate("&#x1234;", 0, null));
    assertEquals(5, unescaper.translate("&#x1234;", 0, null));
    
    // Test input without entity
    assertEquals(0, unescaper.translate("abc", 0, null));
    assertEquals(0, unescaper.translate("123", 0, null));
    
    // Test input with invalid entity
    assertEquals(0, unescaper.translate("&#12x;", 0, null));
    assertEquals(0, unescaper.translate("&#abcd;", 0, null));
  }
  
}