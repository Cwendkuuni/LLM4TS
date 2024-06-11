
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

        // Test input "&amp;"
        String input1 = "&amp;";
        Writer writer1 = new StringWriter();
        unescaper.translate(input1, 0, writer1);
        assertEquals("&", writer1.toString());

        // Test input "&#38;"
        String input2 = "&#38;";
        Writer writer2 = new StringWriter();
        unescaper.translate(input2, 0, writer2);
        assertEquals("&", writer2.toString());

        // Test input "&#x26;"
        String input3 = "&#x26;";
        Writer writer3 = new StringWriter();
        unescaper.translate(input3, 0, writer3);
        assertEquals("&", writer3.toString());

        // Test input "&#65;"
        String input4 = "&#65;";
        Writer writer4 = new StringWriter();
        unescaper.translate(input4, 0, writer4);
        assertEquals("A", writer4.toString());

        // Test input "&#x41;"
        String input5 = "&#x41;";
        Writer writer5 = new StringWriter();
        unescaper.translate(input5, 0, writer5);
        assertEquals("A", writer5.toString());

        // Test input "&#xffff;"
        String input6 = "&#xffff;";
        Writer writer6 = new StringWriter();
        unescaper.translate(input6, 0, writer6);
        assertEquals("￿", writer6.toString());

        // Test input "&#1000000;"
        String input7 = "&#1000000;";
        Writer writer7 = new StringWriter();
        unescaper.translate(input7, 0, writer7);
        assertEquals("𐀀", writer7.toString());
    }
}
