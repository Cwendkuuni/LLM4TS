package com.fasterxml.jackson.core.util;

import java.io.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("serial")
public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(g);
        g.flush();
        assertEquals(" ", g.toString());

        g.close();
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(g);
        g.flush();
        assertEquals("{", g.toString());

        g.close();
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.beforeObjectEntries(g);
        g.flush();
        assertEquals("", g.toString());

        g.close();
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeObjectFieldValueSeparator(g);
        g.flush();
        assertEquals(" :", g.toString());

        g.close();
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeObjectEntrySeparator(g);
        g.flush();
        assertEquals(",", g.toString());

        g.close();
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeEndObject(g, 1);
        g.flush();
        assertEquals(" }", g.toString());

        g.close();
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartArray(g);
        g.flush();
        assertEquals("[", g.toString());

        g.close();
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.beforeArrayValues(g);
        g.flush();
        assertEquals("", g.toString());

        g.close();
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeArrayValueSeparator(g);
        g.flush();
        assertEquals(",", g.toString());

        g.close();
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeEndArray(g, 1);
        g.flush();
        assertEquals(" ]", g.toString());

        g.close();
    }
}