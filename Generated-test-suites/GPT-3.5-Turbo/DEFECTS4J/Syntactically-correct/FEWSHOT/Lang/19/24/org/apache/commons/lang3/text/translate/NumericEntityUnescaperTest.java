
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test case 1: input sequence is "&#65"
        String input1 = "&#65";
        Writer out1 = new StringWriter();
        int result1 = unescaper.translate(input1, 0, out1);
        assertEquals(4, result1);
        assertEquals("A", out1.toString());

        // Test case 2: input sequence is "&#x41"
        String input2 = "&#x41";
        Writer out2 = new StringWriter();
        int result2 = unescaper.translate(input2, 0, out2);
        assertEquals(5, result2);
        assertEquals("A", out2.toString());

        // Test case 3: input sequence is "&#x80;"
        String input3 = "&#x80;";
        Writer out3 = new StringWriter();
        int result3 = unescaper.translate(input3, 0, out3);
        assertEquals(7, result3);
        assertEquals("€", out3.toString());

        // Test case 4: input sequence is "&#9731;"
        String input4 = "&#9731;";
        Writer out4 = new StringWriter();
        int result4 = unescaper.translate(input4, 0, out4);
        assertEquals(7, result4);
        assertEquals("☃", out4.toString());

        // Test case 5: input sequence does not start with "&#" -> should return 0
        String input5 = "test";
        Writer out5 = new StringWriter();
        int result5 = unescaper.translate(input5, 0, out5);
        assertEquals(0, result5);
        assertEquals("", out5.toString());
    }

}
