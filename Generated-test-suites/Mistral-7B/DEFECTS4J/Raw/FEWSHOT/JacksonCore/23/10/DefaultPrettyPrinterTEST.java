package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

/**
 * Test case for DefaultPrettyPrinter class
 */
public class DefaultPrettyPrinterTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("--"));
        assertNotNull(prettyPrinter);
        assertEquals("--", prettyPrinter.getRootSeparator().getValueAsString());
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSeparator = prettyPrinter.withRootSeparator(new SerializedString("="));
        assertNotSame(prettyPrinter, prettyPrinterWithSeparator);
        assertEquals("=", prettyPrinterWithSeparator.getRootSeparator().getValueAsString());
    }

    @Test
    public void testWriteRootValueSeparator() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(":"));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertEquals(":", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteStartObject() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteEndObject() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Object obj = new Object();
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        assertEquals("}", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteStartArray() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteEndArray() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Object[] array = new Object[1];
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        assertEquals("]", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",", jsonGenerator.getOutputBuffer().toString());
        jsonGenerator.close();
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();
        assertNotSame(prettyPrinter, prettyPrinterWithSpaces);
        assertTrue(prettyPrinterWithSpaces.isSpacesInObjectEntries());
    }

    @Test
    public void testWithObjectsIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("----");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter prettyPrinterWithIndenter = prettyPrinter.withObjectIndenter(indenter);
        assertNotSame(prettyPrinter, prettyPrinterWithIndenter);
        assertEquals("----", prettyPrinterWithIndenter.getObjectIndenter().writeIndentation(null, 0).toString());
    }

    @Test
    public void testWithArraysIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("--");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter prettyPrinterWithIndenter = prettyPrinter.withArrayIndenter(indenter);
        assertNotSame(prettyPrinter, prettyPrinterWithIndenter);
        assertEquals("--", prettyPrinterWithIndenter.getArrayIndenter().writeIndentation(null, 0).toString());
    }
}

class Indenter implements PrettyPrinter.Indenter {
    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        // implementation
    }

    @Override
    public boolean isInline() {
        // implementation
        return false;
    }
}