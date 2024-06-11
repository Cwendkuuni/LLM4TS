package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.*;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testCreateInstance() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter instance = pp.createInstance();
        assertEquals(pp, instance);
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        pp.writeRootValueSeparator(g);
        assertEquals("", sw.toString());

        pp = new DefaultPrettyPrinter(new SerializedString(" "));
        sw = new StringWriter();
        g = new JsonFactory().createGenerator(sw);
        pp.writeRootValueSeparator(g);
        assertEquals(" ", sw.toString());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        pp.writeStartObject(g);
        assertEquals("{\n", sw.toString());

        pp = new DefaultPrettyPrinter(new SerializedString("  "));
        sw = new StringWriter();
        g = new JsonFactory().createGenerator(sw);
        pp.writeStartObject(g);
        assertEquals(" {\n", sw.toString());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        pp.writeStartObject(g);
        pp.writeEndObject(g, 0);
        assertEquals("}\n", sw.toString());

        pp = new DefaultPrettyPrinter(new SerializedString("  "));
        sw = new StringWriter();
        g = new JsonFactory().createGenerator(sw);
        pp.writeStartObject(g);
        pp.writeEndObject(g, 0);
        assertEquals(" }\n", sw.toString());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        pp.writeStartArray(g);
        assertEquals("[", sw.toString());

        pp = new DefaultPrettyPrinter(new SerializedString("  "));
        sw = new StringWriter();
        g = new JsonFactory().createGenerator(sw);
        pp.writeStartArray(g);
        assertEquals("[ \n", sw.toString());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        pp.writeStartArray(g);
        pp.writeEndArray(g, 0);
        assertEquals("]\n", sw.toString());

        pp = new DefaultPrettyPrinter(new SerializedString("  "));
        sw = new StringWriter();
        g = new JsonFactory().createGenerator(sw);
        pp.writeStartArray(g);
        pp.writeEndArray(g, 0);
        assertEquals("]\n", sw.toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        pp.writeStartObject(g);
        pp.writeObjectFieldValueSeparator(g);
        assertEquals(": ", sw.toString());

        pp = new DefaultPrettyPrinter(new Separators());
        sw = new StringWriter();
        g = new JsonFactory().createGenerator(sw);
        pp.withSeparators(pp.getDefaultSeparators());
        pp.writeStartObject(g);
        pp.writeObjectFieldValueSeparator(g);
        assertEquals(": ", sw.toString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        StringWriter sw = new StringWriter();
        JsonGenerator g = new JsonFactory().createGenerator(sw);
        pp.writeStartObject(g);
        pp.writeObjectEntrySeparator(g);
        assertEquals(",\n", sw.toString());

        pp = new DefaultPrettyPrinter(new Separators());
        sw = new StringWriter();
        g = new JsonFactory().createGenerator(sw);
        pp.withSeparators(pp.getDefaultSeparators());
        pp.writeStartObject(g);
        pp.writeObjectEntrySeparator(g);
        assertEquals(",\n", sw.toString());
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        assertEquals(pp, pp.withRootSeparator(null));
        assertEquals(pp, pp.withRootSeparator(""));

        String rootSeparator = "root";
        pp = pp.withRootSeparator(new SerializedString(rootSeparator));
        assertNotEquals(pp, pp.withRootSeparator(null));
        assertNotEquals(pp, pp.withRootSeparator(""));
        assertEquals(rootSeparator, pp.getRootSeparator().getValueAsString());
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        assertEquals(pp, pp.withSeparators(null));
        assertEquals(pp, pp.withSeparators(new Separators()));

        Separators separators = new Separators();
        pp = pp.withSeparators(separators);
        assertEquals(separators, pp.getSeparators());
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter indenter = new FixedSpaceIndenter();
        assertEquals(pp, pp.withArrayIndenter(null));
        assertEquals(pp, pp.withArrayIndenter(indenter));

        assertEquals(indenter, pp.getArrayIndenter());
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter indenter = new FixedSpaceIndenter();
        assertEquals(pp, pp.withObjectIndenter(null));
        assertEquals(pp, pp.withObjectIndenter(indenter));

        assertEquals(indenter, pp.getObjectIndenter());
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        assertEquals(pp, pp.withSpacesInObjectEntries());
        assertEquals(pp, pp.withoutSpacesInObjectEntries());

        DefaultPrettyPrinter ppWithSpaces = pp.withSpacesInObjectEntries();
        ppWithSpaces = ppWithSpaces.withSpacesInObjectEntries();
        assertNotEquals(pp, ppWithSpaces);

        DefaultPrettyPrinter ppWithoutSpaces = pp.withoutSpacesInObjectEntries();
        ppWithoutSpaces = ppWithoutSpaces.withoutSpacesInObjectEntries();
        assertNotEquals(pp, ppWithoutSpaces);
    }
}