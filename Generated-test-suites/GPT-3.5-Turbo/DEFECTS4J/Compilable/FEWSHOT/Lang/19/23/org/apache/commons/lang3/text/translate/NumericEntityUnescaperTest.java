
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        StringBuilder input = new StringBuilder();
        
        // Test case: input does not contain any numeric entities
        input.append("This is a test string");
        unescaper.translate(input, 0, writer);
        assertEquals("This is a test string", writer.toString());
        
        // Test case: input contains a hex numeric entity with semicolon
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("&#x41;");
        unescaper.translate(input, 0, writer);
        assertEquals("A", writer.toString());
        
        // Test case: input contains a hex numeric entity without semicolon
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("&#x42");
        unescaper.translate(input, 0, writer);
        assertEquals("B", writer.toString());
        
        // Test case: input contains a decimal numeric entity with semicolon
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("&#65;");
        unescaper.translate(input, 0, writer);
        assertEquals("A", writer.toString());
        
        // Test case: input contains a decimal numeric entity without semicolon
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("&#66");
        unescaper.translate(input, 0, writer);
        assertEquals("B", writer.toString());
        
        // Test case: input contains multiple numeric entities
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("&#x41;&#x42;");
        unescaper.translate(input, 0, writer);
        assertEquals("AB", writer.toString());
        
        // Test case: input contains a numeric entity outside of the translation range
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("&#123456;");
        unescaper.translate(input, 0, writer);
        assertEquals("", writer.toString());
        
        // Test case: input is empty
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("");
        unescaper.translate(input, 0, writer);
        assertEquals("", writer.toString());
        
        // Test case: index is at the end of the input
        writer.getBuffer().setLength(0);
        input.setLength(0);
        input.append("&#x41;");
        unescaper.translate(input, input.length(), writer);
        assertEquals("", writer.toString());
    }
    
}
