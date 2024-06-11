package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();

        // Test with no entities
        String input = "This is a test";
        int length = unescaper.translate(input, 0, out);
        assertEquals(input.length(), length);
        assertEquals(input, out.toString());

        // Test with simple entities
        input = "This is &amp; an &apos; test &quo; of &entit; NumericEntityUnescaper &nbsp; with &iexcl; exclamation, &cent; cent, &pound; pound, &yen; yen, &euro; euro, &copy; copy, &ordf; ordinatum, &laquo; laquo, &not; not, &reg; register, &trade; trade, &plusmn; plus-minus, &sup2; superscript 2, &sup3; superscript 3, &times; times, &divide; divide, &omg; omg, &frac12; fraction 1/2";
        length = unescaper.translate(input, 0, out);
        assertEquals(input.length(), length);
        assertEquals(input, out.toString());

        // Test with entities that include hex codes
        input = "This is &amp; an &apos; test &quo; of &#x414; &#x03B1; NumericEntityUnescaper &nbsp; with &#x00A1; exclamation, &#x00A2; cent, &#x00A3; pound, &#x00A5; yen, &#x20AC; euro, &#x00A9; copy, &#x2019; ordinatum, &#x201C; laquo, &#x2245; not, &#x00AE; register, &#x2122; trade, &#x00B1; plus-minus, &#x2070; superscript 2, &#x2071; superscript 3, &#x03C0; times, &#x03C2; divide, &#x26A9; omg, &#x2161; fraction 1/2";
        length = unescaper.translate(input, 0, out);
        assertEquals(input.length(), length);
        assertEquals(input, out.toString());

        // Test with entities that include semicolon
        input = "This is &amp; an &apos; test &quo; of &#x414; &#x03B1; NumericEntityUnescaper &nbsp; with &#x00A1; exclamation, &#x00A2; cent;, &#x00A3; pound, &#x00A5; yen;, &#x20AC; euro, &#x00A9; copy, &#x2019; ordinatum, &#x201C; laquo, &#x2245; not;, &#x00AE; register;, &#x2122; trade, &#x00B1; plus-minus, &#x2070; superscript 2, &#x2071; superscript 3, &#x03C0; times, &#x03C2; divide, &#x26A9; omg, &#x2161; fraction 1/2";
        length = unescaper.translate(input, 0, out);
        assertEquals(input.length(), length);
        assertEquals(input, out.toString());

        // Test with entities that include whitespace
        input = "This is &amp; an &apos; test &quo; of &#x414; &#x03B1; NumericEntityUnescaper &nbsp; with &#x00A1; exclamation, &#x00A2; cent, &#xA0; pound, &#x00A5; yen, &#x20AC; euro, &#x00A9; copy, &#x2019; ordinatum, &#x201C; laquo, &#x2245; not, &#x00AE; register, &#x2122; trade, &#x00B1; plus-minus, &#x2070; superscript 2, &#x2071; superscript 3, &#x03C0; times, &#x03C2; divide, &#x26A9; omg, &#x2161; fraction 1/2";
        length = unescaper.translate(input, 0, out);
        assertEquals(input.length(), length);
        assertEquals(input, out.toString());

        // Test with invalid entities
        input = "This is &amp; an &apos; test &quo; of &#x41; &#x03B1; NumericEntityUnescaper &nbsp; with &#x00; exclamation, &#x00; cent, &#x00; pound, &#x00; yen, &#x20AC; euro, &#x00; copy, &#x2019; ordinatum, &#x201C; laquo, &#x2245; not, &#x00; register, &#x2122; trade, &#x00; plus-minus, &#x2070; superscript 2, &#x2071; superscript 3, &#x03C0; times, &#x03C2; divide, &#x26A9; omg, &#x2161; fraction 1/2";
        length = unescaper.translate(input, 0, out);
        assertEquals(input.length(), length);
        assertEquals(input, out.toString());
    }

    @Test
    public void testTranslateNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();
        assertNull(unescaper.translate(null, 0, out));
    }

    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringBuilder();
        assertEquals(0, unescaper.translate("", 0, out));
        assertEquals("", out.toString());
    }
}