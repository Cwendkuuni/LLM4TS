package com.fasterxml.jackson.core.json;

import static com.fasterxml.jackson.core.api.TestConstants.STANDARD_JSON_FEATURES;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        assertNull(generator._characterEscapes);
        assertEquals(127, generator._maximumNonEscapedChar);
        assertFalse(generator._cfgUnqNames);
        assertTrue(Feature.QUOTE_FIELD_NAMES.enabledIn(generator._features));
    }

    @Test
    public void testEnableQuoteFieldNames() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES | Feature.QUOTE_FIELD_NAMES.getMask(), new ObjectMapper.DefaultObjectCodec());
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(generator._cfgUnqNames);
    }

    @Test
    public void testDisableQuoteFieldNames() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        generator.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(generator._cfgUnqNames);
    }

    @Test(expected = JsonMappingException.class)
    public void testDisableQuoteFieldNamesTwice() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.enable(Feature.QUOTE_FIELD_NAMES);
        generator.disable(Feature.QUOTE_FIELD_NAMES);
        generator.disable(Feature.QUOTE_FIELD_NAMES);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.setHighestNonEscapedChar(100);
        assertEquals(100, generator._maximumNonEscapedChar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHighestNonEscapedChar() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext();
        CharacterEscapes escapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 128, 129 };
            }
        };
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.setCharacterEscapes(escapes);
        int[] expected = new int[] { 128, 129 };
        assertArrayEquals(expected, generator._outputEscapes);
    }

    @Test
    public void testSetCharacterEscapesToNull() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.setCharacterEscapes(null);
        assertNull(generator._characterEscapes);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, generator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext();
        SerializableString separator = new SerializableString(":");
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.setRootValueSeparator(separator);
        assertEquals(separator, generator._rootValueSeparator);
    }

    @Test
    public void testSetRootValueSeparatorToNull() {
        IOContext ctxt = new IOContext();
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, STANDARD_JSON_FEATURES, new ObjectMapper.DefaultObjectCodec());
        generator.setRootValueSeparator(null);
        assertNull(generator._rootValueSeparator);
    }
}