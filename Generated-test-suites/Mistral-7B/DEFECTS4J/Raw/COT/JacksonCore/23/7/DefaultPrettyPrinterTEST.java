package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        try {
            prettyPrinter.writeRootValueSeparator(generator);
            prettyPrinter.writeStartObject(generator);
            prettyPrinter.writeEndObject(generator, 0);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        JsonGenerator generator = createTestGenerator();
        try {
            prettyPrinter.writeRootValueSeparator(generator);
            prettyPrinter.writeEndObject(generator, 0);
            assertEquals("test", prettyPrinter.getRootSeparator().asString());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        String rootSeparator = "test";
        prettyPrinter.withRootSeparator(new SerializedString(rootSeparator));
        try {
            prettyPrinter.writeRootValueSeparator(generator);
            assertEquals(rootSeparator, generator.getOutputBuffer().toString());
            generator.flush();
            assertEquals(rootSeparator, generator.getOutputBuffer().toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        try {
            prettyPrinter.writeStartObject(generator);
            assertEquals("{", generator.getOutputBuffer().toString());
            generator.flush();
            assertEquals("{", generator.getOutputBuffer().toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeEndObject(generator, 1);
        assertEquals("}", generator.getOutputBuffer().toString());
        generator.flush();
        assertEquals("}", generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndObjectWithZeroEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeEndObject(generator, 0);
        assertEquals("}", generator.getOutputBuffer().toString());
        generator.flush();
        assertEquals("}", generator.getOutputBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteEndObjectWithNegativeNumberOfEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeEndObject(generator, -1);
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        try {
            prettyPrinter.writeStartArray(generator);
            assertEquals("[", generator.getOutputBuffer().toString());
            generator.flush();
            assertEquals("[", generator.getOutputBuffer().toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeEndArray(generator, 1);
        assertEquals("]", generator.getOutputBuffer().toString());
        generator.flush();
        assertEquals("]", generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndArrayWithZeroValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeEndArray(generator, 0);
        assertEquals("]", generator.getOutputBuffer().toString());
        generator.flush();
        assertEquals("]", generator.getOutputBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteEndArrayWithNegativeNumberOfValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        prettyPrinter.writeStartArray(generator);
        prettyPrinter.writeEndArray(generator, -1);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        String separator = ": ";
        prettyPrinter.withObjectIndenter(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                // no indentation is required for this test case
            }

            @Override
            public boolean isInline() {
                return true;
            }
        });
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        assertEquals(separator, generator.getOutputBuffer().toString());
        generator.flush();
        assertEquals(separator, generator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = createTestGenerator();
        String separator = ", ";
        prettyPrinter.withObjectIndenter(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                // no indentation is required for this test case
            }

            @Override
            public boolean isInline() {
                return true;
            }
        });
        prettyPrinter.writeStartObject(generator);
        prettyPrinter.writeObjectEntrySeparator(generator);
        assertEquals(separator, generator.getOutputBuffer().toString());
        generator.flush();
        assertEquals(separator, generator.getOutputBuffer().toString());
    }

    private JsonGenerator createTestGenerator() throws IOException {
        return new JsonFactory().createGenerator(new StringWriter());
    }
}