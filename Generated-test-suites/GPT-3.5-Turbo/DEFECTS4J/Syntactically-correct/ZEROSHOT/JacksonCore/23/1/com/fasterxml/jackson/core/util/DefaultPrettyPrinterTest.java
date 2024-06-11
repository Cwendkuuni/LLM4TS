package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeRootValueSeparator(generator);
        // assertion here
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeStartObject(generator);
        // assertion here
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.beforeObjectEntries(generator);
        // assertion here
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        // assertion here
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeObjectEntrySeparator(generator);
        // assertion here
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeEndObject(generator, 0);
        // assertion here
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeStartArray(generator);
        // assertion here
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.beforeArrayValues(generator);
        // assertion here
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeArrayValueSeparator(generator);
        // assertion here
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = new JsonGenerator();
        prettyPrinter.writeEndArray(generator, 0);
        // assertion here
    }
}