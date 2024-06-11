package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getRootSeparator());
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString("root"));
        assertEquals("root", prettyPrinterWithRootSeparator.getRootSeparator().getValue());
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = prettyPrinter.withRootSeparator(new SerializedString("root"));
        assertEquals("root", prettyPrinterWithRootSeparator.getRootSeparator().getValue());
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertNull(jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString("root"));
        prettyPrinterWithRootSeparator.writeRootValueSeparator(jsonGenerator);
        assertEquals("root", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = new DefaultPrettyPrinter().withObjectIndenter(new FixedSpaceIndenter());
        prettyPrinterWithObjectIndenter.writeStartObject(jsonGenerator);
        assertEquals("{\n", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = new DefaultPrettyPrinter().withObjectIndenter(new FixedSpaceIndenter());
        prettyPrinterWithObjectIndenter.beforeObjectEntries(jsonGenerator);
        assertEquals("\n", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(":", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        JsonGenerator jsonGeneratorWithSpaces = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinterWithSpacesInObjectEntries.writeObjectFieldValueSeparator(jsonGeneratorWithSpaces);
        assertEquals("  :", jsonGeneratorWithSpaces.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = new DefaultPrettyPrinter().withObjectIndenter(new FixedSpaceIndenter());
        prettyPrinterWithObjectIndenter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("\n", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeEndObject(jsonGenerator, 0);
        assertEquals("}", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = new DefaultPrettyPrinter().withObjectIndenter(new FixedSpaceIndenter());
        JsonGenerator jsonGeneratorWithIndentation = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinterWithObjectIndenter.writeStartObject(jsonGeneratorWithIndentation);
        prettyPrinterWithObjectIndenter.writeObjectEntrySeparator(jsonGeneratorWithIndentation);
        prettyPrinterWithObjectIndenter.writeEndObject(jsonGeneratorWithIndentation, 1);
        assertEquals("{\n}", jsonGeneratorWithIndentation.getOutputBuffer().toString());
        assertEquals("}", jsonGeneratorWithIndentation.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = new DefaultPrettyPrinter().withArrayIndenter(new FixedSpaceIndenter());
        prettyPrinterWithArrayIndenter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputBuffer().toString());
        assertEquals("\n", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.beforeArrayValues(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = new DefaultPrettyPrinter().withArrayIndenter(new FixedSpaceIndenter());
        prettyPrinterWithArrayIndenter.beforeArrayValues(jsonGenerator);
        assertEquals("\n", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        JsonGenerator jsonGeneratorWithSpaces = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinterWithSpacesInObjectEntries.writeArrayValueSeparator(jsonGeneratorWithSpaces);
        assertEquals("  ,", jsonGeneratorWithSpaces.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinter.writeEndArray(jsonGenerator, 0);
        assertEquals("]", jsonGenerator.getOutputBuffer().toString());

        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = new DefaultPrettyPrinter().withArrayIndenter(new FixedSpaceIndenter());
        JsonGenerator jsonGeneratorWithIndentation = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        prettyPrinterWithArrayIndenter.writeStartArray(jsonGeneratorWithIndentation);
        prettyPrinterWithArrayIndenter.beforeArrayValues(jsonGeneratorWithIndentation);
        prettyPrinterWithArrayIndenter.writeEndArray(jsonGeneratorWithIndentation, 1);
        assertEquals("[", jsonGeneratorWithIndentation.getOutputBuffer().toString());
        assertEquals("\n", jsonGeneratorWithIndentation.getOutputBuffer().toString());
        assertEquals("]", jsonGeneratorWithIndentation.getOutputBuffer().toString());
    }
}