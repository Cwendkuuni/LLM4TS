package org.apache.commons.lang3.text.translate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test with decimal numeric entity
        String input1 = "&#65;";
        Writer out1 = new StringWriter();
        unescaper.translate(input1, 0, out1);
        assertEquals("A", out1.toString());

        // Test with hexadecimal numeric entity
        String input2 = "&#x61;";
        Writer out2 = new StringWriter();
        unescaper.translate(input2, 0, out2);
        assertEquals("a", out2.toString());

        // Test with invalid numeric entity
        String input3 = "&#1234;";
        Writer out3 = new StringWriter();
        unescaper.translate(input3, 0, out3);
        assertEquals(0, out3.toString().length());
    }

}