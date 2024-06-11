package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConfiguration() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNull(jsonGenerator.getCharacterEscapes());
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
        assertFalse(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testConfigurationWithQuoteFieldNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, Feature.QUOTE_FIELD_NAMES.asInt(), null);
        assertTrue(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testConfigurationWithCustomCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{1, 2, 3};
            }
        };
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(new int[]{1, 2, 3}, jsonGenerator.getOutputEscapes());
    }

    @Test
    public void testConfigurationWithHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(100);
        assertEquals(100, jsonGenerator.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationWithNegativeHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testChangeConfiguration() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator.isEnabled(Feature.QUOTE_FIELD_NAMES));

        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{4, 5, 6};
            }
        });
        assertEquals(new int[]{4, 5, 6}, jsonGenerator.getOutputEscapes());

        jsonGenerator.setHighestNonEscapedChar(200);
        assertEquals(200, jsonGenerator.getHighestEscapedChar());
    }
}