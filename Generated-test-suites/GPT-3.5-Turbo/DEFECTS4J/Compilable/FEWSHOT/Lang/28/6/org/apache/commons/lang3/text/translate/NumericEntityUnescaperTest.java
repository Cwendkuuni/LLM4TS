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

        // Test input with decimal numeric entities
        Writer out1 = new StringWriter();
        assertEquals(6, unescaper.translate("Test &#65; Test", 5, out1));
        assertEquals("Test A Test", out1.toString());

        Writer out2 = new StringWriter();
        assertEquals(6, unescaper.translate("Test &#x41; Test", 5, out2));
        assertEquals("Test A Test", out2.toString());

        // Test input with hex numeric entities
        Writer out3 = new StringWriter();
        assertEquals(7, unescaper.translate("Test &#104; Test", 5, out3));
        assertEquals("Test h Test", out3.toString());

        Writer out4 = new StringWriter();
        assertEquals(7, unescaper.translate("Test &#x68; Test", 5, out4));
        assertEquals("Test h Test", out4.toString());

        // Test input with incorrect numeric entities
        Writer out5 = new StringWriter();
        assertEquals(0, unescaper.translate("Test &#123xyz; Test", 5, out5));
        assertEquals("", out5.toString());

        // Test input without numeric entities
        Writer out6 = new StringWriter();
        assertEquals(0, unescaper.translate("Test Test", 5, out6));
        assertEquals("", out6.toString());
    }
}