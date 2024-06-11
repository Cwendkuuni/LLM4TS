package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testDefaultConstruction() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        assertNull(jsonGenerator._characterEscapes);
        assertNull(jsonGenerator._outputEscapes);
        assertNull(jsonGenerator._maximumNonEscapedChar);
        assertNull(jsonGenerator._cfgUnqNames);
        assertNull(jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testConfiguration() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 1, null);

        assertEquals(0, jsonGenerator._maximumNonEscapedChar);
        assertFalse(jsonGenerator._cfgUnqNames);
        assertNull(jsonGenerator._rootValueSeparator);

        jsonGenerator.setCharacterEscapes(null);
        assertNull(jsonGenerator._characterEscapes);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);

        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 1, 2 };
            }
        });
        assertEquals(new int[] { 1, 2 }, jsonGenerator._outputEscapes);

        jsonGenerator.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGenerator._maximumNonEscapedChar);

        jsonGenerator.setRootValueSeparator("test");
        assertEquals("test", jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testFeatures() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 1, null);

        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGenerator._cfgUnqNames);

        jsonGenerator.disable(Feature.QUOTE_FIELD_NAMES);
        assertFalse(jsonGenerator._cfgUnqNames);

        jsonGenerator.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGenerator._maximumNonEscapedChar);

        jsonGenerator.setHighestNonEscapedChar(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, jsonGenerator._maximumNonEscapedChar);

        jsonGenerator.setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] {};
            }
        });
        assertArrayEquals(new int[0], jsonGenerator._outputEscapes);

        jsonGenerator.setCharacterEscapes(null);
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);

        jsonGenerator.setRootValueSeparator(null);
        assertNull(jsonGenerator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 1, null);
        assertEquals(VersionUtil.Version.JACKSON_CORE_2_1_0, jsonGenerator.version());
    }
}