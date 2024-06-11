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
     * Test translating a simple numeric entity.
     */
    @Test
    public void testSimpleNumericEntity() {
        String input = "&amp;&#160;&#32;&#128;&#163;&#255;&#65279;";
        String expectedOutput = " &amp;    ï ³ ‡ µ ␵";
        String output = translateAndCompare(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test translating a hexadecimal numeric entity.
     */
    @Test
    public void testHexNumericEntity() {
        String input = "&amp;&#x20AC;&#x0020;&#x0250;&#x00A3;&#xFFFD;&#xfeff;";
        String expectedOutput = " &amp;€    ð ¢ ³ ╳ ␵␶";
        String output = translateAndCompare(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test translating an entity without a semicolon.
     */
    @Test
    public void testEntityWithoutSemicolon() {
        String input = "&amp;&#160";
        String output = translateAndCompare(input);
        assertEquals(" &amp; ", output);
    }

    /**
     * Test translating an entity with an invalid value.
     */
    @Test(expected = RuntimeException.class)
    public void testInvalidEntity() {
        String input = "&amp;&#160xAB";
        translateAndCompare(input);
    }

    /**
     * Test translating an entity with an invalid character after the number.
     */
    @Test(expected = RuntimeException.class)
    public void testInvalidCharacterAfterEntity() {
        String input = "&amp;&#160;x";
        translateAndCompare(input);
    }

    /**
     * Test translating an empty string.
     */
    @Test
    public void testEmptyString() {
        String input = "";
        String output = translateAndCompare(input);
        assertEquals("", output);
    }

    /**
     * Test translating a null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(null, 0, null);
    }

    /**
     * Test translating an input with an index out of bounds exception.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        String input = "&amp;&#160";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(input, input.length(), null);
    }

    /**
     * Test translating an input with an invalid writer.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidWriter() {
        String input = "&amp;&#160";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(input, 0, null);
    }

    /**
     * Translates the input and compares the result with the expected output.
     *
     * @param input The input string for translation.
     * @return The translated string.
     */
    private String translateAndCompare(String input) {
        StringBuilder output = new StringBuilder();
        int translatedChars = unescaper.translate(input, 0, output);
        return output.toString();
    }

}