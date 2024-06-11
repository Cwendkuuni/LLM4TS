
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test numeric entity of the form &#d;
        int result1 = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result1);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0); // reset writer
        
        // Test numeric entity of the form &#xh;
        int result2 = unescaper.translate("&#x41;", 0, writer);
        assertEquals(8, result2);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0); // reset writer
        
        // Test input without numeric entity
        int result3 = unescaper.translate("Hello World", 0, writer);
        assertEquals(0, result3);
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0); // reset writer
        
        // Test input with multiple numeric entities
        int result4 = unescaper.translate("&#72;&#101;&#108;&#108;&#111;", 0, writer);
        assertEquals(16, result4);
        assertEquals("Hello", writer.toString());
        writer.getBuffer().setLength(0); // reset writer
    }
}
