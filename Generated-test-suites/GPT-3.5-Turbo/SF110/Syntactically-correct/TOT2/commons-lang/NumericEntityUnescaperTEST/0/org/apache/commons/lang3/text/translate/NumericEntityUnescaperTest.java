package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
    }
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65;"); // Character 'A'
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new java.io.StringWriter());
        
        assertEquals(2, result);
        assertEquals('A', output.toString().charAt(0));
        
        // Test for edge cases
        input = new StringBuilder("&#x61;"); // Character 'a'
        output = new StringBuilder();
        result = unescaper.translate(input, 0, new java.io.StringWriter());
        
        assertEquals(3, result);
        assertEquals('a', output.toString().charAt(0));
        
        // Test for error scenario with missing semicolon
        input = new StringBuilder("&#65"); // Missing semicolon
        output = new StringBuilder();
        try {
            result = unescaper.translate(input, 0, new java.io.StringWriter());
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
        
    }

}