package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link JsonGeneratorImpl}.
 */
public class JsonGeneratorImplTest {

    private IOContext ioContext;
    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
    }

    // Test for constructor
    @Test
    public void testJsonGeneratorImplConstructor() {
        assertNotNull(jsonGeneratorImpl);
    }

    // Test for enable(Feature f) method
    @Test
    public void testEnableFeature() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    // Test for disable(Feature f) method
    @Test
    public void testDisableFeature() {
        jsonGeneratorImpl.disable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImpl._cfgUnqNames);
    }

    // Test for setHighestNonEscapedChar(int charCode) method
    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGeneratorImpl.getHighestEscapedChar());
    }

    // Test for setCharacterEscapes(CharacterEscapes esc) method
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 256 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(256, jsonGeneratorImpl._outputEscapes[0]);
    }

    // Test for getCharacterEscapes() method
    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 256 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertEquals(characterEscapes, jsonGeneratorImpl.getCharacterEscapes());
    }

    // Test for setRootValueSeparator(SerializableString sep) method
    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("test");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl._rootValueSeparator);
    }

    // Test for version() method
    @Test
    public void testVersion() {
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), jsonGeneratorImpl.version());
    }
}