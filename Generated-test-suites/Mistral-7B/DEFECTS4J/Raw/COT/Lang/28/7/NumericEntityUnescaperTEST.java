package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * JUnit 4 test cases for the NumericEntityUnescaper class
 */
public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    /**
     * Test translate method with valid numeric entity
     */
    @Test
    public void testTranslateValidEntity() {
        StringBuilder input = new StringBuilder("&#x1F60A;");
        StringWriter outWriter = new StringWriter();

        int length = unescaper.translate(input, 0, outWriter);

        assertEquals("ō", outWriter.toString());
        assertEquals(1, length);
    }

    /**
     * Test translate method with valid numeric entity in the middle of a string
     */
    @Test
    public void testTranslateValidEntityMiddleOfString() {
        StringBuilder input = new StringBuilder("Hello &#10;World!");
        StringWriter outWriter = new StringWriter();

        int length = unescaper.translate(input, 0, outWriter);

        assertEquals("Hello \nWorld!", outWriter.toString());
        assertEquals(input.length(), length);
    }

    /**
     * Test translate method with valid decimal entity
     */
    @Test
    public void testTranslateValidDecimalEntity() {
        StringBuilder input = new StringBuilder("&#13;");
        StringWriter outWriter = new StringWriter();

        int length = unescaper.translate(input, 0, outWriter);

        assertEquals("\n", outWriter.toString());
        assertEquals(1, length);
    }

    /**
     * Test translate method with invalid character sequence
     */
    @Test(expected = IOException.class)
    public void testTranslateInvalidCharacterSequence() {
        StringBuilder input = new StringBuilder("&%#;");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, 0, outWriter);
    }

    /**
     * Test translate method with empty input
     */
    @Test
     public void testTranslateEmptyInput() {
         StringBuilder input = new StringBuilder();
         StringWriter outWriter = new StringWriter();

         int length = unescaper.translate(input, 0, outWriter);

         assertEquals("", outWriter.toString());
         assertEquals(0, length);
     }

    /**
     * Test translate method with input out of bounds
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateInputOutOfBounds() {
        StringBuilder input = new StringBuilder("&#[xAB];");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, input.length(), outWriter);
    }

    /**
     * Test translate method with hex entity but invalid hex digits
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexEntity() {
        StringBuilder input = new StringBuilder("&#[xABC];");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, 0, outWriter);
    }

    /**
     * Test translate method with decimal entity but invalid decimal number
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidDecimalEntity() {
        StringBuilder input = new StringBuilder("&#[0xAB];");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, 0, outWriter);
    }

    /**
     * Test translate method with hex entity but missing x
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateMissingHexPrefix() {
        StringBuilder input = new StringBuilder("&#xAB;");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, 0, outWriter);
    }

    /**
     * Test translate method with hex entity but missing number
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateMissingHexNumber() {
        StringBuilder input = new StringBuilder("&#[x];");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, 0, outWriter);
    }

    /**
     * Test translate method with decimal entity but missing #
     */
    @Test(expected = IOException.class)
    public void testTranslateMissingDecimalPrefix() {
        StringBuilder input = new StringBuilder("&1F60A;");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, 0, outWriter);
    }

    /**
     * Test translate method with decimal entity but missing number
     */
    @Test(expected = IOException.class)
    public void testTranslateMissingDecimalNumber() {
        StringBuilder input = new StringBuilder("&#;");
        StringWriter outWriter = new StringWriter();

        unescaper.translate(input, 0, outWriter);
    }
}