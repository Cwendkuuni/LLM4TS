package com.fasterxml.jackson.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl generator;

    @Before
    public void setUp() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = null;
        generator = new JsonGeneratorImpl(ctxt, features, codec) {
            public void close() { }
            public void flush() { }
        };
    }

    @Test
    public void testEnable() {
        JsonGenerator result = generator.enable(Feature.QUOTE_FIELD_NAMES);
        assertEquals(generator, result);
        assertTrue(generator._cfgUnqNames);
    }

    @Test
    public void test_checkStdFeatureChanges() {
        int newFeatureFlags = 0;
        int changedFeatures = 0;
        generator._checkStdFeatureChanges(newFeatureFlags, changedFeatures);
        assertFalse(generator._cfgUnqNames);
    }

    @Test
    public void testSetHighestNonEscapedChar() {
        int charCode = 127;
        JsonGenerator result = generator.setHighestNonEscapedChar(charCode);
        assertEquals(generator, result);
        assertEquals(charCode, generator._maximumNonEscapedChar);

        charCode = -1;
        result = generator.setHighestNonEscapedChar(charCode);
        assertEquals(generator, result);
        assertEquals(0, generator._maximumNonEscapedChar);
    }

    @Test
    public void testGetHighestEscapedChar() {
        int result = generator.getHighestEscapedChar();
        assertEquals(0, result);
    }

    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = null;
        JsonGenerator result = generator.setCharacterEscapes(esc);
        assertEquals(generator, result);
        assertArrayEquals(generator._outputEscapes, JsonGeneratorImpl.sOutputEscapes);

        esc = new CharacterEscapes() {
            public int[] getEscapeCodesForAscii() {
                return new int[] { 1, 2, 3, 4, 5 };
            }
        };
        result = generator.setCharacterEscapes(esc);
        assertEquals(generator, result);
        assertArrayEquals(generator._outputEscapes, new int[] { 1, 2, 3, 4, 5 });
    }

    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes result = generator.getCharacterEscapes();
        assertNull(result);
    }

    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
        JsonGenerator result = generator.setRootValueSeparator(sep);
        assertEquals(generator, result);
        assertEquals(sep, generator._rootValueSeparator);
    }

    @Test
    public void testVersion() {
        Version result = generator.version();
        assertEquals(VersionUtil.versionFor(generator.getClass()), result);
    }

    @Test
    public void testWriteStringField() throws IOException {
        String fieldName = "testField";
        String value = "testValue";
        generator.writeStringField(fieldName, value);
    }
}