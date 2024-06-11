package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("Testing"));
        assertNotNull(prettyPrinter);
        assertEquals("Testing", prettyPrinter.getRootSeparator().asString());
    }

    @Test
    public void testDefaultPrettyPrinterIndenterObjects() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter._objectIndenter);
    }

    @Test
    public void testDefaultPrettyPrinterIndenterArrays() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter._arrayIndenter);
    }

    @Test
    public void testDefaultPrettyPrinterWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertNull(jsonGenerator.getOutputBuffer().toString());
        if (prettyPrinter._rootSeparator != null) {
            jsonGenerator.writeRaw(prettyPrinter._rootSeparator.asString());
            assertEquals(prettyPrinter._rootSeparator.asString(), jsonGenerator.getOutputBuffer().toString());
        }
    }

    @Test
    public void testDefaultPrettyPrinterWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        assertEquals("}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        assertEquals("]", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());
        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        prettyPrinterWithSpaces.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(" ", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());
        DefaultPrettyPrinter prettyPrinterWithSpaces = new DefaultPrettyPrinter().withSpacesInObjectEntries();
        prettyPrinterWithSpaces.writeObjectEntrySeparator(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithSeparators() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSeparators = prettyPrinter.withSeparators(new Separators(',', ':', '[', ']', '{', '}'));
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinterWithSeparators.writeStartArray(jsonGenerator);
        prettyPrinterWithSeparators.writeEndArray(jsonGenerator, 1);
        assertEquals("[", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.reset();
        prettyPrinterWithSeparators.writeStartObject(jsonGenerator);
        prettyPrinterWithSeparators.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinterWithSeparators.writeObjectFieldName("name");
        prettyPrinterWithSeparators.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinterWithSeparators.writeString("John Doe");
        prettyPrinterWithSeparators.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinterWithSeparators.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinterWithSeparators.writeNumber(1);
        prettyPrinterWithSeparators.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinterWithSeparators.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinterWithSeparators.writeNull();
        prettyPrinterWithSeparators.writeEndObject(jsonGenerator, 3);
        assertEquals("{\"name\":\"John Doe\",\"1\":null}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteStartObjectWithRootSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("Testing "));
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("Testing {\n", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteEndObjectWithRootSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("Testing "));
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.writeStringFieldName("name");
        prettyPrinter.writeString("John Doe");
        prettyPrinter.writeStringFieldName("age");
        prettyPrinter.writeNumber(30);
        prettyPrinter.writeEndObject(jsonGenerator, 2);
        assertEquals("Testing {\n\"name\":\"John Doe\",\n\"age\":30\n}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();
        assertNotEquals(prettyPrinter, prettyPrinterWithSpaces);
    }

    @Test
    public void testDefaultPrettyPrinterWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("\t");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = prettyPrinter.withArrayIndenter(arrayIndenter);
        assertNotEquals(prettyPrinter, prettyPrinterWithArrayIndenter);
    }

    @Test
    public void testDefaultPrettyPrinterWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("\t");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = prettyPrinter.withObjectIndenter(objectIndenter);
        assertNotEquals(prettyPrinter, prettyPrinterWithObjectIndenter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorAndIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("Testing "));
        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("\t");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("\t");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter prettyPrinterWithRootSeparatorAndIndenter = new DefaultPrettyPrinter(prettyPrinter)
                .withRootSeparator(new SerializedString("Testing "))
                .withArrayIndenter(arrayIndenter)
                .withObjectIndenter(objectIndenter);
        JsonGenerator jsonGenerator = new TestJsonGenerator();
        prettyPrinterWithRootSeparatorAndIndenter.writeStartArray(jsonGenerator);
        prettyPrinterWithRootSeparatorAndIndenter.writeEndArray(jsonGenerator, 1);
        assertEquals("Testing [\n\t]\n", jsonGenerator.getOutputBuffer().toString());
    }

    private static class TestJsonGenerator extends JsonGenerator {
        private ByteArrayOutputStream outputStream;

        public TestJsonGenerator() {
            super(new ByteArrayOutputStream());
            this.outputStream = (ByteArrayOutputStream) getOutputTarget();
        }

        @Override
        public void reset() {
            super.reset();
            outputStream.reset();
        }

        public String getOutputBuffer() {
            return outputStream.toString();
        }
    }
}