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
 * Test class for JsonGeneratorImpl
 */
public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImplConstructor() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testEnableFeature() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImpl._cfgUnqNames == false);
    }

    @Test
    public void testCheckStdFeatureChanges() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.checkStdFeatureChanges(1, Feature.QUOTE_FIELD_NAMES.getMask());
        assertTrue(jsonGeneratorImpl._cfgUnqNames == false);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGeneratorImpl.getHighestEscapedChar());

        jsonGeneratorImpl.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 1, 2, 3 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertArrayEquals(new int[] { 1, 2, 3 }, jsonGeneratorImpl._outputEscapes);

        jsonGeneratorImpl.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGeneratorImpl._outputEscapes);
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
    public void testWriteStringFieldWithNullFieldName() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.writeStringField(null, "value");
    }

    @Test
    public void testWriteStringField() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        String fieldName = "test";
        String value = "value";
        jsonGeneratorImpl.writeStringField(fieldName, value);
        // check if field name was written
        // check if value was written
    }

    @Test
    public void testVersion() throws Exception {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);
        assertEquals(VersionUtil.versionFor(JsonGeneratorImpl.class), jsonGeneratorImpl.version());
    }
}