package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);

        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator.getHighestNonEscapedChar());

        jsonGenerator.setHighestNonEscapedChar(0);
        assertNull(jsonGenerator.getHighestNonEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);

        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{-1, -2, -3};
            }
        };

        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertArrayEquals(new int[]{-1, -2, -3}, jsonGenerator._outputEscapes);

        jsonGenerator.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);

        SerializableString rootValueSeparator = new SerializableString("_");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator);
    }
}