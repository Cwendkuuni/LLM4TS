package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test valid numeric entities
        assertEquals('\u00A9', unescaper.translate(new String("&copy;"), 0, new StringBuilder()).toString().charAt(0));
        assertEquals('\u00A0', unescaper.translate(new String("&nbsp;"), 0, new StringBuilder()).toString().charAt(0));
        assertEquals('\u0026', unescaper.translate(new String("&amp;"), 0, new StringBuilder()).toString().charAt(0));
        assertEquals('\u0022', unescaper.translate(new String("&quot;"), 0, new StringBuilder()).toString().charAt(0));
        assertEquals('\u003C', unescaper.translate(new String("&lt;"), 0, new StringBuilder()).toString().charAt(0));
        assertEquals('\u003E', unescaper.translate(new String("&gt;"), 0, new StringBuilder()).toString().charAt(0));

        // Test valid hexadecimal entities
        assertEquals('\u00EF', unescaper.translate(new String("&Auml;"), 0, new StringBuilder()).toString().charAt(0));
        assertEquals('\u00F1', unescaper.translate(new String("&agrave;"), 0, new StringBuilder()).toString().charAt(0));
        assertEquals('\u00F8', unescaper.translate(new String("&ordm;"), 0, new StringBuilder()).toString().charAt(0));

        // Test entities without semi-colon
        assertEquals('\u00A3', unescaper.translate(new String("&pound;"), 0, new StringBuilder()).toString().charAt(0));

        // Test entities with uppercase characters
        assertEquals('\u0041', unescaper.translate(new String("&A;"), 0, new StringBuilder()).toString().charAt(0));

        // Testentities with digits only
        assertEquals('\u0061', unescaper.translate(new String("&a;"), 0, new StringBuilder()).toString().charAt(0));

        // Test entities with whitespaces
        assertEquals('\u0061', unescaper.translate(new String("&a;&#x61;"), 0, new StringBuilder()).toString().charAt(0));

        // Test entities with multiple entities
        assertEquals("abcï»¿def\u0304gh\u2212ijk", unescaper.translate(new String("abc&iuml;ï»¿def&micro;gh&ndash;ijk"), 0, new StringBuilder()).toString());

        // Test entities with invalid characters
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new String("&notanentity;"), 0, new StringBuilder()));

        // Test entities with invalid format
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new String("&&#;"), 0, new StringBuilder()));
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new String("&#x;"), 0, new StringBuilder()));
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new String("&#xabc;"), 0, new StringBuilder()));
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new String("&#xab;c;"), 0, new StringBuilder()));

        // Test entities outside the Unicode range
        assertThrows(IOException.class, () -> unescaper.translate(new String("&#x110000;"), 0, new StringBuilder()));
    }

    @Test
    public void testTranslateWithEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        assertEquals("", unescaper.translate(new String(""), 0, output).toString());
    }

    @Test
    public void testTranslateWithNegativeIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(new String("abc"), -1, output));
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(new String("abc"), -2, output));
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(new String("abc"), -3, output));
    }

    @Test
    public void testTranslateWithNullWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertThrows(NullPointerException.class, () -> unescaper.translate(new String("abc"), 0, null));
    }
}