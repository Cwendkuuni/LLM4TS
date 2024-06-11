package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = new CharSequence[][] { { new String("apple"), new String("pomme") },
                { new String("banana"), new String("plantain") },
                { new String("orange"), new String("citron") } };

        LookupTranslator translator = new LookupTranslator(lookup);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("apple", "pomme");
        expectedMap.put("banana", "plantain");
        expectedMap.put("orange", "citron");

        assertEquals(expectedMap, translator.getLookupMap());
        assertEquals(3, translator.getShortest());
        assertEquals(6, translator.getLongest());
    }

    @Test
    public void testTranslateWithSuccessfulMatch() {
        CharSequence[][] lookup = new CharSequence[][] { { new String("apple"), new String("pomme") },
                { new String("banana"), new String("plantain") },
                { new String("orange"), new String("citron") } };

        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "apple";
        int index = 0;

        int length = translator.translate(input, index, writer);
        assertEquals("pomme", writer.toString());
        assertEquals(5, length);
    }

    @Test
    public void testTranslateWithSuccessfulMatchLongestSequence() {
        CharSequence[][] lookup = new CharSequence[][] { { new String("apple"), new String("pomme") },
                { new String("banana"), new String("plantain") },
                { new String("orange"), new String("citron") } };

        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "applemegapple";
        int index = 0;

        int length = translator.translate(input, index, writer);
        assertEquals("pommeapplepomme", writer.toString());
        assertEquals(11, length);
    }

    @Test
    public void testTranslateWithMultipleMatches() {
        CharSequence[][] lookup = new CharSequence[][] { { new String("apple"), new String("pomme") },
                { new String("banana"), new String("plantain") },
                { new String("orange"), new String("citron") },
                { new String("app"), new String("fruit") },
                { new String("ban"), new String("fruit") } };

        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "appban";
        int index = 0;

        int length = translator.translate(input, index, writer);
        assertEquals("fruitban", writer.toString());
        assertEquals(7, length);
    }

    @Test
    public void testTranslateWithNoMatch() {
        CharSequence[][] lookup = new CharSequence[][] { { new String("apple"), new String("pomme") },
                { new String("banana"), new String("plantain") },
                { new String("orange"), new String("citron") } };

        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "pear";
        int index = 0;

        int length = translator.translate(input, index, writer);
        assertNull(writer.toString());
        assertEquals(0, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupTable() {
        new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithEmptyLookupTable() {
        new LookupTranslator(new CharSequence[][] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithInvalidSequenceSize() {
        CharSequence[][] lookup = new CharSequence[][] { { new String("apple"), new String("pomme") },
                { new String("banana"), new String("plantain") },
                { new String("orange"), new String("citron") },
                { new String("apple"), new String("pomme"), new String("invalid") } };

        try {
            new LookupTranslator(lookup);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid lookup entry. The number of elements in the array does not match the number of expected elements.", e.getMessage());
            throw e;
        }
    }

}