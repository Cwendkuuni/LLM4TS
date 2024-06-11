package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test for regular decimal entity
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
        assertEquals(4, result);
        
        // Test for regular hexadecimal entity
        input = new StringBuilder("&#x41;");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
        assertEquals(5, result);
        
        // Test for entity with optional semicolon
        input = new StringBuilder("&#65");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
        assertEquals(4, result);
        
        // Test for incorrect entity value
        input = new StringBuilder("&#abc;");
        output = new StringBuilder();
        result = unescaper.translate(input, 0, output);
        assertEquals(0, result);
    }
}