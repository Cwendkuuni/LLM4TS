package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        assertNotNull(pp);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString("rootValueSeparator"));
        assertEquals("rootValueSeparator", pp.getRootSeparator().asString());
    }

    @Test
    public void testDefaultPrettyPrinterInstance() {
        DefaultPrettyPrinter pp1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter pp2 = new DefaultPrettyPrinter(pp1);
        assertEquals(pp1, pp2);
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp = pp.withRootSeparator(new SerializedString("newRootValueSeparator"));
        assertEquals("newRootValueSeparator", pp.getRootSeparator().asString());
    }

    @Test
    public void testIndentArraysWith() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("indentArraysWith:");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        pp.indentArraysWith(indenter);
        pp = pp.withArrayIndenter(indenter);

        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeStartArray(g);
        pp.writeEndArray(g, 1);

        verify(g, times(3)).writeRaw("indentArraysWith:");
        verify(g).writeRaw('[');
        verify(g).writeRaw(']');
    }

    @Test
    public void testIndentObjectsWith() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter indenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("indentObjectsWith:");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        pp.indentObjectsWith(indenter);
        pp = pp.withObjectIndenter(indenter);

        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeStartObject(g);
        pp.writeEndObject(g, 1);

        verify(g, times(3)).writeRaw("indentObjectsWith:");
        verify(g).writeRaw('{');
        verify(g).writeRaw('}');
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp = pp.withSpacesInObjectEntries();
        assertTrue(pp.isSpacesInObjectEntries());

        pp = pp.withoutSpacesInObjectEntries();
        assertFalse(pp.isSpacesInObjectEntries());
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeRootValueSeparator(g);
        verify(g).writeRaw("");

        pp = new DefaultPrettyPrinter(new SerializedString("rootValueSeparator"));
        pp.writeRootValueSeparator(g);
        verify(g).writeRaw("rootValueSeparator");
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeStartObject(g);
        verify(g).writeRaw("{");
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter indenter = mock(Indenter.class);
        pp.beforeObjectEntries(g);
        verify(indenter).writeIndentation(g, 0);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeObjectFieldValueSeparator(g);
        verify(g).writeRaw(":");
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter indenter = mock(Indenter.class);
        pp.writeObjectEntrySeparator(g);
        verify(indenter).writeIndentation(g, 0);
        verify(g).writeRaw(",");
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeEndObject(g, 1);
        verify(g).writeRaw("}");
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeStartArray(g);
        verify(g).writeRaw("[");
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter indenter = mock(Indenter.class);
        pp.beforeArrayValues(g);
        verify(indenter).writeIndentation(g, 0);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeArrayValueSeparator(g);
        verify(g).writeRaw(",");
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeEndArray(g, 1);
        verify(g).writeRaw("]");
    }

    private interface Indenter {
        void writeIndentation(JsonGenerator g, int level) throws IOException;

        boolean isInline();
    }
}