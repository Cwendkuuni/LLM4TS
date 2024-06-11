package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNull(jsonGenerator._ioContext);
        assertNull(jsonGenerator._characterEscapes);
        assertTrue(jsonGenerator._cfgUnqNames);
        assertNull(jsonGenerator._rootValueSeparator);
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testJsonGeneratorImplWithIoContext() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        assertSame(context, jsonGenerator._ioContext);
        assertNull(jsonGenerator._characterEscapes);
        assertTrue(jsonGenerator._cfgUnqNames);
        assertNull(jsonGenerator._rootValueSeparator);
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testJsonGeneratorImplWithCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {};
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null) {
            {
                setCharacterEscapes(characterEscapes);
            }
        };
        assertNull(jsonGenerator._ioContext);
        assertSame(characterEscapes, jsonGenerator._characterEscapes);
        assertTrue(jsonGenerator._cfgUnqNames);
        assertNull(jsonGenerator._rootValueSeparator);
        assertNotEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testJsonGeneratorImplWithRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("rootValueSeparator");
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null) {
            {
                setRootValueSeparator(rootValueSeparator);
            }
        };
        assertNull(jsonGenerator._ioContext);
        assertNull(jsonGenerator._characterEscapes);
        assertTrue(jsonGenerator._cfgUnqNames);
        assertSame(rootValueSeparator, jsonGenerator._rootValueSeparator);
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(127, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testJsonGeneratorImplWithMaxNonEscapedChar() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null) {
            {
                setHighestNonEscapedChar(255);
            }
        };
        assertNull(jsonGenerator._ioContext);
        assertNull(jsonGenerator._characterEscapes);
        assertTrue(jsonGenerator._cfgUnqNames);
        assertNull(jsonGenerator._rootValueSeparator);
        assertEquals(sOutputEscapes, jsonGenerator._outputEscapes);
        assertEquals(255, jsonGenerator._maximumNonEscapedChar);
    }

    @Test
    public void testJsonGeneratorImplWithQuoteFieldNames() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, Feature.QUOTE_FIELD_NAMES.asInt(), null);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testJsonGeneratorImplWithQuoteFieldNames_true() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null) {
            {
                enable(Feature.QUOTE_FIELD_NAMES);
            }
        };
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testJsonGeneratorImplWithQuoteFieldNames_false() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null) {
            {
                disable(Feature.QUOTE_FIELD_NAMES);
            }
        };
        assertTrue(jsonGenerator._cfgUnqNames);
    }
}