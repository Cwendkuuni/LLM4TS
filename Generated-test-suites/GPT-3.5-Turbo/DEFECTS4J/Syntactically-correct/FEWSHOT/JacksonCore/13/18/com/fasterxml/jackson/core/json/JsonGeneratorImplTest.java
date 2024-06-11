package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl generator;

    @Before
    public void setup() {
        generator = new JsonGeneratorImpl(null, 0, null) {
            // implementation for abstract methods in JsonGeneratorImpl
            @Override
            public void writeStartArray() {}
            @Override
            public void writeEndArray() {}
            @Override
            public void writeStartObject() {}
            @Override
            public void writeEndObject() {}
            @Override
            public void writeFieldName(String name) {}
            @Override
            public void writeString(String text) {}
            @Override
            public void writeNumber(int v) {}
            @Override
            public void writeNumber(long v) {}
            @Override
            public void writeNumber(double d) {}
            @Override
            public void writeBoolean(boolean state) {}
            @Override
            public void writeNull() {}
        };
    }

    @Test
    public void testEnableFeature() {
        assertFalse(generator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        generator.setHighestNonEscapedChar(100);
        assertEquals(100, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        assertNull(generator.getCharacterEscapes());
        generator.setCharacterEscapes(null);
        assertNull(generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator._rootValueSeparator);
        generator.setRootValueSeparator(null);
        assertNull(generator._rootValueSeparator);
    }
}