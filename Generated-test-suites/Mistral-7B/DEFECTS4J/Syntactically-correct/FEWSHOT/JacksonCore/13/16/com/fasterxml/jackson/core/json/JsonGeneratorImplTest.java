package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Test;

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
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{123};
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertArrayEquals(new int[]{123}, jsonGeneratorImpl.getCharacterEscapes().getEscapeCodesForAscii());
    }

    @Test
    public void testGetCharacterEscapes() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
    }

    @Test
    public void testEnableQuoteFieldNames() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_Names);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testDisableQuoteFieldNames() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.disable(Feature.QUOTE_FIELD_Names);
        assertTrue(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetHighestNonEscapedCharNegativeValue() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(-1);
        assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetHighestNonEscapedCharZeroValue() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        SerializableString rootValueSeparator = new SerializableString("root");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl._rootValueSeparator);
    }
}