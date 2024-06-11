package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test valid numeric entities
        Writer writer = new StringBuilder();
        CharSequence input = "&amp;&#x00A9;&#x03C3;&#xA0;&#x03B1;&#x03C0;&#x03B4;&#x2018;&#x2019;&#x2212;&#x2248;&#x27E8;&#x27E9;&#xFFFD;&#x1F471;&#x1F474;&#x1F469;&#x1F466;&#x1F464;&#x1F465;&#xFFFE;";
        for (int i = 0; i < input.length(); i++) {
            int Len = unescaper.translate(input, i, writer);
            String expected = Character.isDefinedChar(Character.codePointAt(input, i)) ? Character.toString((char) Character.codePointAt(input, i)) : "\\u" + Integer.toHexString(Character.codePointAt(input, i)).toUpperCase();
            assertEquals(expected, writer.toString());
            writer.setLength(0);
            i += Len;
        }

        // Test invalid numeric entities
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringBuilder("&").append(';'), 0, writer));
        assertThrows(IOException.class, () -> unescaper.translate(new StringBuilder("&amp;"), 1, null));
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(new StringBuilder("&amp;"), input.length(), writer));
    }

    @Test
    public void testTranslateHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer writer = new StringBuilder();
        CharSequence input = "&amp;&#xka;&#xaa;";
        for (int i = 0; i < input.length(); i++) {
            int Len = unescaper.translate(input, i, writer);
            String expected = Character.isDefinedChar(Character.codePointAt(input, i)) ? Character.toString((char) Character.codePointAt(input, i)) : "\\u" + Integer.toHexString(Character.codePointAt(input, i)).toUpperCase();
            if (i < 2) {
                expected = "\\u" + String.format("%04x", 0x%x % Character.codePointAt(input, i));
            }
            assertEquals(expected, writer.toString());
            writer.setLength(0);
            i += Len;
        }
    }

    @Test
    public void testTranslateHexLarge() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer writer = new StringBuilder();
        CharSequence input = "&amp;&#x110000;";
        for (int i = 0; i < input.length(); i++) {
            int Len = unescaper.translate(input, i, writer);
            String expected = Character.isDefinedChar(Character.codePointAt(input, i)) ? Character.toString((char) Character.codePointAt(input, i)) : "\\u" + Integer.toHexString(Character.codePointAt(input, i)).toUpperCase();
            assertEquals(expected, writer.toString());
            writer.setLength(0);
            i += Len;
        }
    }

    @Test
    public void testTranslateInvalidHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer writer = new StringBuilder();
        CharSequence input = "&amp;&#xkk;";
        for (int i = 0; i < input.length(); i++) {
            int Len = unescaper.translate(input, i, writer);
            assertEquals("", writer.toString());
            writer.setLength(0);
            i += Len;
        }
    }

    @Test
    public void testTranslateInvalidChar() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer writer = new StringBuilder();
        CharSequence input = "&amp;&#x0;";
        for (int i = 0; i < input.length(); i++) {
            int Len = unescaper.translate(input, i, writer);
            assertEquals("", writer.toString());
            writer.setLength(0);
            i += Len;
        }
    }
}