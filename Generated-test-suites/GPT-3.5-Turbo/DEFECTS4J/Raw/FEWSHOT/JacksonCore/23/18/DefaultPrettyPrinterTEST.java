
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeRootValueSeparator(generator);
        assertEquals(" ", sb.toString());
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeStartObject(generator);
        assertEquals("{", sb.toString());
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.beforeObjectEntries(generator);
        assertEquals("", sb.toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeObjectFieldValueSeparator(generator);
        assertEquals(" :", sb.toString());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeObjectEntrySeparator(generator);
        assertEquals(",", sb.toString());
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeEndObject(generator, 1);
        assertEquals(" }", sb.toString());
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeStartArray(generator);
        assertEquals("[", sb.toString());
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.beforeArrayValues(generator);
        assertEquals("", sb.toString());
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeArrayValueSeparator(generator);
        assertEquals(",", sb.toString());
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new JsonGenerator(sb);
        printer.writeEndArray(generator, 1);
        assertEquals(" ]", sb.toString());
    }

}
