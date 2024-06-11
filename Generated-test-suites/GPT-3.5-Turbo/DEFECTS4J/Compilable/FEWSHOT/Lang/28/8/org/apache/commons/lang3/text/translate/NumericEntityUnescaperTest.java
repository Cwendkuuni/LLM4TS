
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

        // Test numeric entity with decimal value
        String input1 = "&#65;";
        int result1 = unescaper.translate(input1, 0, writer);
        assertEquals(6, result1);
        assertEquals("A", writer.toString());

        // Test numeric entity with hexadecimal value
        String input2 = "&#x41;";
        int result2 = unescaper.translate(input2, 0, writer);
        assertEquals(6, result2);
        assertEquals("A", writer.toString());

        // Test numeric entity with invalid value
        String input3 = "&#INVALID;";
        int result3 = unescaper.translate(input3, 0, writer);
        assertEquals(0, result3);
        assertEquals("", writer.toString());

        // Test numeric entity without semicolon
        String input4 = "&#65";
        int result4 = unescaper.translate(input4, 0, writer);
        assertEquals(0, result4);
        assertEquals("", writer.toString());

        // Test input without numeric entity
        String input5 = "ABC";
        int result5 = unescaper.translate(input5, 0, writer);
        assertEquals(0, result5);
        assertEquals("", writer.toString());
    }
}
