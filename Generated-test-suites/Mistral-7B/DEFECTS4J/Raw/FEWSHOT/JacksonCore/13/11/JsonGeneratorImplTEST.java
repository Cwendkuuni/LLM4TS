package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

/**
 * Test cases for {@link JsonGeneratorImpl} class.
 */
public class JsonGeneratorImplTest {

    @Test
    public void testVersion() {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(null, 0, null);
        Version version = jsonGenerator.version();
        assertTrue(version.major > 0);
        assertTrue(version.minor > 0);
        assertTrue(version.build > 0);
    }

    @Test
    public void testSetCharacterEscapesNull() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.setCharacterEscapes(null);
        assertNull(jsonGenerator.getCharacterEscapes());
        assertArrayEquals(JsonGeneratorImpl.sOutputEscapes, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetCharacterEscapes() {
        IOContext context = new IOContext();
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[] { 0x10, 0x20, 0x30 };
            }
        };

        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.setCharacterEscapes(characterEscapes);
        CharacterEscapes escapedCharacterEscapes = jsonGenerator.getCharacterEscapes();
        assertNotNull(escapedCharacterEscapes);
        assertArrayEquals(new int[] { 0x10, 0x20, 0x30 }, escapedCharacterEscapes.getEscapeCodesForAscii());
        assertArrayEquals(new int[] { 0x10, 0x20, 0x30 }, jsonGenerator._outputEscapes);
    }

    @Test
    public void testSetRootValueSeparator() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        SerializableString rootValueSeparator = new SerializableString("rootValueSeparator");
        jsonGenerator.setRootValueSeparator(rootValueSeparator);
        SerializableString actualRootValueSeparator = jsonGenerator.getRootValueSeparator();
        assertEquals(rootValueSeparator, actualRootValueSeparator);
    }

    @Test
    public void testDefaultRootValueSeparator() {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        SerializableString actualRootValueSeparator = jsonGenerator.getRootValueSeparator();
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, actualRootValueSeparator);
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteStringFieldWithNullFieldName() throws IOException {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        String nullFieldName = null;
        jsonGenerator.writeStringField(nullFieldName, "value");
    }

    @Test
    public void testWriteStringField() throws IOException {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        String fieldName = "fieldName";
        String value = "value";
        jsonGenerator.writeStringField(fieldName, value);
        assertFalse(jsonGenerator.getOutputBuffer().isEmpty());
        assertEquals("\"fieldName\":\"value\"", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testHighestNonEscapedChar() throws IOException {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.setHighestNonEscapedChar(0x10);
        assertEquals(0x10, jsonGenerator.getHighestEscapedChar());
    }

    @Test
    public void testDisableQuoteFieldNames() throws IOException {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeStringField("fieldName", "value");
        assertEquals("fieldName:value", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator.disableQuoteFieldNames();
        jsonGenerator.writeStringField("fieldName", "value");
        assertEquals("fieldName value", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDisableQuoteFieldNamesWithQuotesInFieldName() throws IOException {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeStringField("fieldName\"", "value");
        assertEquals("\"fieldName\":\"value\"", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator.disableQuoteFieldNames();
        jsonGenerator.writeStringField("fieldName\"", "value");
        assertEquals("fieldName\" value", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDisableQuoteFieldNamesWithEmptyFieldName() throws IOException {
        IOContext context = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(context, 0, null);
        jsonGenerator.enable(Feature.QUOTE_FIELD_NAMES);
        jsonGenerator.writeStringField("", "value");
        assertEquals("\"\":\"value\"", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator.disableQuoteFieldNames();
        jsonGenerator.writeStringField("", "value");
        assertEquals(" value", jsonGenerator.getOutputBuffer().toString());
    }
}