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

        // Test input = "&#65;"
        CharSequence input1 = "&#65;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer);
        assertEquals(5, result1);
        assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0); // Clear the writer

        // Test input = "&#x41;"
        CharSequence input2 = "&#x41;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals(6, result2);
        assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0); // Clear the writer

        // Test input = "&#256;"
        CharSequence input3 = "&#256;";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer);
        assertEquals(5, result3);
        assertEquals("Ā", writer.toString());

        writer.getBuffer().setLength(0); // Clear the writer

        // Test input = "&#x100;"
        CharSequence input4 = "&#x100;";
        int index4 = 0;
        int result4 = unescaper.translate(input4, index4, writer);
        assertEquals(6, result4);
        assertEquals("Ā", writer.toString());

        writer.getBuffer().setLength(0); // Clear the writer

        // Test input = "&#65"
        CharSequence input5 = "&#65";
        int index5 = 0;
        int result5 = unescaper.translate(input5, index5, writer);
        assertEquals(0, result5);
        assertEquals("", writer.toString());

    }
}