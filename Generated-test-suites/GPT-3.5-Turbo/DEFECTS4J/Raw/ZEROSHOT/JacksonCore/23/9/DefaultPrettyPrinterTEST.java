package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.writeRootValueSeparator(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Root value separator is not written correctly", generator.getRawResult(), "");
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.writeStartObject(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Start object is not written correctly", generator.getRawResult(), "{");
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.beforeObjectEntries(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Before object entries is not written correctly", generator.getRawResult(), "");
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.writeObjectFieldValueSeparator(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Object field value separator is not written correctly", generator.getRawResult(), ":");
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.writeObjectEntrySeparator(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Object entry separator is not written correctly", generator.getRawResult(), ",");
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        int nrOfEntries = 2;
        try {
            printer.writeEndObject(generator, nrOfEntries);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("End object is not written correctly", generator.getRawResult(), "}");
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.writeStartArray(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Start array is not written correctly", generator.getRawResult(), "[");
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.beforeArrayValues(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Before array values is not written correctly", generator.getRawResult(), "");
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        try {
            printer.writeArrayValueSeparator(generator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("Array value separator is not written correctly", generator.getRawResult(), ",");
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        // Set up mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        int nrOfValues = 2;
        try {
            printer.writeEndArray(generator, nrOfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Assert the result
        assertEquals("End array is not written correctly", generator.getRawResult(), "]");
    }
}