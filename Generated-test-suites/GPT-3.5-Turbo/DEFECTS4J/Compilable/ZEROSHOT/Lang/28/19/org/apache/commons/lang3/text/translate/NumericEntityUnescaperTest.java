package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test with valid numeric entity
        CharSequence input = "&amp;#65;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(8, result);
        assertEquals("A", writer.toString());
        
        // Test with valid hex numeric entity
        input = "&amp;#x41;";
        result = unescaper.translate(input, index, writer);
        assertEquals(9, result);
        assertEquals("A", writer.toString());
        
        // Test with invalid numeric entity
        input = "&amp;#invalid;";
        result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        // Test with incomplete numeric entity
        input = "&amp;#";
        result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}