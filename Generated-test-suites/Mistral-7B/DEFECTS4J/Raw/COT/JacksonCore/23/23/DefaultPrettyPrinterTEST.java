package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Tests for DefaultPrettyPrinter class.
 */
public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        prettyPrinter = new DefaultPrettyPrinter();
    }

    // Test constructor with no arguments
    @Test
    public void testDefaultConstructor() {
        assertNotNull(prettyPrinter);
    }

    // Test constructor with root separator
    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString("CustomRootSeparator"));
        assertNotNull(prettyPrinterWithRootSeparator);
        assertEquals("CustomRootSeparator", prettyPrinterWithRootSeparator.getRootSeparator().getValueAsString());
    }

    // Test writeRootValueSeparator method
    @Test
    public void testWriteRootValueSeparator() throws IOException, JsonProcessingException {
        String rootSeparator = "CustomRootSeparator";
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinterWithRootSeparator.writeRootValueSeparator(generator);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals(rootSeparator, json.substring(0, rootSeparator.length()));
    }

    // Test writeStartObject method
    @Test
    public void testWriteStartObject() throws IOException, JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartObject(generator);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals("{\n", json);
    }

    // Test writeEndObject method
    @Test
    public void testWriteEndObject() throws IOException, JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeEndObject(generator, 1);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals("{\n}\n", json);
    }

    // Test writeStartArray method
    @Test
    public void testWriteStartArray() throws IOException, JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartArray(generator);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals("[", json);
    }

    // Test writeEndArray method
    @Test
    public void testWriteEndArray() throws IOException, JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeEndArray(generator, 1);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals("[", json);
        assertEquals("]", json.substring(json.length() - 1));
    }

    // Test writeObjectFieldValueSeparator method
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException, JsonProcessingException {
        String objectFieldValueSeparator = "CustomSeparator";
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinterWithObjectFieldValueSeparator = new DefaultPrettyPrinter();
        prettyPrinterWithObjectFieldValueSeparator.withSeparators(new DefaultSeparators(objectFieldValueSeparator));
        prettyPrinterWithObjectFieldValueSeparator.writeObjectFieldValueSeparator(generator);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals(objectFieldValueSeparator, json.substring(json.length() - objectFieldValueSeparator.length()));
    }

    // Test writeArrayValueSeparator method
    @Test
    public void testWriteArrayValueSeparator() throws IOException, JsonProcessingException {
        String arrayValueSeparator = "CustomSeparator";
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinterWithArrayValueSeparator = new DefaultPrettyPrinter();
        prettyPrinterWithArrayValueSeparator.withSeparators(new DefaultSeparators(arrayValueSeparator));
        prettyPrinterWithArrayValueSeparator.writeArrayValueSeparator(generator);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals(arrayValueSeparator, json.substring(json.lastIndexOf(arrayValueSeparator), json.length()));
    }

    // Test writeObjectEntrySeparator method
    @Test
    public void testWriteObjectEntrySeparator() throws IOException, JsonProcessingException {
        String objectEntrySeparator = "CustomSeparator";
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DefaultPrettyPrinter prettyPrinterWithObjectEntrySeparator = new DefaultPrettyPrinter();
        prettyPrinterWithObjectEntrySeparator.withSeparators(new DefaultSeparators(objectEntrySeparator));
        prettyPrinterWithObjectEntrySeparator.writeObjectEntrySeparator(generator);
        generator.flush();
        String json = new String(generator.getOutputStream().toByteArray());
        assertEquals(objectEntrySeparator, json.substring(json.lastIndexOf(objectEntrySeparator), json.length()));
    }
}