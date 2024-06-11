package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNull(jsonGenerator._characterEscapes);
        assertEquals(JsonGenerator.Feature.QUOTE_FIELD_NAMES, jsonGenerator._cfgUnqNames);
        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.getDefaultValue(), jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testConstructionWithFeatures() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0xFFFF, null);
        assertNull(jsonGenerator._characterEscapes);
        assertFalse(jsonGenerator._cfgUnqNames);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{0x22, 0x22, 0x5C};
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
        assertEquals(new int[]{0x22, 0x22, 0x5C}, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        SerializableString rootValueSeparator = new SerializableString("root");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testEnableQuoteFieldNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(0xFF);
        assertEquals(0xFF, jsonGenerator.getHighestEscapedChar());
    }
}