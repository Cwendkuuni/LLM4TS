package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        
        // Test case 1
        String input1 = "&amp;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, out);
        assertEquals(5, result1);
        assertEquals("&", out.toString());

        // Test case 2
        String input2 = "&#38;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, out);
        assertEquals(5, result2);
        assertEquals("&", out.toString());

        // Test case 3
        String input3 = "&#x26;";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, out);
        assertEquals(6, result3);
        assertEquals("&", out.toString());

        // Test case 4
        String input4 = "&#38";
        int index4 = 0;
        int result4 = unescaper.translate(input4, index4, out);
        assertEquals(4, result4);
        assertEquals("", out.toString());
    }
}