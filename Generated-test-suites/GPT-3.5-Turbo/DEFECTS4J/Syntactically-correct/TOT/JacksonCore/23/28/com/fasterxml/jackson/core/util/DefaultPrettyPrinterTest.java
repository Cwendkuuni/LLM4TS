
package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setup() {
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeRootValueSeparator(generator);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, generator.getOutput());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeStartObject(generator);
        assertEquals("{", generator.getOutput());
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.beforeObjectEntries(generator);
        assertEquals("", generator.getOutput());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        assertEquals(" ", generator.getOutput());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeObjectEntrySeparator(generator);
        assertEquals(DefaultPrettyPrinter.Separators.OBJECT_ENTRY_SEPARATOR, generator.getOutput());
    }

    @Test
    public void testWriteEndObjectWithEntries() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeEndObject(generator, 1);
        assertEquals("}", generator.getOutput());
    }

    @Test
    public void testWriteEndObjectWithoutEntries() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeEndObject(generator, 0);
        assertEquals(" }", generator.getOutput());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeStartArray(generator);
        assertEquals("[", generator.getOutput());
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.beforeArrayValues(generator);
        assertEquals("", generator.getOutput());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeArrayValueSeparator(generator);
        assertEquals(DefaultPrettyPrinter.Separators.ARRAY_VALUE_SEPARATOR, generator.getOutput());
    }

    @Test
    public void testWriteEndArrayWithValues() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeEndArray(generator, 1);
        assertEquals("]", generator.getOutput());
    }

    @Test
    public void testWriteEndArrayWithoutValues() throws IOException {
        TestJsonGenerator generator = new TestJsonGenerator();
        prettyPrinter.writeEndArray(generator, 0);
        assertEquals(" ]", generator.getOutput());
    }

    private static class TestJsonGenerator extends JsonGenerator {

        private String output;

        public TestJsonGenerator() {
            output = "";
        }

        public String getOutput() {
            return output;
        }

        @Override
        public void writeRaw(String value) {
            output += value;
        }

        // Other overridden methods are not necessary for the test cases
    }
}
