package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateValidNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        String input = "This text contains &amp; some &#10; numeric &#160; &#x20AC; &#xFF00; &#xFFFF &#123456; entities &#x1F600;&#x1F641;";

        for (int i = 0; i < input.length(); i++) {
            int len = unescaper.translate(new String(input, i, 1), i, writer);
            if (len > 0) {
                i += len;
            }
        }

        String output = writer.toString();
        assertEquals("This text contains &amp; some \u000B numeric \u00A0 \u00AC \u00FF \u00FF \u123456 entities \uD835\uDD14\uD835\uDDE1", output);
    }

    @Test
    public void testTranslateValidHexNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        String input = "This text contains &amp; some &#x41; &#x1F345; entities";

        for (int i = 0; i < input.length(); i++) {
            int len = unescaper.translate(new String(input, i, 1), i, writer);
            if (len > 0) {
                i += len;
            }
        }

        String output = writer.toString();
        assertEquals("This text contains &amp; some A \uD835\uDCCD entities", output);
    }

    @Test
    public void testTranslateInvalidNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        String input = "This text contains &amp; some &#xyz; entities";

        assertThrows(NumberFormatException.class, () -> {
            for (int i = 0; i < input.length(); i++) {
                int len = unescaper.translate(new String(input, i, 1), i, writer);
                if (len > 0) {
                    i += len;
                }
            }
        });

        assertNotNull(writer.toString());
    }

    @Test
    public void testTranslateInvalidHexNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        String input = "This text contains &amp; some &#xzx; entities";

        assertThrows(NumberFormatException.class, () -> {
            for (int i = 0; i < input.length(); i++) {
                int len = unescaper.translate(new String(input, i, 1), i, writer);
                if (len > 0) {
                    i += len;
                }
            }
        });

        assertNotNull(writer.toString());
    }

    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();
        String input = "";

        for (int i = 0; i < input.length(); i++) {
            int len = unescaper.translate(new String(input, i, 1), i, writer);
            if (len > 0) {
                i += len;
            }
        }

        String output = writer.toString();
        assertEquals("", output);
    }
}