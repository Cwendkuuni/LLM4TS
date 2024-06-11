package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testgetDefaults() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        assertNull(jsonGenerator.getCharacterEscapes());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator.getRootValueSeparator());
        assertEquals(CharTypes.get7BitOutputEscapes(), jsonGenerator.getOutputEscapes());
        assertFalse(jsonGenerator.isQuoteFieldNamesEnabled());
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{4, 5};
            }
        };

        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGenerator.getCharacterEscapes());
        assertEquals(new int[]{4, 5}, jsonGenerator.getOutputEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        SerializableString separator = new SerializableString(":");
        jsonGenerator.setRootValueSeparator(separator);
        assertEquals(separator, jsonGenerator.getRootValueSeparator());
    }

    @Test
    public void testEnableQuoteFieldNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator.isQuoteFieldNamesEnabled());
    }

    @Test
    public void testSetMaximumNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGenerator.getHighestEscapedChar());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaximumNonEscapedCharNegative() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setHighestNonEscapedChar(-1);
    }
}