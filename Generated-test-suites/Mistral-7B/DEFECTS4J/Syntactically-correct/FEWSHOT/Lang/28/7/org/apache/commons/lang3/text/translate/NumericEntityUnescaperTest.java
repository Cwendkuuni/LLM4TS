package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        StringBuilder out = new StringBuilder();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test with valid entities
        String input = "&amp;&#x26;#032;&#xA0;&#x00A0;&#x1F000;&#x1F6FF;&#x20AC;";
        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, out, null);
            index += length;
        }
        assertEquals("&amp; &amp; &amp; &nbsp; &nbsp; ⊘ ♂", out.toString());

        // Test with invalid entities
        input = "&invalidEntity;";
        index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, out, null);
            index += length;
        }
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateHex() throws IOException {
        StringBuilder out = new StringBuilder();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#x26;#xFFFF;";
        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, out, null);
            index += length;
        }
        assertEquals("&amp; &fffe;", out.toString());
    }

    @Test
    public void testTranslateDec() throws IOException {
        StringBuilder out = new StringBuilder();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#1025;";
        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, out, null);
            index += length;
        }
        assertEquals("&amp; ԭ;", out.toString());
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidDecimal() throws IOException {
        StringBuilder out = new StringBuilder();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#a;";
        int index = 0;
        while (index < input.length()) {
            unescaper.translate(new String(input), index, out, null);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidHex() throws IOException {
        StringBuilder out = new StringBuilder();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#xz;";
        int index = 0;
        while (index < input.length()) {
            unescaper.translate(new String(input), index, out, null);
        }
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testArrayIndexOutOfBounds() throws IOException {
        StringBuilder out = new StringBuilder();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#";
        int index = 0;
        while (index < input.length()) {
            unescaper.translate(new String(input), index, out, null);
            index++;
        }
    }
}