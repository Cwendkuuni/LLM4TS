package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        beforeObjectEntries(jsonGenerator);
        // Test empty object
        prettyPrinter.writeEndObject(jsonGenerator, 0);

        assertEquals("{\n}", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        beforeObjectEntries(jsonGenerator);
        // Test object with field
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValue("fieldValue");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        assertEquals("{\n \"fieldName\": \"fieldValue\"\n}", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test empty array
        prettyPrinter.writeEndArray(jsonGenerator, 0);

        assertEquals("[\n]", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test array with one value
        prettyPrinter.writeValue("value");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);

        assertEquals("[\n \"value\"\n]", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test array with multiple values
        prettyPrinter.writeValue("value1");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeValue("value2");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeValue("value3");
        prettyPrinter.writeEndArray(jsonGenerator, 3);

        assertEquals("[\n \"value1\"\n \"value2\"\n \"value3\"\n]", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("separator");
        JsonGenerator jsonGenerator = new TestJsonGenerator();

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals("separator", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        beforeObjectEntries(jsonGenerator);
        // Test empty object
        prettyPrinter.writeEndObject(jsonGenerator, 0);

        assertEquals("separator{\n}", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        beforeObjectEntries(jsonGenerator);
        // Test object with field
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValue("fieldValue");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        assertEquals("separator{\n \"fieldName\": \"fieldValue\"\n}", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test empty array
        prettyPrinter.writeEndArray(jsonGenerator, 0);

        assertEquals("separator[\n]", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test array with one value
        prettyPrinter.writeValue("value");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);

        assertEquals("separator[\n \"value\"\n]", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test array with multiple values
        prettyPrinter.writeValue("value1");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeValue("value2");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeValue("value3");
        prettyPrinter.writeEndArray(jsonGenerator, 3);

        assertEquals("separator[\n \"value1\"\n \"value2\"\n \"value3\"\n]", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithIndenters() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();

        prettyPrinter.indentObjectsWith(new SpaceIndenter(4));
        prettyPrinter.indentArraysWith(new SpaceIndenter(2));

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        beforeObjectEntries(jsonGenerator);
        // Test object with field
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValue("fieldValue");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        assertEquals("{\n\t\"fieldName\": \"fieldValue\"\n}", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test array with one value
        prettyPrinter.writeValue("value");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);

        assertEquals("[\n\t\"value\"\n]", jsonGenerator.getOutputContent().toString());

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartArray(jsonGenerator);
        beforeArrayValues(jsonGenerator);
        // Test array with multiple values
        prettyPrinter.writeValue("value1");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeValue("value2");
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeValue("value3");
        prettyPrinter.writeEndArray(jsonGenerator, 3);

        assertEquals("[\n\t\"value1\"\n\t\"value2\"\n\t\"value3\"\n]", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.withSpacesInObjectEntries();

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        beforeObjectEntries(jsonGenerator);
        // Test object with field
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValue("fieldValue");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        assertEquals("{\n \"fieldName\": \"fieldValue\"\n}", jsonGenerator.getOutputContent().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithoutSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.withoutSpacesInObjectEntries();

        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        prettyPrinter.writeStartObject(jsonGenerator);
        beforeObjectEntries(jsonGenerator);
        // Test object with field
        prettyPrinter.writeObjectFieldName("fieldName");
        prettyPrinter.writeObjectFieldValue("fieldValue");
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        assertEquals("{\"fieldName\": \"fieldValue\"}", jsonGenerator.getOutputContent().toString());
    }

    private void beforeObjectEntries(JsonGenerator jsonGenerator) throws IOException {
        prettyPrinter.beforeObjectEntries(jsonGenerator);
    }

    private void beforeArrayValues(JsonGenerator jsonGenerator) throws IOException {
        prettyPrinter.beforeArrayValues(jsonGenerator);
    }

    private static class TestJsonGenerator extends JsonGenerator {
        private StringBuffer content = new StringBuffer();

        @Override
        public void writeRaw(String text) throws IOException {
            content.append(text);
        }

        @Override
        public String getOutputContent() {
            return content.toString();
        }
    }

    private static class SpaceIndenter implements Indenter {
        private int spaces;

        public SpaceIndenter(int spaces) {
            this.spaces = spaces;
        }

        @Override
        public void writeIndentation(JsonGenerator g, int level) throws IOException {
            for (int i = 0; i < (level * spaces); i++) {
                g.writeRaw(' ');
            }
        }

        @Override
        public boolean isInline() {
            return false;
        }
    }
}