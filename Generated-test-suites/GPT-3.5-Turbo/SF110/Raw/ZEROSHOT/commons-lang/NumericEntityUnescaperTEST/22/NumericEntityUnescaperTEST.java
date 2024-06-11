import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        // Test with valid input
        StringBuffer output = new StringBuffer();
        int result = unescaper.translate("&#65;", 0, output);
        assertEquals(6, result);
        assertEquals("A", output.toString());
        
        // Test with invalid input (invalid entity value)
        result = unescaper.translate("&#XYZ;", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
        
        // Test with invalid input (no entity value)
        result = unescaper.translate("&#;", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
        
        // Test with invalid input (no semi-colon)
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        try {
            result = unescaper.translate("&#65", 0, output);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }
}