import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Typical use case
        assertEquals(7, unescaper.translate("&#65;", 0, new StringWriter()));
        
        // Edge cases
        // Test when input is empty
        assertEquals(0, unescaper.translate("", 0, new StringWriter()));
        
        // Test when input does not start with '&#'
        assertEquals(0, unescaper.translate("A", 0, new StringWriter()));
        
        // Test when input starts with '&#' but without following digit
        assertEquals(0, unescaper.translate("&#", 0, new StringWriter()));
        
        // Test when input starts with '&#x' but without following hex digit
        assertEquals(0, unescaper.translate("&#x", 0, new StringWriter()));
        
        // Test when input contains non-numeric value in-between '&#' and ';'
        assertEquals(0, unescaper.translate("&#A;", 0, new StringWriter()));
        
        // Error scenarios
        // Test when input contains invalid hex value after '&#x'
        assertEquals(0, unescaper.translate("&#xG;", 0, new StringWriter()));
        
        // Test when input contains invalid decimal value after '&#'
        assertEquals(0, unescaper.translate("&#ABC;", 0, new StringWriter()));
        
        // Test when input contains invalid numeric entity value
        assertEquals(0, unescaper.translate("&#1000000;", 0, new StringWriter()));
    }
}