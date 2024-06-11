package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.writeRootValueSeparator(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.writeStartObject(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.beforeObjectEntries(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.writeObjectFieldValueSeparator(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.writeObjectEntrySeparator(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        int nrOfEntries = 1;
        try {
            prettyPrinter.writeEndObject(g, nrOfEntries);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.writeStartArray(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.beforeArrayValues(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        try {
            prettyPrinter.writeArrayValueSeparator(g);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonGenerator();
        int nrOfValues = 1;
        try {
            prettyPrinter.writeEndArray(g, nrOfValues);
            assertEquals(true, true);
        } catch(IOException e) {
            fail(e.getMessage());
        }
    }

}