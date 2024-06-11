package com.fasterxml.jackson.core.json;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        IOContext ctxt = new IOContext(null, null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertNotNull(generator);
        assertEquals(0, generator.getFeatureMask());
        assertNull(generator.getCodec());
        assertNull(generator.getCharacterEscapes());
        assertEquals(0, generator.getHighestEscapedChar());
    }

    @Test
    public void testEnable() {
        IOContext ctxt = new IOContext(null, null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertEquals(0, generator.getFeatureMask());
        generator.enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        assertTrue(generator.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertFalse(generator.isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET));
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ctxt = new IOContext(null, null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertEquals(0, generator.getHighestEscapedChar());
        generator.setHighestNonEscapedChar(127);
        assertEquals(127, generator.getHighestEscapedChar());
        generator.setHighestNonEscapedChar(200);
        assertEquals(200, generator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ctxt = new IOContext(null, null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertNull(generator.getCharacterEscapes());
        generator.setCharacterEscapes(null);
        assertNull(generator.getCharacterEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ctxt = new IOContext(null, null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertNull(generator.getRootValueSeparator());
        generator.setRootValueSeparator(null);
        assertNull(generator.getRootValueSeparator());
    }

    @Test
    public void testVersion() {
        IOContext ctxt = new IOContext(null, null, null, false);
        JsonGeneratorImpl generator = new JsonGeneratorImpl(ctxt, 0, null);
        assertNotNull(generator.version());
    }
}