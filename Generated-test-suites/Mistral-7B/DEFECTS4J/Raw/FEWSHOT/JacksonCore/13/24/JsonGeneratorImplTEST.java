package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImpl() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        IOContext ioContext = new IOContext();
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{123, 456};
            }
        };
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        int[] outputEscapes = jsonGeneratorImpl.getOutputEscapes();
        assertArrayEquals(new int[]{123, 456}, outputEscapes);
    }

    @Test
    public void testGetCharacterEscapes() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        CharacterEscapes characterEscapes = jsonGeneratorImpl.getCharacterEscapes();
        assertNull(characterEscapes);
    }

    @Test
    public void testSetRootValueSeparator() throws Exception {
        IOContext ioContext = new IOContext();
        SerializableString rootValueSeparator = new SerializableString("rootValueSeparator");
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl.getRootValueSeparator());
    }

    @Test
    public void testGetRootValueSeparator() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        SerializableString rootValueSeparator = jsonGeneratorImpl.getRootValueSeparator();
        assertNull(rootValueSeparator);
    }

    @Test
    public void testEnableQuoteFieldNames() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testDisableQuoteFieldNames() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl._cfgUnqNames = false;
        assertTrue(jsonGeneratorImpl._cfgUnqNames);
        jsonGeneratorImpl.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(255);
        assertEquals(255, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetHighestNonEscapedCharNegativeValue() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }
}