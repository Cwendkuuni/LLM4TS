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
    public void testJsonGeneratorImplConstructor() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGenerator);
    }

    @Test
    public void testEnableFeature() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);
    }

    @Test
    public void testDisableFeature() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFeature() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.enable(Feature.INVALID_FEATURE);
    }

    @Test
    public void testSetHighestNonEscapedChar() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setHighestNonEscapedChar(128);
        assertEquals(128, jsonGenerator.getHighestEscapedChar());

        jsonGenerator.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testSetCharacterEscapes() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);

        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{128, 129};
            }
        };
        jsonGenerator.setCharacterEscapes(characterEscapes);

        int[] outputEscapes = jsonGenerator._outputEscapes;
        assertArrayEquals(new int[]{128, 129}, outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() throws Exception {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);

        String rootValueSeparator = "test";
        jsonGenerator.setRootValueSeparator(new SerializableString(rootValueSeparator));
        assertEquals(rootValueSeparator, jsonGenerator._rootValueSeparator.getValue());
    }

    @Test
    public void testVersion() throws Exception {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertEquals(Version.unknownVersion(), jsonGenerator.version());
    }
}