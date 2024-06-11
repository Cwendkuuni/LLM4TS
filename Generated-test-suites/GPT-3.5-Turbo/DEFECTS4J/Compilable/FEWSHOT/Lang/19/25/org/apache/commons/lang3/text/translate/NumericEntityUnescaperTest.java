
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test case with numeric entity without semicolon
        assertEquals(3, unescaper.translate("&#65", 0, new StringWriter()));
        
        // Test case with numeric entity with semicolon
        assertEquals(4, unescaper.translate("&#65;", 0, new StringWriter()));
        
        // Test case with hex numeric entity without semicolon
        assertEquals(4, unescaper.translate("&#x41", 0, new StringWriter()));
        
        // Test case with hex numeric entity with semicolon
        assertEquals(5, unescaper.translate("&#x41;", 0, new StringWriter()));
        
        // Test case with numeric entity followed by non-numeric character
        assertEquals(5, unescaper.translate("&#65ab", 0, new StringWriter()));
        
        // Test case with hex numeric entity followed by non-numeric character
        assertEquals(6, unescaper.translate("&#x41ab", 0, new StringWriter()));
        
        // Test case with invalid hex numeric entity
        assertEquals(0, unescaper.translate("&#xG1;", 0, new StringWriter()));
        
        // Test case with invalid decimal numeric entity
        assertEquals(0, unescaper.translate("&#A;", 0, new StringWriter()));
    }
}
