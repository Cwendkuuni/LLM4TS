package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Unit tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());

        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);

        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{-1, -2, -3};
            }
        };

        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertArrayEquals(new int[]{-1, -2, -3}, jsonGeneratorImpl.getOutputEscapes());

        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGeneratorImpl.getOutputEscapes());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);

        SerializableString rootValueSeparator = new SerializableString("root");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl.getRootValueSeparator());

        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setRootValueSeparator(null);
        assertNull(jsonGeneratorImpl.getRootValueSeparator());
    }

    @Test
    public void testEnableFeatureQuoteFieldNames() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImpl._cfgUnqNames);

        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }
}