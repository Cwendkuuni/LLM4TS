package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testGetHighestEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestEscapedChar());
        jsonGenerator.setHighestNonEscapedChar(0);
        assertNull(jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 100, 200 }; // Custom escape codes
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(new int[] { 100, 200 }, jsonGenerator.getCharacterEscapes().getEscapeCodesForAscii());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        SerializableString rootValueSeparator = new SerializableString("_");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRootValueSeparatorNull() {
        new JsonGeneratorImpl(null, 0, null).setRootValueSeparator(null);
    }
}