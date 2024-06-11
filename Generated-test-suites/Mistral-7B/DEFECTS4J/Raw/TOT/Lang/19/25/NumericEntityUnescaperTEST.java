package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test translating a simple entity with no semicolon.
     */
    @Test
    public void testSimpleEntity() {
        CharSequence input = "&#[x1F600];"; // Happy Face
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);

        assertEquals("&#x1F600;", input.subSequence(0, 6).toString());
        assertEquals("\uD83C\uDC00", out.toString().substring(0, 2));
        assertEquals(5, length);
    }

    /**
     * Test translating a simple entity with a semicolon.
     */
    @Test
    public void testEntityWithSemicolon() {
        CharSequence input = "&#[x1F600];"; // Happy Face
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);

        assertEquals("&#x1F600;", input.subSequence(0, 6).toString());
        assertEquals("\uD83C\uDC00", out.toString().substring(0, 2));
        assertEquals(5, length);
    }

    /**
     * Test translating an entity that is too large for a single character.
     */
    @Test(expected = IOException.class)
    public void testLargeEntity() {
        CharSequence input = "&#[1114111];"; // Emoji Sequence
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);
    }

    /**
     * Test translating an entity that doesn't start with '&'.
     */
    @Test
    public void testInvalidEntityStart() {
        CharSequence input = "invalidStart&#[x1F600];";
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);

        assertEquals("invalidStart&#[x1F600];", input.toString());
        assertEquals("", out.toString());
        assertEquals(0, length);
    }

    /**
     * Test translating an entity with an invalid prefix.
     */
    @Test
    public void testInvalidEntityPrefix() {
        CharSequence input = "%&#[x1F600];";
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);

        assertEquals("%&#[x1F600];", input.toString());
        assertEquals("", out.toString());
        assertEquals(0, length);
    }

    /**
     * Test translating an entity with an invalid character after '#'.
     */
    @Test(expected = IOException.class)
    public void testInvalidCharacterAfterHash() {
        CharSequence input = "&#[x1F600a];";
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);
    }

    /**
     * Test translating an entity with an invalid hexadecimal character.
     */
    @Test(expected = IOException.class)
    public void testInvalidHexCharacter() {
        CharSequence input = "&#[x1F6Z0];"; // Invalid character 'Z'
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);
    }

    /**
     * Test translating an entity with a hexadecimal number higher than 0xFFFF.
     */
    @Test
    public void testLargeHexEntity() {
        CharSequence input = "&#[x110000];";
        Writer out = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, 0, out);

        assertEquals("&#[x110000];", input.toString());
        assertEquals("\uD83D\uDC00", out.toString().substring(0, 2)); // Grinning Face with Big Eyes and Smiling Eyes
        assertEquals(4, length);
    }
}