package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;

import org.junit.Test;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with empty lookup array.
     */
    @Test
    public void testEmptyLookupArray() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        assertNull(translator.lookupMap);
        assertNull(translator.shortest);
        assertNull(translator.longest);
    }

    /**
     * Test constructor with valid lookup array.
     */
    @Test
    public void testValidLookupArray() {
        CharSequence key1 = "key1";
        CharSequence value1 = "value1";
        CharSequence key2 = "key2";
        CharSequence value2 = "value2";
        CharSequence key3 = "key3";
        CharSequence value3 = "value3";

        CharSequence[][] lookup = {
                {key1, value1},
                {key2, value2},
                {key3, value3}
        };

        LookupTranslator translator = new LookupTranslator(lookup);
        HashMap<CharSequence, CharSequence> expectedMap = new HashMap<>();
        expectedMap.put(key1, value1);
        expectedMap.put(key2, value2);
        expectedMap.put(key3, value3);
        assertEquals(expectedMap, translator.lookupMap);
        assertEquals(3, translator.shortest);
        assertEquals(3, translator.longest);
    }

    /**
     * Test translate method with valid input.
     */
    @Test
    public void testTranslateValidInput() throws IOException {
        CharSequence key1 = "key1";
        CharSequence value1 = "value1";
        CharSequence key2 = "key2";
        CharSequence value2 = "value2";
        CharSequence key3 = "key3";
        CharSequence value3 = "value3";

        CharSequence[][] lookup = {
                {key1, value1},
                {key2, value2},
                {key3, value3}
        };

        LookupTranslator translator = new LookupTranslator(lookup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);

        CharSequence input = "input" + key1;
        int expectedIndex = 0;
        int expectedLength = key1.length();
        int result = translator.translate(input, expectedIndex, outputWriter);
        byte[] expectedOutput = value1.toString().getBytes();
        byte[] actualOutput = outputStream.toByteArray();
        assertEquals(expectedLength, result);
        assertEquals(expectedOutput.length, actualOutput.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], actualOutput[i]);
        }
    }

    /**
     * Test translate method with input shorter than the shortest key.
     */
    @Test
    public void testTranslateInputShorterThanShortestKey() throws IOException {
        CharSequence key1 = "key1";
        CharSequence value1 = "value1";
        CharSequence key2 = "key2";
        CharSequence value2 = "value2";
        CharSequence key3 = "key3";
        CharSequence value3 = "value3";

        CharSequence[][] lookup = {
                {key1, value1},
                {key2, value2},
                {key3, value3}
        };

        LookupTranslator translator = new LookupTranslator(lookup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);

        CharSequence input = "input";
        int expectedIndex = 0;
        int result = translator.translate(input, expectedIndex, outputWriter);
        assertEquals(0, result);
    }

    /**
     * Test translate method with input longer than the longest key but not longer than the input string.
     */
    @Test
    public void testTranslateInputLongerThanLongestKeyButNotLongerThanInputString() throws IOException {
        CharSequence key1 = "key1";
        CharSequence value1 = "value1";
        CharSequence key2 = "key2";
        CharSequence value2 = "value2";

        CharSequence[][] lookup = {
                {key1, value1},
                {key2, value2}
        };

        LookupTranslator translator = new LookupTranslator(lookup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);

        CharSequence input = "input" + key1 + "suffix";
        int expectedIndex = 0;
        int expectedLength = key1.length();
        int result = translator.translate(input, expectedIndex, outputWriter);
        assertEquals(expectedLength, result);
    }

    /**
     * Test translate method with input longer than the longest key and longer than the input string.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateInputLongerThanLongestKeyAndLongerThanInputString() throws IOException {
        CharSequence key1 = "key1";
        CharSequence value1 = "value1";
        CharSequence key2 = "key2";
        CharSequence value2 = "value2";

        CharSequence[][] lookup = {
                {key1, value1},
                {key2, value2}
        };

        LookupTranslator translator = new LookupTranslator(lookup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);

        CharSequence input = "input" + key1 + "suffix";
        int expectedIndex = 0;
        int indexAfterKey = expectedIndex + key1.length();
        int result = translator.translate(input, indexAfterKey, outputWriter);
    }

    /**
     * Test translate method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullInput() throws IOException {
        CharSequence[][] lookup = {
                {"key1", "value1"},
                {"key2", "value2"}
        };

        LookupTranslator translator = new LookupTranslator(lookup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);

        CharSequence input = null;
        int index = 0;
        int result = translator.translate(input, index, outputWriter);
    }

    /**
     * Test translate method with null outputWriter.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullOutputWriter() throws IOException {
        CharSequence key1 = "key1";
        CharSequence value1 = "value1";
        CharSequence key2 = "key2";
        CharSequence value2 = "value2";

        CharSequence[][] lookup = {
                {key1, value1},
                {key2, value2}
        };

        LookupTranslator translator = new LookupTranslator(lookup);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputWriter = null;

        CharSequence input = "input" + key1;
        int index = 0;
        int result = translator.translate(input, index, outputWriter);
    }

    /**
     * Test translate method with null lookup array.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullLookupArray() throws IOException {
        LookupTranslator translator = new LookupTranslator(null);
    }
}