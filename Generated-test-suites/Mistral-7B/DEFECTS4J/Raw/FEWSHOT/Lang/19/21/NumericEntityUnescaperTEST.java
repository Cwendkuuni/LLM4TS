package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test simple entities
        unescaper.translate(new StringSource("&amp;"), 0, writer);
        assertEquals("&", writer.toString());

        unescaper.translate(new StringSource("&lt;"), 0, writer);
        assertEquals("<", writer.toString());

        unescaper.translate(new StringSource("&gt;"), 0, writer);
        assertEquals(">", writer.toString());

        // Test entities with semi-colon
        unescaper.translate(new StringSource("&apos;"), 0, writer);
        assertEquals("'", writer.toString());

        unescaper.translate(new StringSource("&quot;"), 0, writer);
        assertEquals("\"", writer.toString());

        // Test entities without semi-colon
        unescaper.translate(new StringSource("&eacute;"), 0, writer);
        assertEquals('\u00E9', writer.toString()[0]);

        unescaper.translate(new StringSource("&AUMl;"), 0, writer);
        assertEquals('\u00C4', writer.toString()[0]);

        // Test entities with leading whitespace
        unescaper.translate(new StringSource(" &nbsp;"), 0, writer);
        assertEquals(" ", writer.toString());

        unescaper.translate(new StringSource(" &nbsp; &eacute;"), 0, writer);
        assertEquals('\u00E9', writer.toString()[1]);

        // Test entities with multiple entities
        unescaper.translate(new StringSource("&lt;p&gt;Hello &amp; &nbsp; world!&lt;/p&gt;"), 0, writer);
        assertEquals("<p>Hello & &nbsp; world!</p>", writer.toString());

        // Test entities with invalid format
        unescaper.translate(new StringSource("&foo;"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#x;"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#xABCDEF;"), 0, writer);
        assertEquals("\uABCDEF", writer.toString());

        unescaper.translate(new StringSource("&#x0000;"), 0, writer);
        assertEquals("\u0000", writer.toString());

        unescaper.translate(new StringSource("&#x10FFFF;"), 0, writer);
        assertEquals("\u10FFFF", writer.toString());

        // Test entities with invalid characters
        unescaper.translate(new StringSource("&%&#;"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#%;"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#[^\\x];"), 0, writer);
        assertEquals("", writer.toString());

        // Test entities with invalid length
        unescaper.translate(new StringSource("&#[1234];"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#[12345];"), 0, writer);
        assertEquals("", writer.toString());

        // Test entities with invalid hex digits
        unescaper.translate(new StringSource("&#[xABC];"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#[xABCD];"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#[xABCDEF];"), 0, writer);
        assertEquals("\uABCDEF", writer.toString());

        unescaper.translate(new StringSource("&#[xABCDEFG];"), 0, writer);
        assertEquals("", writer.toString());

        // Test entities with invalid octal digits
        unescaper.translate(new StringSource("&#[0123];"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#[01234];"), 0, writer);
        assertEquals("\u0040", writer.toString()[0]);

        unescaper.translate(new StringSource("&#[012345];"), 0, writer);
        assertEquals("\u0041", writer.toString()[0]);

        unescaper.translate(new StringSource("&#[0123456];"), 0, writer);
        assertEquals("\u0042", writer.toString()[0]);

        unescaper.translate(new StringSource("&#[01234567];"), 0, writer);
        assertEquals("", writer.toString());

        // Test entities with invalid base
        unescaper.translate(new StringSource("&#[xABCDEFGHIJ];"), 0, writer);
        assertEquals("", writer.toString());

        unescaper.translate(new StringSource("&#[x123456789ABCDEFABCDEF];"), 0, writer);
        assertEquals("\u12345678\u9ABCDEFABCDEF", writer.toString());

        // Test entities with invalid characters after semi-colon
        unescaper.translate(new StringSource("&#[x1234];foo"), 0, writer);
        assertEquals("\u1234", writer.toString());

        unescaper.translate(new StringSource("&#[x1234];bar"), 0, writer);
        assertEquals("\u1234", writer.toString());

        // Test entities with multi-byte characters
        unescaper.translate(new StringSource("&#[x9FAC];"), 0, writer);
        assertTrue(writer.toString().matches("[\u9FAC]+"));

        unescaper.translate(new StringSource("&#[x10400];"), 0, writer);
        assertTrue(writer.toString().matches("[\u10400-\u104FF]+"));

        // Test entities with Unicode surrogate pairs
        unescaper.translate(new StringSource("&#[x1D301];"), 0, writer);
        assertTrue(writer.toString().matches("[\uD835\uDD31]+"));

        unescaper.translate(new StringSource("&#[x1D302];"), 0, writer);
        assertTrue(writer.toString().matches("[\uD835\uDD32]+"));

        unescaper.translate(new StringSource("&#[x1D303];"), 0, writer);
        assertTrue(writer.toString().matches("[\uD835\uDD33]+"));

        unescaper.translate(new StringSource("&#[x1D304];"), 0, writer);
        assertTrue(writer.toString().matches("[\uD835\uDD34]+"));

        // Test entities with characters outside BMP
        unescaper.translate(new StringSource("&#[x10FFFF];"), 0, writer);
        assertTrue(writer.toString().matches("[\u10FFFF]+"));

        unescaper.translate(new StringSource("&#[x110000];"), 0, writer);
        assertTrue(writer.toString().matches("[\u110000]+"));
    }
}