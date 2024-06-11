package com.fasterxml.jackson.core.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter printer;

    @Before
    public void setup() {
        printer = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeRootValueSeparator(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeStartObject(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.beforeObjectEntries(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeObjectFieldValueSeparator(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeObjectEntrySeparator(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeEndObject(generator, 0);
        // Verify the functionality of the method
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeStartArray(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.beforeArrayValues(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeArrayValueSeparator(generator);
        // Verify the functionality of the method
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator generator = createMock(JsonGenerator.class);
        printer.writeEndArray(generator, 0);
        // Verify the functionality of the method
    }
}