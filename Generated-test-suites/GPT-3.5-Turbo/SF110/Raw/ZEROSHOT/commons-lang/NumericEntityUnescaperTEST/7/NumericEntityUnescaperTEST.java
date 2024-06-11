package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test numeric entity with decimal value
        int result1 = unescaper.translate("&35;", 0, writer);
        assertEquals(4, result1);
        assertEquals('#', writer.toString().charAt(0));
        
        // Test numeric entity with hexadecimal value
        int result2 = unescaper.translate("&#x21;", 0, writer);
        assertEquals(6, result2);
        assertEquals('!', writer.toString().charAt(0));
        
        // Test numeric entity with hexadecimal value and missing semicolon
        int result3 = unescaper.translate("&#x21", 0, writer);
        assertEquals(0, result3);
        
        // Test numeric entity with non-numeric value
        int result4 = unescaper.translate("&#xG;", 0, writer);
        assertEquals(0, result4);
    }

}