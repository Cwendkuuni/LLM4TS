package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testEnableFeature() {
        IOContext ctxt = mock(IOContext.class);
        ObjectCodec codec = mock(ObjectCodec.class);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        generator.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = mock(IOContext.class);
        ObjectCodec codec = mock(ObjectCodec.class);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        generator.setHighestNonEscapedChar(100);
        assertEquals(100, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = mock(IOContext.class);
        ObjectCodec codec = mock(ObjectCodec.class);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        CharacterEscapes escapes = mock(CharacterEscapes.class);
        generator.setCharacterEscapes(escapes);
        assertEquals(escapes, generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = mock(IOContext.class);
        ObjectCodec codec = mock(ObjectCodec.class);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, codec);
        SerializableString sep = mock(SerializableString.class);
        generator.setRootValueSeparator(sep);
        assertEquals(sep, generator._rootValueSeparator);
    }
}