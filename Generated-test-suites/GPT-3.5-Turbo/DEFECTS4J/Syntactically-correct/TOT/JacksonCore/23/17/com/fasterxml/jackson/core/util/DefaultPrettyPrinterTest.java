
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Before;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setup() {
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeRootValueSeparator(g);

        assertEquals(" ", g.getOutput());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeStartObject(g);

        assertEquals("{", g.getOutput());
        assertEquals(1, prettyPrinter._nesting);
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.beforeObjectEntries(g);

        assertEquals("", g.getOutput());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeObjectFieldValueSeparator(g);

        assertEquals(": ", g.getOutput());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeObjectEntrySeparator(g);

        assertEquals(", ", g.getOutput());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeEndObject(g, 0);

        assertEquals(" }", g.getOutput());
        assertEquals(0, prettyPrinter._nesting);
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeStartArray(g);

        assertEquals("[", g.getOutput());
        assertEquals(1, prettyPrinter._nesting);
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.beforeArrayValues(g);

        assertEquals("", g.getOutput());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeArrayValueSeparator(g);

        assertEquals(", ", g.getOutput());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator g = new StubJsonGenerator();
        prettyPrinter.writeEndArray(g, 0);

        assertEquals(" ]", g.getOutput());
        assertEquals(0, prettyPrinter._nesting);
    }

    // Custom Stub class to verify output
    static class StubJsonGenerator extends JsonGenerator {
        private StringBuilder output = new StringBuilder();

        @Override
        public void writeStartObject() throws IOException {
            output.append("{");
        }

        @Override
        public void writeEndObject() throws IOException {
            output.append("}");
        }

        @Override
        public void writeStartArray() throws IOException {
            output.append("[");
        }

        @Override
        public void writeEndArray() throws IOException {
            output.append("]");
        }

        @Override
        public void writeFieldName(String name) throws IOException {
            output.append(name);
        }

        @Override
        public void writeFieldName(SerializedString name) throws IOException {
            output.append(name.getValue());
        }

        @Override
        public void writeString(String text) throws IOException {
            output.append(text);
        }

        @Override
        public void writeString(SerializedString text) throws IOException {
            output.append(text.getValue());
        }

        @Override
        public void writeRaw(String text) throws IOException {
            output.append(text);
        }

        @Override
        public void writeRaw(char[] text, int offset, int len) throws IOException {
            output.append(text, offset, len);
        }

        @Override
        public void writeRaw(String text, int offset, int len) throws IOException {
            output.append(text, offset, len);
        }

        @Override
        public void flush() throws IOException {
            // no op
        }

        @Override
        public void close() throws IOException {
            // no op
        }

        public String getOutput() {
            return output.toString();
        }
    }
}
