package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    // Test constructor
    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    // Test constructor with root separator
    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(":"));
        assertNotNull(prettyPrinter);
        assertEquals(":", prettyPrinter.getRootSeparator());
    }

    // Test setRootSeparator method
    @Test
    public void testSetRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withRootSeparator(new SerializedString(":"));
        assertEquals(":", prettyPrinter.getRootSeparator());
    }

    // Test writeRootValueSeparator method
    @Test
    public void testWriteRootValueSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        byte[] result = jsonGenerator.getOutputContent();
        String jsonString = new String(result);
        assertTrue(jsonString.contains(":"));
    }

    // Test writeStartObject method
    @Test
    public void testWriteStartObject() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        prettyPrinter.writeStartObject(jsonGenerator);
        byte[] result = jsonGenerator.getOutputContent();
        String jsonString = new String(result);
        assertTrue(jsonString.contains("{"));
    }

    // Test writeObjectFieldValueSeparator method
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        byte[] result = jsonGenerator.getOutputContent();
        String jsonString = new String(result);
        assertTrue(jsonString.contains(":"));
    }

    // Test writeObjectEntrySeparator method
    @Test
    public void testWriteObjectEntrySeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        byte[] result = jsonGenerator.getOutputContent();
        String jsonString = new String(result);
        assertTrue(jsonString.contains(","));
    }

    // Test writeEndObject method
    @Test
    public void testWriteEndObject() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldName("test");
        prettyPrinter.writeNull();
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        byte[] result = jsonGenerator.getOutputContent();
        String jsonString = new String(result);
        assertTrue(jsonString.contains("}"));
    }

    // Test indentArraysWith method
    @Test
    public void testIndentArraysWith() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("--");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        prettyPrinter.indentArraysWith(indenter);
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.writeNull();
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        byte[] result = jsonGenerator.getOutputContent();
        String jsonString = new String(result);
        assertTrue(jsonString.contains("--"));
        assertTrue(jsonString.contains("--"));
        assertTrue(jsonString.contains("]"));
    }

    // Test withSpacesInObjectEntries method
    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter withSpaces = prettyPrinter.withSpacesInObjectEntries();
        assertNotSame(prettyPrinter, withSpaces);
        assertTrue(withSpaces.isSpacesInObjectEntries());
        DefaultPrettyPrinter withoutSpaces = prettyPrinter.withoutSpacesInObjectEntries();
        assertNotSame(prettyPrinter, withoutSpaces);
        assertFalse(withoutSpaces.isSpacesInObjectEntries());
    }
}