package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    /**
     * Test escaping of numeric entities with semicolon.
     */
    @Test
    public void testWithSemicolon() {
        String input = "&amp;#128;#x41;#x1F4;#xFFFF;#x1F800;";
        String expectedOutput = "\u0080\u0041\u00D8\uffff\uD800\uDC00";
        String output = "";

        try (Writer writer = new StringBuilder(output).writer()) {
            unescaper.translate(new StringReader(input), 0, writer);
        }

        assertEquals(expectedOutput, output);
    }

    /**
     * Test escaping of numeric entities without semicolon.
     */
    @Test
    public void testWithoutSemicolon() {
        String input = "&amp;#128#x41#x1F4#xFFFF#x1F800";
        String expectedOutput = "\u0080\u0041\u00D8\uffff\uD800\uDC00";
        String output = "";

        try (Writer writer = new StringBuilder(output).writer()) {
            unescaper.translate(new StringReader(input), 0, writer);
        }

        assertEquals(expectedOutput, output);
    }

    /**
     * Test escaping of entities that fail to parse.
     */
    @Test(expected = NumberFormatException.class)
    public void testInvalidEntity() {
        String input = "&amp;#xyz;";
        String output = "";

        try (Writer writer = new StringBuilder(output).writer()) {
            unescaper.translate(new StringReader(input), 0, writer);
        }
    }

    /**
     * Test escaping of entities with negative values.
     */
    @Test(expected = NumberFormatException.class)
    public void testNegativeEntity() {
        String input = "&amp;#-1;";
        String output = "";

        try (Writer writer = new StringBuilder(output).writer()) {
            unescaper.translate(new StringReader(input), 0, writer);
        }
    }

    /**
     * Test escaping of entities outside the Unicode range.
     */
    @Test(expected = NumberFormatException.class)
    public void testOutOfRangeEntity() {
        String input = "&amp;#65536;";
        String output = "";

        try (Writer writer = new StringBuilder(output).writer()) {
            unescaper.translate(new StringReader(input), 0, writer);
        }
    }

    /**
     * Test escaping of entities with non-hexadecimal characters.
     */
    @Test(expected = NumberFormatException.class)
    public void testInvalidHexEntity() {
        String input = "&amp;#xABC;";
        String output = "";

        try (Writer writer = new StringBuilder(output).writer()) {
            unescaper.translate(new StringReader(input), 0, writer);
        }
    }

    /**
     * Test escaping of entities larger than two characters.
     */
    @Test
    public void testLargeEntity() {
        String input = "&amp;#1114111;";
        String expectedOutput = "\u00011\u00011\u00011";
        String output = "";

        try (Writer writer = new StringBuilder(output).writer()) {
            unescaper.translate(new StringReader(input), 0, writer);
        }

        assertEquals(expectedOutput, output);
    }
}