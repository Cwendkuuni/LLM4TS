package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setup() {
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeRootValueSeparator(g);
        // TODO: Assert result
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeStartObject(g);
        // TODO: Assert result
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.beforeObjectEntries(g);
        // TODO: Assert result
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeObjectFieldValueSeparator(g);
        // TODO: Assert result
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeObjectEntrySeparator(g);
        // TODO: Assert result
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator g = new JsonGenerator();
        int nrOfEntries = 0;
        prettyPrinter.writeEndObject(g, nrOfEntries);
        // TODO: Assert result
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeStartArray(g);
        // TODO: Assert result
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.beforeArrayValues(g);
        // TODO: Assert result
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator g = new JsonGenerator();
        prettyPrinter.writeArrayValueSeparator(g);
        // TODO: Assert result
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator g = new JsonGenerator();
        int nrOfValues = 0;
        prettyPrinter.writeEndArray(g, nrOfValues);
        // TODO: Assert result
    }
}