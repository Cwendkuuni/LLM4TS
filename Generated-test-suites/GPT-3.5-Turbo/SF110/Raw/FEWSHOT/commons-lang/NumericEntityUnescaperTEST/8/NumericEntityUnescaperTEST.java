
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test valid numeric entity
        CharSequence input1 = "&#65;";
        StringWriter out1 = new StringWriter();
        assertEquals(2, unescaper.translate(input1, 0, out1));
        assertEquals("A", out1.toString());

        // Test valid hexadecimal numeric entity
        CharSequence input2 = "&#x1F602;";
        StringWriter out2 = new StringWriter();
        assertEquals(7, unescaper.translate(input2, 0, out2));
        assertEquals("\uD83D\uDE02", out2.toString());

        // Test invalid numeric entity without semicolon
        CharSequence input3 = "&#65";
        StringWriter out3 = new StringWriter();
        assertEquals(0, unescaper.translate(input3, 0, out3));
        assertTrue(out3.toString().isEmpty());

        // Test invalid hexadecimal numeric entity without semicolon
        CharSequence input4 = "&#x1F602";
        StringWriter out4 = new StringWriter();
        assertEquals(0, unescaper.translate(input4, 0, out4));
        assertTrue(out4.toString().isEmpty());

        // Test invalid numeric entity with invalid format
        CharSequence input5 = "&#123a;";
        StringWriter out5 = new StringWriter();
        assertEquals(0, unescaper.translate(input5, 0, out5));
        assertTrue(out5.toString().isEmpty());

        // Test invalid hexadecimal numeric entity with invalid format
        CharSequence input6 = "&#x1F6G2;";
        StringWriter out6 = new StringWriter();
        assertEquals(0, unescaper.translate(input6, 0, out6));
        assertTrue(out6.toString().isEmpty());
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_errorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        CharSequence input = "&#65";
        StringWriter out = new StringWriter();
        unescaper.translate(input, 0, out);
    }

}

