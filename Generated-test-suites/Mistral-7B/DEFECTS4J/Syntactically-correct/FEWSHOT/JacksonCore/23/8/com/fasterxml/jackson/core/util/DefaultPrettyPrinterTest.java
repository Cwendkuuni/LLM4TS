package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    /**
     * Test constructor that specifies separator String to use between root values.
     */
    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("rootSeparator");
        assertNotNull(prettyPrinter);
        assertEquals("rootSeparator", prettyPrinter.getRootSeparator().asString());
    }

    /**
     * Test constructor that does not specify root separator.
     */
    @Test
    public void testDefaultPrettyPrinterWithoutRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getRootSeparator());
    }

    /**
     * Test setter for root separator.
     */
    @Test
    public void testSetRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.setRootSeparator(new SerializedString("newRootSeparator"));
        assertEquals("newRootSeparator", prettyPrinter.getRootSeparator().asString());
    }

    /**
     * Test getter for root separator.
     */
    @Test
    public void testGetRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        assertEquals("rootSeparator", prettyPrinter.getRootSeparator().asString());
    }

    /**
     * Test method writeRootValueSeparator.
     *
     * @throws IOException
     */
    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertNull(jsonGenerator.getCurrentOutputValue());

        prettyPrinter = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("rootSeparator", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method writeStartObject.
     *
     * @throws IOException
     */
    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method beforeObjectEntries.
     *
     * @throws IOException
     */
    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        assertEquals("", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method writeObjectFieldValueSeparator.
     *
     * @throws IOException
     */
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(":", jsonGenerator.getCurrentOutputValue().getValueAsString());

        prettyPrinter = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals("  :", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method writeObjectEntrySeparator.
     *
     * @throws IOException
     */
    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getCurrentOutputValue().getValueAsString());

        prettyPrinter = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(",\n  ", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method writeEndObject.
     *
     * @throws IOException
     */
    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeEndObject(jsonGenerator, 0);
        assertEquals("}", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method writeStartArray.
     *
     * @throws IOException
     */
    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method beforeArrayValues.
     *
     * @throws IOException
     */
    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        assertEquals("", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method writeArrayValueSeparator.
     *
     * @throws IOException
     */
    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getCurrentOutputValue().getValueAsString());

        prettyPrinter = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",\n  ", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method writeEndArray.
     *
     * @throws IOException
     */
    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), null);
        prettyPrinter.writeEndArray(jsonGenerator, 0);
        assertEquals("]", jsonGenerator.getCurrentOutputValue().getValueAsString());
    }

    /**
     * Test method withSeparators.
     */
    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators("objectFieldValueSeparator", "objectEntrySeparator", "arrayValueSeparator");
        prettyPrinter.withSeparators(separators);
        assertEquals(separators, prettyPrinter.getSeparators());
    }

    /**
     * Test method getSeparators.
     */
    @Test
    public void testGetSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new Separators("objectFieldValueSeparator", "objectEntrySeparator", "arrayValueSeparator"));
        assertEquals(new Separators("objectFieldValueSeparator", "objectEntrySeparator", "arrayValueSeparator"), prettyPrinter.getSeparators());
    }

    /**
     * Test method setSeparators.
     */
    @Test
    public void testSetSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators("objectFieldValueSeparator", "objectEntrySeparator", "arrayValueSeparator");
        prettyPrinter.setSeparators(separators);
        assertEquals(separators, prettyPrinter.getSeparators());
    }
}