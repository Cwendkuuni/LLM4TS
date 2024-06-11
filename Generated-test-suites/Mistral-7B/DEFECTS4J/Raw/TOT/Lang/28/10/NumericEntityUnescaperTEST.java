package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link NumericEntityUnescaper} class.
 */
public class NumericEntityUnescaperTest {

    private final static String EMPTY_STRING = "";
    private final static String NUMERIC_ENTITY_TEST_CASE = "&#[x3A];&#10;";
    private final static String NUMERIC_ENTITY_WITH_HEX_CODE_TEST_CASE = "&#[x41];";
    private final static String NUMERIC_ENTITY_WITH_DECIMAL_CODE_TEST_CASE = "&#[41];";
    private final static String NUMERIC_ENTITY_WITH_INVALID_HEX_CODE_TEST_CASE = "&#[xz];";
    private final static String NUMERIC_ENTITY_WITH_INVALID_DECIMAL_CODE_TEST_CASE = "&#[-1];";

    private final static String INPUT_STRING_WITH_VALID_ENTITIES = "Input &#[x27];String&#[x22]; with &#[x26];entities&#[x3B];.";
    private final static String INPUT_STRING_WITH_INVALID_ENTITIES = "Input &#[xz];String&#[x22]; with &#[-1];entities&#[x3B];.";

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    /**
     * Test translating a valid numeric entity with decimal code.
     */
    @Test
    public void testTranslateDecimalEntity() throws IOException {
        String input = "&#[41];";
        String expectedOutput = "A";
        String actualOutput = translateAndCompare(input, expectedOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translating a valid numeric entity with hex code.
     */
    @Test
    public void testTranslateHexEntity() throws IOException {
        String input = "&#[x41];";
        String expectedOutput = "A";
        String actualOutput = translateAndCompare(input, expectedOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translating an invalid numeric entity with decimal code.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidDecimalEntity() throws IOException {
        String input = "&#[-1];";
        String expectedOutput = ""; // No output in this case
        String actualOutput = translateAndCompare(input, expectedOutput);
        fail("Expected NumberFormatException was not thrown.");
    }

    /**
     * Test translating an invalid numeric entity with hex code.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexEntity() throws IOException {
        String input = "&#[xz];";
        String expectedOutput = ""; // No output in this case
        translateAndCompare(input, expectedOutput);
        fail("Expected NumberFormatException was not thrown.");
    }

    /**
     * Test translating a string with valid numeric entities using the {@link NumericEntityUnescaper}.
     */
    @Test
    public void testTranslateStringWithValidEntities() throws IOException {
        String input = INPUT_STRING_WITH_VALID_ENTITIES;
        String expectedOutput = "Input String with entities.";
        String actualOutput = translateStringAndCompare(input, expectedOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translating a string with invalid numeric entities using the {@link NumericEntityUnescaper}.
     */
    @Test
    public void testTranslateStringWithInvalidEntities() throws IOException {
        String input = INPUT_STRING_WITH_INVALID_ENTITIES;
        String expectedOutput = "Input String with entities.";
        String actualOutput = translateStringAndCompare(input, expectedOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Helper method to translate a char sequence and compare the output with expected output.
     *
     * @param input Char sequence to be translated.
     * @param expectedOutput Expected output after translation.
     * @return Actual output after translation.
     * @throws IOException If an I/O error occurs.
     */
    private String translateAndCompare(String input, String expectedOutput) throws IOException {
        StringBuilder outputBuilder = new StringBuilder();
        int length = unescaper.translate(input, 0, outputBuilder, 0, input.length());
        String actualOutput = outputBuilder.toString();
        return actualOutput;
    }

    /**
     * Helper method to translate a char sequence and compare the output with expected output.
     *
     * @param input Char sequence to be translated.
     * @param expectedOutput Expected output after translation.
     * @return Actual output after translation.
     * @throws IOException If an I/O error occurs.
     */
    private String translateStringAndCompare(String input, String expectedOutput) throws IOException {
        StringBuilder outputBuilder = new StringBuilder();
        unescaper.translate(input, 0, outputBuilder);
        String actualOutput = outputBuilder.toString();
        return actualOutput;
    }
}