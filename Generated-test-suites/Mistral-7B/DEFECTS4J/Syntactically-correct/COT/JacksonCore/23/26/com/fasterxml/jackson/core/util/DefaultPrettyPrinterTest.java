package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JUnit test file for DefaultPrettyPrinter class.
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        assertNotNull(prettyPrinter);
        assertEquals("--", prettyPrinter.getRootSeparator().getValueAsString());
    }

    @Test
    public void testWriteRootValueSeparator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(generator);
        String rootSeparator = prettyPrinter.getRootSeparator() != null ? prettyPrinter.getRootSeparator().getValueAsString() : "";
        byte[] bytes = generator.getOutputBuffer().toByteArray();
        String result = new String(bytes, 0, bytes.length);
        assertEquals(rootSeparator, result.substring(0, rootSeparator.length()));
    }

    @Test
    public void testWriteStartObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartObject(generator);
        String result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals("{\n", result);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.setSpacesInObjectEntries(true);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        String result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals("  :\n", result.substring(result.lastIndexOf(":") - 4, result.lastIndexOf(":") + 3));

        prettyPrinter.setSpacesInObjectEntries(false);
        generator.clear();
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals(" :\n", result.substring(result.lastIndexOf(":") - 2, result.lastIndexOf(":")));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeObjectEntrySeparator(generator);
        String result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals(",\n", result);
    }

    @Test
    public void testWriteEndObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldName("name");
        prettyPrinter.writeObjectValue("John Doe");
        prettyPrinter.writeEndObject(generator, 1);
        String result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals("{\n  \"name\": \"John Doe\"\n}", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteEndObjectNegativeNestingLevel() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeEndObject(generator, -1);
    }

    @Test
    public void testWriteStartArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartArray(generator);
        String result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals("[", result);
    }

    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeArrayValueSeparator(generator);
        String result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals(",\n", result);
    }

    @Test
    public void testWriteEndArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeArrayValue("value1");
        prettyPrinter.writeEndArray(generator, 1);
        String result = new String(generator.getOutputBuffer().toByteArray(), 0, generator.getOutputBuffer().size());
        assertEquals("[\"value1\"]\n", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteEndArrayNegativeNestingLevel() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeEndArray(generator, -1);
    }
}