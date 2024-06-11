package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonGenerator jsonGeneratorMock;

    @Before
    public void setUp() {
        jsonGeneratorMock = mock(JsonGenerator.class);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testDefaultConstructor() throws IOException, JsonProcessingException {
        prettyPrinter.writeRootValueSeparator(jsonGeneratorMock);
        assertEquals("", jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test
    public void testConstructorWithNullRootSeparator() throws IOException, JsonProcessingException {
        prettyPrinter = new DefaultPrettyPrinter(null);
        prettyPrinter.writeRootValueSeparator(jsonGeneratorMock);
        assertEquals("", jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test
    public void testConstructorWithRootSeparator() throws IOException, JsonProcessingException {
        String rootSeparator = "RootSeparator";
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinter.writeRootValueSeparator(jsonGeneratorMock);
        assertEquals(rootSeparator, jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartObject() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        assertEquals("{\n", jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndObject() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        prettyPrinter.writeObjectEntrySeparator(jsonGeneratorMock);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGeneratorMock);
        prettyPrinter.writeObjectFieldValue("fieldName", "fieldValue");
        prettyPrinter.writeObjectEntrySeparator(jsonGeneratorMock);
        prettyPrinter.writeEndObject(jsonGeneratorMock, 1);
        assertEquals("{\n  \"fieldName\": \"fieldValue\"\n}", jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartArray() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartArray(jsonGeneratorMock);
        assertEquals("[\n", jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndArray() throws IOException, JsonProcessingException {
        prettyPrinter.writeStartArray(jsonGeneratorMock);
        prettyPrinter.writeArrayValueSeparator(jsonGeneratorMock);
        prettyPrinter.writeArrayValue("value1");
        prettyPrinter.writeArrayValueSeparator(jsonGeneratorMock);
        prettyPrinter.writeEndArray(jsonGeneratorMock, 1);
        assertEquals("[\n  \"value1\"\n]", jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(null);
    }

    @Test
    public void testWriteObjectFieldValueSeparatorWithSpaces() throws IOException, JsonProcessingException {
        prettyPrinter.withRootSeparator("RootSeparator");
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGeneratorMock);
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValue("fieldValue");
        prettyPrinter.writeEndObject(jsonGeneratorMock, 1);
        assertEquals("{\n  \"fieldName\": \"fieldValue\"\n}", jsonGeneratorMock.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparatorWithoutSpaces() throws IOException, JsonProcessingException {
        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withRootSeparator("RootSeparator");
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGeneratorMock);
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValue("fieldValue");
        prettyPrinter.writeEndObject(jsonGeneratorMock, 1);
        assertEquals("{\n  \"fieldName\": \"fieldValue\"\n}", jsonGeneratorMock.getOutputBuffer().toString());
    }
}