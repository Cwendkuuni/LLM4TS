package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("rootSeparator");
        assertNotNull(prettyPrinter);
        assertEquals("rootSeparator", prettyPrinter.getRootSeparator().asString());
    }

    @Test
    public void testObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter.getObjectIndenter());
        prettyPrinter.setObjectIndenter(new DefaultIndenter.SystemLineFeedInstance());
        assertEquals(System.lineSeparator(), prettyPrinter.getObjectIndenter().writeIndentation(null, 0).asString());
    }

    @Test
    public void testObjectIndenterIsInline() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter.getObjectIndenter());
        assertTrue(prettyPrinter.getObjectIndenter().isInline());
    }

    @Test
    public void testArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter.getArrayIndenter());
        prettyPrinter.setArrayIndenter(new FixedSpaceIndenter.Instance());
        assertEquals(" ", prettyPrinter.getArrayIndenter().writeIndentation(null, 0).asString());
    }

    @Test
    public void testArrayIndenterIsInline() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter.getArrayIndenter());
        assertTrue(prettyPrinter.getArrayIndenter().isInline());
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertNull(jsonGenerator.getCurrentOutputEntry());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        assertEquals(System.lineSeparator(), jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(System.lineSeparator(), jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(System.lineSeparator(), jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        assertEquals("}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.beforeArrayValues(jsonGenerator);
        assertEquals(System.lineSeparator(), jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        assertEquals("]", jsonGenerator.getOutputBuffer().toString());
    }
}