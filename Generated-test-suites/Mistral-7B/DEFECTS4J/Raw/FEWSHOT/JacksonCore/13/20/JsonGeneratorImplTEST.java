package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);

        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestNonEscapedChar());

        jsonGenerator.setHighestNonEscapedChar(65535);
        assertEquals(65535, jsonGenerator.getHighestNonEscapedChar());

        jsonGenerator.setHighestNonEscapedChar(-1);
        assertNull(jsonGenerator.getHighestNonEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);

        CharacterEscapes standardEscapes = jsonGenerator.getCharacterEscapes();
        int[] standardOutputEscapes = standardEscapes.getEscapeCodesForAscii();

        CharacterEscapes customEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 92, 92, 92 }; // Custom escape codes for test
            }
        };

        jsonGenerator.setCharacterEscapes(customEscapes);
        int[] customOutputEscapes = jsonGenerator.getCharacterEscapes().getEscapeCodesForAscii();

        assertArrayEquals(new int[] { 92, 92, 92 }, customOutputEscapes);

        jsonGenerator.setCharacterEscapes(null);
        assertArrayEquals(standardOutputEscapes, jsonGenerator.getCharacterEscapes().getEscapeCodesForAscii());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);

        SerializableString rootValueSeparator = new SerializableString(":");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);

        assertEquals(rootValueSeparator, jsonGenerator.getRootValueSeparator());
    }
}