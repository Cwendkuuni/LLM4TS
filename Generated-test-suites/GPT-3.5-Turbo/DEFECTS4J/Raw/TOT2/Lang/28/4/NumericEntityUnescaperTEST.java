import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Typical use case
        assertEquals("Testing typical use case", 3, unescaper.translate("&#65;", 0, new StringWriter()));
        
        // Edge case: Unicode character outside of Basic Multilingual Plane (BMP)
        assertEquals("Testing edge case: Unicode character outside of BMP", 5, unescaper.translate("&#128514;", 0, new StringWriter()));
        
        // Error scenario: Invalid numeric entity
        assertEquals("Testing error scenario: Invalid numeric entity", 0, unescaper.translate("&#invalid;", 0, new StringWriter()));
        
        // Edge case: Hexadecimal numeric entity
        assertEquals("Testing edge case: Hexadecimal numeric entity", 5, unescaper.translate("&#x41;", 0, new StringWriter()));
        
    }
}