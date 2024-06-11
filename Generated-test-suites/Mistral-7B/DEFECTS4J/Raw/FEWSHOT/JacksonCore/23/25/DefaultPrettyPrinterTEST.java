package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = "{\n" +
                "}\n" +
                "[]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString(":"));
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = ":\n{\n}\n[]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithArrayIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(new FixedSpaceIndenter());
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = " {\n}\n [\n]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithObjectIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentObjectsWith(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = ":\n{\n  \n}\n[\n  \n]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.withSpacesInObjectEntries();
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = " {\n  \n}\n[\n  \n]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorAndSpacesInObjectEntries() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString(":"));
        pp.withSpacesInObjectEntries();
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = ":\n{\n  \n}\n[\n  \n]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomObjectIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        String indent = "  ";
        PrettyPrinter.Indenter customIndenter = new PrettyPrinter.Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                for (int i = 0; i < level * 2; i += 2) {
                    g.writeRaw(indent);
                }
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        pp.indentObjectsWith(customIndenter);
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = ":\n{\n  \n  \n}\n[\n  \n  \n]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomArrayIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        String indent = "  ";
        PrettyPrinter.Indenter customIndenter = new PrettyPrinter.Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                for (int i = 0; i < level * 2; i += 2) {
                    g.writeRaw(indent);
                }
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        pp.indentArraysWith(customIndenter);
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = " {\n  \n}\n[\n  \n  \n]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomObjectIndenterAndRootSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString(":"));
        String indent = "  ";
        PrettyPrinter.Indenter customIndenter = new PrettyPrinter.Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                for (int i = 0; i < level * 2; i += 2) {
                    g.writeRaw(indent);
                }
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        pp.indentObjectsWith(customIndenter);
        JsonGenerator gen = new MockJsonGenerator();
        pp.writeRootValueSeparator(gen);
        pp.writeStartObject(gen);
        pp.beforeObjectEntries(gen);
        pp.writeObjectFieldValueSeparator(gen);
        pp.writeObjectEntrySeparator(gen);
        pp.writeEndObject(gen, 0);
        pp.writeStartArray(gen);
        pp.beforeArrayValues(gen);
        pp.writeArrayValueSeparator(gen);
        pp.writeEndArray(gen, 0);

        String expected = ":\n{\n  \n  \n}\n[\n  \n  \n]\n";

        String actual = gen.getOutputBuffer().toString();
        assertEquals(expected, actual);
    }
}

class MockJsonGenerator implements JsonGenerator {
    private final StringBuilder outputBuffer = new StringBuilder();

    @Override
    public void close() throws IOException {
        // Do nothing
    }

    @Override
    public void flush() throws IOException {
        // Do nothing
    }

    @Override
    public void writeRaw(String str) throws IOException {
        outputBuffer.append(str);
    }

    @Override
    public void writeRaw(byte[] bytes, int offset, int length) throws IOException {
        // Do nothing
    }

    @Override
    public void writeString(String str) throws IOException {
        // Do nothing
    }

    @Override
    public void writeNumber(Number number) throws IOException {
        // Do nothing
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        // Do nothing
    }

    @Override
    public void writeNull() throws IOException {
        // Do nothing
    }

    @Override
    public void writeStartArray() throws IOException {
        // Do nothing
    }

    @Override
    public void writeEndArray() throws IOException {
        // Do nothing
    }

    @Override
    public void writeStartObject() throws IOException {
        // Do nothing
    }

    @Override
    public void writeEndObject() throws IOException {
        // Do nothing
    }

    @Override
    public void writeFieldName(String fieldName) throws IOException {
        // Do nothing
    }

    @Override
    public void writeObjectFieldValueSeparator() throws IOException {
        // Do nothing
    }

    @Override
    public void writeArrayValueSeparator() throws IOException {
        // Do nothing
    }

    public String getOutputBuffer() {
        return outputBuffer.toString();
    }
}