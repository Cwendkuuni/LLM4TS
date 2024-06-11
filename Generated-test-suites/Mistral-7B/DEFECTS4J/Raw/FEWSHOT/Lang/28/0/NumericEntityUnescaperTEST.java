package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test valid numeric entities
        CharSequence input = "&amp;&#x26; &#32;&#xA0;&#x2260;&#x2261;&#x00A9;&#x2122;";
        Writer out = new StringBuilder();
        int length = unescaper.translate(input, 0, out);
        assertEquals("& & © ≤≥ ", out.toString());
        assertEquals(12, length);

        // Test hexadecimal numeric entities
        input = "&hex;&#x1F4;";
        out = new StringBuilder();
        length = unescaper.translate(input, 0, out);
        assertEquals("\u00DF", out.toString());
        assertEquals(2, length);

        // Test decimal numeric entities
        input = "&dec;&#128;";
        out = new StringBuilder();
        length = unescaper.translate(input, 0, out);
        assertEquals("\u0080", out.toString());
        assertEquals(2, length);

        // Test entities with leading whitespace and trailing non-ASCII characters
        input = " &#x1F4; &#x20AC;";
        out = new StringBuilder();
        length = unescaper.translate(input, 0, out);
        assertEquals("\u00DF\u00A2", out.toString());
        assertEquals(4, length);

        // Test entities with multiple characters
        input = "&copy;&#xA9;&#x2117;&#x2122;";
        out = new StringBuilder();
        length = unescaper.translate(input, 0, out);
        assertEquals("©™®", out.toString());
        assertEquals(6, length);

        // Test entities with leading ampersand and trailing semicolon
        input = "&amp;&#32;&#xA0;&#x2260;&#x2261;&#x00A9;&#x2122;&#x26;";
        out = new StringBuilder();
        length = unescaper.translate(input, 0, out);
        assertEquals("& & © ≤≥ &", out.toString());
        assertEquals(14, length);

        // Test entities with leading whitespace and multiple characters
        input = " &#x2260;&#x2261;&#x00A9;";
        out = new StringBuilder();
        length = unescaper.translate(input, 0, out);
        assertEquals(" ≤≥ ©", out.toString());
        assertEquals(4, length);

        // Test entities with invalid format
        input = "&invalid;";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(input, 0, new StringBuilder()));
    }

    @Test
    public void testTranslateWithNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();
        assertThrows(NullPointerException.class, () -> unescaper.translate(null, 0, out));
    }

    @Test
    public void testTranslateWithNullOutput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;&#x26; &#32;&#xA0;&#x2260;&#x2261;&#x00A9;&#x2122;";
        assertThrows(NullPointerException.class, () -> unescaper.translate(input, 0, null));
    }
}