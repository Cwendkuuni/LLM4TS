package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence input = "&amp;&#x26;&#xA0;&#x1F4;foo&nbsp;&#8250;&#8249;";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = 0;
        int index = 0;
        while (length < input.length()) {
            int result = unescaper.translate(input, index, out);
            index += result;
            length += result;
        }

        assertEquals("&amp;&amp; &b &A0 &p &nbsp; &d &d;foo&nbsp;&diams;&diams;", out.toString());
    }

    @Test
    public void testTranslate_InvalidEntity() throws IOException {
        CharSequence input = "&invalidEntity;";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int result = unescaper.translate(input, index, out);
            index += result;
        }

        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_EmptyString() throws IOException {
        CharSequence input = "";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int result = unescaper.translate(input, index, out);
            index += result;
            length += result;
        }

        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_SingleCharacter() throws IOException {
        CharSequence input = "&amp;";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int result = unescaper.translate(input, index, out);
            index += result;
            length += result;
        }

        assertEquals("&amp;", out.toString());
    }

    @Test
    public void testTranslate_DecimalEntity() throws IOException {
        CharSequence input = "&eacute;";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int result = unescaper.translate(input, index, out);
            index += result;
            length += result;
        }

        assertEquals("À", out.toString());
    }

    @Test
    public void testTranslate_HexEntity() throws IOException {
        CharSequence input = "&#x41;";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int result = unescaper.translate(input, index, out);
            index += result;
            length += result;
        }

        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslate_EntityWithTrailingSemicolon() throws IOException {
        CharSequence input = "&eacute;";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int result = unescaper.translate(input, index, out);
            index += result;
            length += result;
        }

        assertEquals("À;", out.toString());

        CharSequence inputWithTrailingSemicolon = "&eacute;;";
        Writer out2 = new StringBuilder();

        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper();
        int index2 = 0;
        int length2 = inputWithTrailingSemicolon.length();
        while (index2 < length2) {
            int result2 = unescaper2.translate(inputWithTrailingSemicolon, index2, out2);
            index2 += result2;
            length2 += result2;
        }

        assertEquals("À;", out2.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslate_ArrayIndexOutOfBounds() throws IOException {
        CharSequence input = "&amp;";
        Writer out = new StringBuilder();

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int index = 2;
        int length = input.length();
        while (index < length) {
            int result = unescaper.translate(input, index, out);
            index += result;
            length += result;
        }

        fail("Should have thrown IOException");
    }
}