package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Test;

/**
 * Test class for JsonGeneratorImpl
 */
public class JsonGeneratorImplTest {

    @Test
    public void testConstructor() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testEnableFeature() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testCheckStdFeatureChanges() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl._checkStdFeatureChanges(0xFFFF, 0xF000);
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        CharacterEscapes characterEscapes = new StandardCharacterEscapes();
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertArrayEquals(characterEscapes.getEscapeCodesForAscii(), jsonGeneratorImpl._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        SerializableString rootValueSeparator = new SerializableString("test");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl._rootValueSeparator);
    }

    @Test(expected = IOException.class)
    public void testWriteFieldNameNull() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.writeFieldName(null);
    }

    @Test(expected = IOException.class)
    public void testWriteStringNull() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.writeString(null);
    }
}