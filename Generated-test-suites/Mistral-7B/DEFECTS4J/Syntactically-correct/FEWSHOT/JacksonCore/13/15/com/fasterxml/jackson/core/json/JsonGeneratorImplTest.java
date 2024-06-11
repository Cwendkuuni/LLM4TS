package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {
    private IOContext ioContext;
    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() {
        ioContext = new IOContext();
        jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
    }

    @Test
    public void testVersion() {
        assertSame(VersionUtil.Version.VERSION_2_13_1, jsonGeneratorImpl.version());
    }

    @Test
    public void testEnabledFeatureQuoteFieldNames() {
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testDisabledFeatureQuoteFieldNames() {
        jsonGeneratorImpl._cfgUnqNames = true;
        assertFalse(jsonGeneratorImpl.isEnabled(Feature.QUOTE_FIELD_NAMES));
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{ 0x01 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        int[] outputEscapes = jsonGeneratorImpl.getCharacterEscapes().getEscapeCodesForAscii();
        assertEquals(new int[]{ 0x01 }, outputEscapes);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(0x03);
        assertEquals(0x03, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetHighestNonEscapedCharNegativeValue() {
        jsonGeneratorImpl.setHighestNonEscapedChar(-1);
        assertNull(jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString rootValueSeparator = new SerializableString("root");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl._rootValueSeparator);
    }
}