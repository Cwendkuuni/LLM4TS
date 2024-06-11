package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeRootValueSeparator(g);
        // Assert the result
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeStartObject(g);
        // Assert the result
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.beforeObjectEntries(g);
        // Assert the result
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeObjectFieldValueSeparator(g);
        // Assert the result
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeObjectEntrySeparator(g);
        // Assert the result
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeEndObject(g, 1);
        // Assert the result
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeStartArray(g);
        // Assert the result
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.beforeArrayValues(g);
        // Assert the result
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeArrayValueSeparator(g);
        // Assert the result
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeEndArray(g, 1);
        // Assert the result
    }
}