package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65;", 0, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());
        
        out.getBuffer().setLength(0);
        result = unescaper.translate("&#x41;", 0, out);
        assertEquals(7, result);
        assertEquals("A", out.toString());
        
        out.getBuffer().setLength(0);
        result = unescaper.translate("&amp;", 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
        
        out.getBuffer().setLength(0);
        try {
            result = unescaper.translate("&#65", 0, out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
}