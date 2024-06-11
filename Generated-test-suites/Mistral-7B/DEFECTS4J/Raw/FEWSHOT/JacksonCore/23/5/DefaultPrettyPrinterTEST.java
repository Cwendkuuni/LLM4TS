package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Separators;

public class DefaultPrettyPrinterTest {

    @Test
    public void testConstructors() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, pp.getRootSeparator());

        String rootSeparator = "root-separator";
        pp = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        assertEquals(rootSeparator, pp.getRootSeparator().getValueAsString());

        // Test with null rootSeparator
        pp = new DefaultPrettyPrinter(null);
        assertNull(pp.getRootSeparator());

        // Test withIndentObjectsWith()
        Indenter indentObject = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
        pp = new DefaultPrettyPrinter();
        pp.indentObjectsWith(indentObject);
        assertEquals(indentObject, pp._objectIndenter);

        // Test withIndentArraysWith()
        Indenter indentArray = new FixedSpaceIndenter();
        pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(indentArray);
        assertEquals(indentArray, pp._arrayIndenter);
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        String rootSeparator = "root-separator";
        pp = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        pp.writeRootValueSeparator(g);
        assertEquals(rootSeparator, g.getOutputBuffer().toString());

        // Test with null rootSeparator
        pp = new DefaultPrettyPrinter();
        pp.writeRootValueSeparator(g);
        assertEquals("", g.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        pp.writeStartObject(g);
        assertEquals("{\n", g.getOutputBuffer().toString());

        Indenter indentObject = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
        pp = new DefaultPrettyPrinter();
        pp.indentObjectsWith(indentObject);
        pp.writeStartObject(g);
        assertEquals("{\n", g.getOutputBuffer().toString());
        g.writeRaw("    "); // write some indentation
        pp.writeStartObject(g);
        assertEquals("{\n    {\n", g.getOutputBuffer().toString());
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        pp.beforeObjectEntries(g);
        assertEquals("", g.getOutputBuffer().toString());

        Indenter indentObject = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
        pp = new DefaultPrettyPrinter();
        pp.indentObjectsWith(indentObject);
        pp.beforeObjectEntries(g);
        assertEquals("\n", g.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        Separators separators = new Separators(new SerializedString(":"), new SerializedString(","));
        pp.withSeparators(separators);

        pp.writeObjectFieldValueSeparator(g);
        assertEquals(":\n", g.getOutputBuffer().toString());

        pp.withSpacesInObjectEntries();
        pp.writeObjectFieldValueSeparator(g);
        assertEquals(" :\n", g.getOutputBuffer().toString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        Separators separators = new Separators(new SerializedString(":"), new SerializedString(","));
        pp.withSeparators(separators);

        pp.writeObjectEntrySeparator(g);
        assertEquals(",\n", g.getOutputBuffer().toString());

        Indenter indentObject = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
        pp = new DefaultPrettyPrinter();
        pp.indentObjectsWith(indentObject);
        pp.writeObjectEntrySeparator(g);
        assertEquals("\n    ", g.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        pp.writeEndObject(g, 0);
        assertEquals("}\n", g.getOutputBuffer().toString());

        Indenter indentObject = new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE();
        pp = new DefaultPrettyPrinter();
        pp.indentObjectsWith(indentObject);
        pp.writeEndObject(g, 0);
        assertEquals("}\n\n", g.getOutputBuffer().toString());

        pp.writeEndObject(g, 1);
        assertEquals("}\n    ", g.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        pp.writeStartArray(g);
        assertEquals("[", g.getOutputBuffer().toString());

        Indenter indentArray = new FixedSpaceIndenter();
        pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(indentArray);
        pp.writeStartArray(g);
        assertEquals("[", g.getOutputBuffer().toString());
        g.writeRaw("  "); // write some indentation
        pp.writeStartArray(g);
        assertEquals("[", g.getOutputBuffer().toString());
        g.writeRaw("  "); // write some indentation
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        pp.beforeArrayValues(g);
        assertEquals("", g.getOutputBuffer().toString());

        Indenter indentArray = new FixedSpaceIndenter();
        pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(indentArray);
        pp.beforeArrayValues(g);
        assertEquals("\n", g.getOutputBuffer().toString());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        Separators separators = new Separators(new SerializedString(":"), new SerializedString(","));
        pp.withSeparators(separators);

        pp.writeArrayValueSeparator(g);
        assertEquals(",\n", g.getOutputBuffer().toString());

        pp.withSpacesInObjectEntries();
        pp.writeArrayValueSeparator(g);
        assertEquals(" :\n", g.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        pp.writeEndArray(g, 0);
        assertEquals("]\n", g.getOutputBuffer().toString());

        Indenter indentArray = new FixedSpaceIndenter();
        pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(indentArray);
        pp.writeEndArray(g, 0);
        assertEquals("]\n\n", g.getOutputBuffer().toString());

        pp.writeEndArray(g, 1);
        assertEquals("]\n  ", g.getOutputBuffer().toString());
    }
}