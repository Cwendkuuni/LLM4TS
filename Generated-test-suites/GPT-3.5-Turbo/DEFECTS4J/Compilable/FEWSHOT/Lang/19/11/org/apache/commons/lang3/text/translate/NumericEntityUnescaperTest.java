
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test input with valid numeric entities
        assertEquals(4, unescaper.translate("&#65;", 0, new StringWriter()));
        assertEquals(4, unescaper.translate("&#97;", 0, new StringWriter()));
        assertEquals(5, unescaper.translate("&#x41;", 0, new StringWriter()));
        assertEquals(5, unescaper.translate("&#x61;", 0, new StringWriter()));
        assertEquals(4, unescaper.translate("&#8364;", 0, new StringWriter()));
        assertEquals(4, unescaper.translate("&#163;", 0, new StringWriter()));
        
        // Test input with invalid numeric entities
        StringWriter out = new StringWriter();
        try {
            unescaper.translate("&#xG1;", 0, out);
            fail("Exception not thrown for invalid numeric entity");
        } catch (NumberFormatException e) {
            // Expected exception
        }
        assertEquals(4, out.getBuffer().length());
        
        out = new StringWriter();
        try {
            unescaper.translate("&#x1G;", 0, out);
            fail("Exception not thrown for invalid numeric entity");
        } catch (NumberFormatException e) {
            // Expected exception
        }
        assertEquals(0, out.getBuffer().length());
        
        out = new StringWriter();
        try {
            unescaper.translate("&#abc;", 0, out);
            fail("Exception not thrown for invalid numeric entity");
        } catch (NumberFormatException e) {
            // Expected exception
        }
        assertEquals(0, out.getBuffer().length());
        
        out = new StringWriter();
        try {
            unescaper.translate("&#;", 0, out);
            fail("Exception not thrown for invalid numeric entity");
        } catch (NumberFormatException e) {
            // Expected exception
        }
        assertEquals(1, out.getBuffer().length());
        
        // Test input without numeric entity
        assertEquals(0, unescaper.translate("abc", 0, new StringWriter()));
    }
}
