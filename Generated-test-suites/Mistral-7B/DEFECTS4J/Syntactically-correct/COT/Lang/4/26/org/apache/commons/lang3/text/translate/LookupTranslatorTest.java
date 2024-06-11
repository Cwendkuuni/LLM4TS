package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * LookupTranslatorTest
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithNullLookup() {
        LookupTranslator translator = new LookupTranslator(null);
        assertNull(translator.lookupMap);
    }

    @Test
    public void testLookupTranslatorWithEmptyLookup() {
        CharSequence[][] lookup = new CharSequence[0][2];
        LookupTranslator translator = new LookupTranslator(lookup);
        assertTrue(translator.lookupMap.isEmpty());
    }

    @Test
    public void testLookupTranslatorWithValidLookup() {
        CharSequence key1 = "hello";
        CharSequence value1 = "hi";
        CharSequence key2 = "world";
        CharSequence value2 = "hello";
        CharSequence[][] lookup = new CharSequence[][] { { key1, value1 }, { key2, value2 } };

        LookupTranslator translator = new LookupTranslator(lookup);
        HashMap<CharSequence, CharSequence> expectedMap = new HashMap<>();
        expectedMap.put(key1, value1);
        expectedMap.put(key2, value2);

        assertEquals(expectedMap, translator.lookupMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithInvalidLookup() {
        CharSequence[][] lookup = new CharSequence[1][2];
        lookup[0][0] = "hello";
        lookup[0][1] = "hi";
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate("world", 0, null);
    }

    @Test
    public void testTranslateWithShortestInput() throws IOException {
        CharSequence key1 = "hello";
        CharSequence value1 = "hi";
        CharSequence[][] lookup = new CharSequence[][] { { key1, value1 } };
        LookupTranslator translator = new LookupTranslator(lookup);
        String expectedOutput = "hi";

        StringWriter output = new StringWriter();
        int length = key1.length();
        int index = 0;
        int result = translator.translate(key1, index, output);
        assertEquals(length, result);
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateWithLongestInput() throws IOException {
        CharSequence key1 = "hello";
        CharSequence value1 = "hi";
        CharSequence key2 = "world";
        CharSequence value2 = "hello";
        CharSequence[][] lookup = new CharSequence[][] { { key1, value1 }, { key2, value2 } };
        LookupTranslator translator = new LookupTranslator(lookup);
        String expectedOutput = "hello";

        StringWriter output = new StringWriter();
        int inputLength = key2.length();
        int index = 0;
        int result = translator.translate(key2, index, output);
        assertEquals(key2.length(), result);
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateWithInputLongerThanLongestLookup() throws IOException {
        CharSequence key1 = "hello";
        CharSequence value1 = "hi";
        CharSequence key2 = "worldhello";
        CharSequence value2 = "hello";
        CharSequence[][] lookup = new CharSequence[][] { { key1, value1 }, { key2, value2 } };
        LookupTranslator translator = new LookupTranslator(lookup);
        String expectedOutput = "hello";

        StringWriter output = new StringWriter();
        int inputLength = key2.length();
        int index = 0;
        int result = translator.translate(key2, index, output);
        assertEquals(key1.length(), result);
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslateWithInputShorterThanShortestLookup() throws IOException {
        CharSequence key1 = "hello";
        CharSequence value1 = "hi";
        CharSequence[][] lookup = new CharSequence[][] { { key1, value1 } };
        LookupTranslator translator = new LookupTranslator(lookup);
        String expectedOutput = "";

        StringWriter output = new StringWriter();
        int inputLength = key1.length() - 1;
        int index = 0;
        int result = translator.translate(key1, index, output);
        assertEquals(0, result);
        assertEquals(expectedOutput, output.toString());
    }

}