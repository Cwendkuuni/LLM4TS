package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test case 1: numeric entity without hex
        CharSequence input1 = "&#65;";
        int index1 = 0;
        StringWriter out1 = new StringWriter();
        assertEquals(5, unescaper.translate(input1, index1, out1));
        assertEquals("A", out1.toString());

        // Test case 2: numeric entity with hex
        CharSequence input2 = "&#x41;";
        int index2 = 0;
        StringWriter out2 = new StringWriter();
        assertEquals(6, unescaper.translate(input2, index2, out2));
        assertEquals("A", out2.toString());

        // Test case 3: numeric entity with invalid hex
        CharSequence input3 = "&#xG;";
        int index3 = 0;
        StringWriter out3 = new StringWriter();
        assertEquals(0, unescaper.translate(input3, index3, out3));
        assertEquals("", out3.toString());
    }
}