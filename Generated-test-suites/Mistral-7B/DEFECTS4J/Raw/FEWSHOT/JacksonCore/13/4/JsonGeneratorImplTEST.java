package com.fasterxml.jackson.core.json;

import static com.fasterxml.jackson.core.api.TestConstants.COMMON_FIELD_NAME;
import static com.fasterxml.jackson.core.api.TestConstants.COMMON_STRING_VALUE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    // Test constructor

    @Test
    public void testJsonGeneratorImplConstructor() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectCodec() {});
        assertEquals(ioContext, jsonGeneratorImpl._ioContext);
        assertEquals(Feature.AUTO_CLOSE_SOURCE.getMask(), jsonGeneratorImpl._features);
        assertNull(jsonGeneratorImpl._characterEscapes);
        assertNull(jsonGeneratorImpl._outputEscapes);
        assertNull(jsonGeneratorImpl._rootValueSeparator);
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    // Test setHighestNonEscapedChar()

    @Test
    public void testSetHighestNonEscapedChar() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectCodec() {});

        jsonGeneratorImpl.setHighestNonEscapedChar(123);
        assertEquals(123, jsonGeneratorImpl.getHighestEscapedChar());

        jsonGeneratorImpl.setHighestNonEscapedChar(0);
        assertEquals(0, jsonGeneratorImpl.getHighestEscapedChar());
    }

    // Test _checkStdFeatureChanges()

    @Test
    public void testCheckStdFeatureChanges() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(null, 0, null);

        int newFeatureFlags = JsonGenerator.Feature.INDENT_OUTPUT.getMask();
        jsonGeneratorImpl._checkStdFeatureChanges(newFeatureFlags, JsonGenerator.Feature.INDENT_OUTPUT.getMask());
        assertTrue(jsonGeneratorImpl._cfgUnqNames);

        jsonGeneratorImpl._checkStdFeatureChanges(newFeatureFlags, JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask());
        assertFalse(jsonGeneratorImpl._cfgUnqNames);
    }

    // Test setCharacterEscapes()

    @Test
    public void testSetCharacterEscapes() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectCodec() {});

        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 512, 513 };
            }
        };
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        assertSame(characterEscapes, jsonGeneratorImpl.getCharacterEscapes());
        assertArrayEquals(new int[] { 512, 513 }, jsonGeneratorImpl._outputEscapes);

        CharacterEscapes nullCharacterEscapes = null;
        jsonGeneratorImpl.setCharacterEscapes(nullCharacterEscapes);
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGeneratorImpl._outputEscapes);
    }

    // Test setRootValueSeparator()

    @Test
    public void testSetRootValueSeparator() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectCodec() {});

        SerializableString rootValueSeparator = new SerializableString(" ");
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        assertEquals(rootValueSeparator, jsonGeneratorImpl._rootValueSeparator);

        jsonGeneratorImpl.setRootValueSeparator(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGeneratorImpl._rootValueSeparator);
    }

    // Test writeStringField()

    @Test
    public void testWriteStringField() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, Feature.AUTO_CLOSE_SOURCE.getMask(), new ObjectCodec() {});
        writeTestWriteStringField(jsonGeneratorImpl, COMMON_FIELD_NAME, COMMON_STRING_VALUE);
    }

    private void writeTestWriteStringField(JsonGeneratorImpl jsonGeneratorImpl, String fieldName, String value)
            throws IOException {
        jsonGeneratorImpl.writeStartObject();
        jsonGeneratorImpl.writeFieldName(fieldName);
        jsonGeneratorImpl.writeString(value);
        jsonGeneratorImpl.writeEndObject();

        String json = jsonGeneratorImpl.getOutputContent();
        String expectedJson = String.format("\"%s\":%s", fieldName, value);
        assertEquals(expectedJson, json);
    }
}