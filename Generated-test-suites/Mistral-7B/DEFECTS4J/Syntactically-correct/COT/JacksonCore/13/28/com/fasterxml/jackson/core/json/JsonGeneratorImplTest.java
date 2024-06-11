package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Test;

/**
 * Unit tests for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImplConstructor() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testEnableFeature() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl._checkStdFeatureChanges(1, Feature.QUOTE_FIELD_NAMES.getId());
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());

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
                return new int[] { 1 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(new int[] { 1 }, jsonGeneratorImpl._outputEscapes);

        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGeneratorImpl._outputEscapes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHighestNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(-1);
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setRootValueSeparator(new SerializableString("test"));
        assertEquals("test", jsonGeneratorImpl._rootValueSeparator.getValue());
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        Version version = jsonGeneratorImpl.version();
        assertNotNull(version);
    }
}