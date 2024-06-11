import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_normalDecimalValue() {
        // Create instance of NumericEntityUnescaper
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test translating numeric entity with normal decimal value
        StringBuilder output = new StringBuilder();
        assertEquals(8, unescaper.translate("&#65;", 0, output));
        assertEquals('A', output.charAt(0));
    }
    
    @Test
    public void testTranslate_hexadecimalValue() {
        // Create instance of NumericEntityUnescaper
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test translating numeric entity with hexadecimal value
        StringBuilder output = new StringBuilder();
        assertEquals(6, unescaper.translate("&#x21;", 0, output));
        assertEquals('!', output.charAt(0));
    }
    
    @Test
    public void testTranslate_invalidEntityFormat() {
        // Create instance of NumericEntityUnescaper
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test translating numeric entity with invalid format
        StringBuilder output = new StringBuilder();
        assertEquals(0, unescaper.translate("&invalid;", 0, output));
    }
    
    @Test
    public void testTranslate_invalidNumberFormatException() {
        // Create instance of NumericEntityUnescaper
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test translating numeric entity with invalid number format
        StringBuilder output = new StringBuilder();
        assertEquals(0, unescaper.translate("&#invalid;", 0, output));
    }
}