package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        generator = new JsonGeneratorImpl(null, 0, null) {
            @Override
            public void close() {
            }
            @Override
            public void flush() {
            }
            @Override
            public void writeStartArray() {
            }
            @Override
            public void writeEndArray() {
            }
            @Override
            public void writeStartObject() {
            }
            @Override
            public void writeEndObject() {
            }
            @Override
            public void writeFieldName(String name) {
            }
            @Override
            public void writeString(String text) {
            }
        };
    }

    @Test
    public void testWriteStringField() throws Exception {
        generator.writeStringField("fieldName", "value");
        //TODO: Add assertions
    }
    
    @Test
    public void testEnable_quoteFieldNamesFeature() throws Exception {
        generator.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
    }
    
    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        generator.setHighestNonEscapedChar(255);
        assertEquals(255, generator._maximumNonEscapedChar);
    }
    
    @Test
    public void testGetHighestEscapedChar() throws Exception {
        generator.setHighestNonEscapedChar(127);
        assertEquals(127, generator.getHighestEscapedChar());
    }
    
    @Test
    public void testSetCharacterEscapes() throws Exception {
        generator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return null;
            }
            @Override
            public SerializableString getEscapeSequence(int ch) {
                return null;
            }
        });
        //TODO: Add assertions
    }
    
    @Test
    public void testSetRootValueSeparator() throws Exception {
        generator.setRootValueSeparator(null);
        //TODO: Add assertions
    }
}