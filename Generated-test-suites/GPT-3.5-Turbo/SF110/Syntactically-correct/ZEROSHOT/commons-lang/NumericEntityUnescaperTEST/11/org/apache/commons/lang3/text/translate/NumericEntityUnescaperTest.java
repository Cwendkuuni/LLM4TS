package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import org.junit.Assert.*;
import java.io.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test input with valid numeric entity
        CharSequence input1 = "&#65;";
        StringWriter out1 = new StringWriter();
        int result1 = unescaper.translate(input1, 0, out1);
        assertEquals(4, result1);
        assertEquals("A", out1.toString());

        // Test input with valid hexadecimal numeric entity
        CharSequence input2 = "&#x41;";
        StringWriter out2 = new StringWriter();
        int result2 = unescaper.translate(input2, 0, out2);
        assertEquals(5, result2);
        assertEquals("A", out2.toString());

        // Test input with invalid numeric entity
        CharSequence input3 = "&#XYZ;";
        StringWriter out3 = new StringWriter();
        int result3 = unescaper.translate(input3, 0, out3);
        assertEquals(0, result3);
        assertEquals("", out3.toString());

        // Test input with numeric entity exceeding 65535
        CharSequence input4 = "&#65536;";
        StringWriter out4 = new StringWriter();
        int result4 = unescaper.translate(input4, 0, out4);
        assertEquals(6, result4);
        assertEquals("", out4.toString());

        // Test input with missing semi-colon and semiColonRequired option set
        CharSequence input5 = "&#65";
        StringWriter out5 = new StringWriter();
        int result5 = unescaper.translate(input5, 0, out5);
        assertEquals(0, result5);
        assertEquals("", out5.toString());

        // Test input with missing semi-colon and semiColonOptional option set
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        CharSequence input6 = "&#65";
        StringWriter out6 = new StringWriter();
        int result6 = unescaper2.translate(input6, 0, out6);
        assertEquals(0, result6);
        assertEquals("", out6.toString());

        // Test input with missing semi-colon and errorIfNoSemiColon option set
        NumericEntityUnescaper unescaper3 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input7 = "&#65";
        StringWriter out7 = new StringWriter();
        try {
            int result7 = unescaper3.translate(input7, 0, out7);
            assertEquals(0, result7);
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        assertEquals("", out7.toString());
    }
}