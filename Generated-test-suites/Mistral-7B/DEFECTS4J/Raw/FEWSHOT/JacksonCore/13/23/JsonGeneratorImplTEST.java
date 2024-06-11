package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConfiguration() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        assertNotNull(jsonGenerator.getCharacterEscapes());
        assertEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator.getHighestEscapedChar());
        assertNull(jsonGenerator._characterEscapes);
        assertFalse(jsonGenerator._cfgUnqNames);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testConfiguration() {
        CharacterEscapes customEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{ 255 };
            }
        };
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setCharacterEscapes(customEscapes);
        assertEquals(new int[]{ 255 }, jsonGenerator._outputEscapes);

        jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setHighestNonEscapedChar(1234);
        assertEquals(1234, jsonGenerator.getHighestEscapedChar());

        jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.setRootValueSeparator(new SerializedString("custom_separator"));
        assertEquals(new SerializedString("custom_separator"), jsonGenerator._rootValueSeparator);

        jsonGenerator = new JsonGeneratorImpl(new IOContext(), Feature.QUOTE_FIELD_NAMES.asInt(), null);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testFeatureChanges() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);

        jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);
        jsonGenerator._checkStdFeatureChanges(Feature.QUOTE_FIELD_NAMES.asInt(), Feature.QUOTE_FIELD_NAMES.asInt());
        assertFalse(jsonGenerator._cfgUnqNames);

        jsonGenerator = new JsonGeneratorImpl(new IOContext(), Feature.ESCAPE_NON_ASCII.asInt(), null);
        jsonGenerator.setHighestNonEscapedChar(1234);
        assertEquals(1234, jsonGenerator.getHighestEscapedChar());

        jsonGenerator = new JsonGeneratorImpl(new IOContext(), Feature.QUOTE_FIELD_NAMES.asInt(), null);
        jsonGenerator.setCharacterEscapes(null);
        assertEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }
}