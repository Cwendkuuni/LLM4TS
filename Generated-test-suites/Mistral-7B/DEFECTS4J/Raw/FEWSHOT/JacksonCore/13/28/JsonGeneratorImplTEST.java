package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testSetHighestNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);

        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator.getHighestEscapedChar());

        jsonGenerator.setHighestNonEscapedChar(0);
        assertNull(jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);

        CharacterEscapes characterEscapes = new CharacterEscapes() {};
        jsonGenerator.setCharacterEscapes(characterEscapes);
        int[] outputEscapes = jsonGenerator.getOutputEscapes();
        assertNull(outputEscapes);

        jsonGenerator.setCharacterEscapes(null);
        outputEscapes = jsonGenerator.getOutputEscapes();
        assertEquals(JsonGeneratorImpl.sOutputEscapes, outputEscapes);
    }

    @Test
    public void testConfigureQuoteFieldNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);

        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);

        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testSetRootValueSeparator() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, null);

        String separator = "test";
        jsonGenerator.setRootValueSeparator(new SerializableString(separator));
        assertEquals(separator, jsonGenerator._rootValueSeparator.getValue());
    }
}