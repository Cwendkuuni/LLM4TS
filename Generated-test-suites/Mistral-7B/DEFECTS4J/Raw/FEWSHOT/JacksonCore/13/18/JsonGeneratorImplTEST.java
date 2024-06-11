package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;
import org.junit.Test;

public class JsonGeneratorImplTest {

    @Test
    public void testConfigurableOutputEscapes() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        CharacterEscapes standardEscapes = jsonGenerator.getCharacterEscapes();
        int[] standardOutputEscapes = standardEscapes.getEscapeCodesForAscii();

        jsonGenerator.setCharacterEscapes(new CharacterEscapes.None());
        int[] newOutputEscapes = jsonGenerator.getOutputEscapes();
        assertArrayEquals(new int[0], newOutputEscapes);

        jsonGenerator.setCharacterEscapes(standardEscapes);
        newOutputEscapes = jsonGenerator.getOutputEscapes();
        assertArrayEquals(standardOutputEscapes, newOutputEscapes);
    }

    @Test
    public void testQuoteFieldNames() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);

        jsonGenerator.setQuoteFieldNames(false);

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("foo");
            jsonGenerator.writeValue("bar");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"foo\":\"bar\"}", json);
        }

        jsonGenerator.setQuoteFieldNames(true);

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("foo");
            jsonGenerator.writeValue("bar");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"foo\":\"bar\"}", json);
        }

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("foo");
            jsonGenerator.writeValue("bar");
            jsonGenerator.writeFieldName("baz");
            jsonGenerator.writeValue("qux");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"foo\":\"bar\",\"baz\":\"qux\"}", json);
        }

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.setQuoteFieldNames(false);
            jsonGenerator.writeFieldName("foo");
            jsonGenerator.writeValue("bar");
            jsonGenerator.setQuoteFieldNames(true);
            jsonGenerator.writeFieldName("baz");
            jsonGenerator.writeValue("qux");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"foo\":\"bar\",\"baz\":\"qux\"}", json);
        }
    }

    @Test
    public void testHighestNonEscapedChar() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGenerator.setHighestNonEscapedChar(127);

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName(" foo ");
            jsonGenerator.writeValue(" bar ");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"foo\":\"bar\"}", json);
        }

        jsonGenerator.setHighestNonEscapedChar(0);

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName(" foo ");
            jsonGenerator.writeValue(" bar ");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"foo\":\"foo \",\"bar\":\"bar \"}", json);
        }
    }

    @Test
    public void testGetCharacterEscapes() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);

        CharacterEscapes standardEscapes = jsonGenerator.getCharacterEscapes();
        int[] standardOutputEscapes = standardEscapes.getEscapeCodesForAscii();

        jsonGenerator.setCharacterEscapes(new CharacterEscapes.None());
        CharacterEscapes newEscapes = jsonGenerator.getCharacterEscapes();
        assertNull(newEscapes);

        jsonGenerator.setCharacterEscapes(standardEscapes);
        CharacterEscapes newEscapes2 = jsonGenerator.getCharacterEscapes();
        assertEquals(standardEscapes, newEscapes2);
    }

    @Test
    public void testSetRootValueSeparator() throws IOException {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);

        SerializableString defaultSeparator = jsonGenerator.getRootValueSeparator();
        SerializableString newSeparator = new SerializableString("newSeparator");

        jsonGenerator.setRootValueSeparator(newSeparator);

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeValue("rootValue");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"newSeparator\":\"rootValue\"}", json);
        }

        jsonGenerator.setRootValueSeparator(defaultSeparator);

        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeValue("rootValue");
            jsonGenerator.writeEndObject();
            String json = writer.getJsonContent();
            assertEquals("{\"rootValue\":\"rootValue\"}", json);
        }
    }
}