package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;

/**
 * Test class for DefaultPrettyPrinter class
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("", result);
    }

    @Test
    public void testConstructorWithRootSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("test", result);
    }

    @Test
    public void testWithRootSeparator() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withRootSeparator(new SerializedString("test"));
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("test", result);
    }

    @Test
    public void testWriteStartObject() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeStartObject(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\n", result);
    }

    @Test
    public void testWriteEndObject() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeStartObject(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\n", result);
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("}\n", result);
    }

    @Test
    public void testWriteStartArray() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeStartArray(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("[", result);
    }

    @Test
    public void testWriteEndArray() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeStartArray(jsonGenerator);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("[", result);
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("]\n", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteStartObjectWithNullObjectIndenter() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.indentObjectsWith(null);
        try {
            prettyPrinter.writeStartObject(jsonGenerator);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteStartArrayWithNullArrayIndenter() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.indentArraysWith(null);
        try {
            prettyPrinter.writeStartArray(jsonGenerator);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWithSeparators() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = new DefaultPrettyPrinter.DefaultIndenter(System.lineSeparator(), 2) {
            @Override
            public String getObjectEntrySeparator() {
                return "\n";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return ": ";
            }

            @Override
            public String getArrayValueSeparator() {
                return ", ";
            }
        };
        prettyPrinter.withSeparators(objectIndenter);
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonGenerationSchema.getDefaultSchema());
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeObjectFieldName("name");
        prettyPrinter.writeObjectValue("John Doe");
        prettyPrinter.writeObjectFieldName("age");
        prettyPrinter.writeObjectValue(30);
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        String result = jsonGenerator.getOutputBuffer().toString();
        assertEquals("{\n" +
                     "  \"name\": \"John Doe\",\n" +
                     "  \"age\": 30\n" +
                     "}", result);
    }
}