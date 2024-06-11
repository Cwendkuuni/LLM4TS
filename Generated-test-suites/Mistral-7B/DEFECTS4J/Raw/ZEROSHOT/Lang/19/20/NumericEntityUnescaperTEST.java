package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Random;

import org.junit.Test;

/**
 * Comprehensive JUnit 4 test file for NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

    private Random random = new Random();

    @Test
    public void testTranslateNumbersWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        for (int i = 0x21; i <= 0x7E; i++) {
            String entity = "&" + Integer.toHexString(i & 0xFFFF) + ";";
            String input = "Test input: " + entity;
            int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
            assertEquals("Unexpected length", 1 + 1 + 2, length);
            assertEquals("Unexpected output: " + out.toString(), Character.toString((char) i), out.toString());
            out.getBuffer().setLength(0);
        }
        for (int i = 0x80; i <= 0xFFFF; i++) {
            String entity = "&" + Integer.toHexString(i & 0xFFFF) + ";";
            String input = "Test input: " + entity;
            int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
            assertEquals("Unexpected length", 2 + 1 + 2 + 1, length);
            assertEquals("Unexpected output: " + out.toString(), Character.toString((char) (i >> 8)), out.toString());
            assertEquals("Unexpected output: " + out.toString(), Character.toString((char) (i & 0xFF)), out.toString());
            out.getBuffer().setLength(0);
        }
    }

    @Test
    public void testTranslateNumbersWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        for (int i = 0x21; i <= 0x7E; i++) {
            String entity = "&" + Integer.toHexString(i & 0xFFFF);
            String input = "Test input: " + entity;
            int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
            assertEquals("Unexpected length", 1 + 1 + 2, length);
            assertEquals("Unexpected output: " + out.toString(), Character.toString((char) i), out.toString());
            out.getBuffer().setLength(0);
        }
        for (int i = 0x80; i <= 0xFFFF; i++) {
            String entity = "&" + Integer.toHexString(i & 0xFFFF);
            String input = "Test input: " + entity;
            int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
            assertEquals("Unexpected length", 2 + 1 + 2, length);
            assertEquals("Unexpected output: " + out.toString(), Character.toString((char) (i >> 8)), out.toString());
            assertEquals("Unexpected output: " + out.toString(), Character.toString((char) (i & 0xFF)), out.toString());
            out.getBuffer().setLength(0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "Test input: &asdf;";
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntityWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "Test input: &asdf;";
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidNumber() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "Test input: &abcd;";
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidNumberWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "Test input: &abcd;";
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
    }

    @Test
    public void testTranslateEmptyString() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "";
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
        assertEquals("Unexpected length", 0, length);
    }

    @Test
    public void testTranslateNullEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = null;
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, out);
        assertEquals("Unexpected length", 0, length);
    }
}