package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerator;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeRootValueSeparator(generator);
        assertEquals(" ", generator.getContent());
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeStartObject(generator);
        assertEquals("{", generator.getContent());
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.beforeObjectEntries(generator);
        assertEquals("", generator.getContent());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeObjectFieldValueSeparator(generator);
        assertEquals(" :", generator.getContent());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeObjectEntrySeparator(generator);
        assertEquals(",", generator.getContent());
    }

    @Test
    public void testWriteEndObjectWithValidNrOfEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeEndObject(generator, 1);
        assertEquals(" }", generator.getContent());
    }

    @Test
    public void testWriteEndObjectWithNoEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeEndObject(generator, 0);
        assertEquals(" }", generator.getContent());
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeStartArray(generator);
        assertEquals("[", generator.getContent());
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.beforeArrayValues(generator);
        assertEquals("", generator.getContent());
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeArrayValueSeparator(generator);
        assertEquals(",", generator.getContent());
    }

    @Test
    public void testWriteEndArrayWithValidNrOfValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeEndArray(generator, 1);
        assertEquals(" ]", generator.getContent());
    }

    @Test
    public void testWriteEndArrayWithNoValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockGenerator generator = new MockGenerator();
        printer.writeEndArray(generator, 0);
        assertEquals(" ]", generator.getContent());
    }

    private static class MockGenerator extends JsonGenerator {
        
        private StringBuilder content = new StringBuilder();

        @Override
        public void writeRaw(String text) {
            content.append(text);
        }

        @Override
        public void writeRaw(char c) {
            content.append(c);
        }

        @Override
        public void flush() {
            // Do nothing
        }

        @Override
        public void close() {
            // Do nothing
        }

        public String getContent() {
            return content.toString();
        }
    }
}