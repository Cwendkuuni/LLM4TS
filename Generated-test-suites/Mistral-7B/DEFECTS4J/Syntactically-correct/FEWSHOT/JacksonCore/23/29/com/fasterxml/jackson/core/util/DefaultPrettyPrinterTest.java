package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        String json = prettyPrinter.printValue("{\"key\":\"value\"}");
        assertEquals("{\n  \"key\": \"value\"\n}", json);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(" --"));
        String json = prettyPrinter.printValue("{\"key\":\"value\"}");
        assertEquals("--{\n  \"key\": \"value\"\n}", json);
    }

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeRootValueSeparator(generator);
        verify(generator, never()).writeRaw("");

        String rootSeparator = "---";
        prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinter.writeRootValueSeparator(generator);
        verify(generator).writeRaw(rootSeparator);
    }

    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeStartObject(generator);
        verify(generator).writeRaw("{");
    }

    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.beforeObjectEntries(generator);
        verify(generator, never()).writeRaw("");
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        verify(generator).writeRaw(":");
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeObjectEntrySeparator(generator);
        verify(generator).writeRaw(",\n  ");
    }

    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeEndObject(generator, 1);
        verify(generator).writeRaw("}\n");
    }

    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeStartArray(generator);
        verify(generator).writeRaw("[");
    }

    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.beforeArrayValues(generator);
        verify(generator, never()).writeRaw("");
    }

    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeArrayValueSeparator(generator);
        verify(generator).writeRaw(",\n  ");
    }

    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeEndArray(generator, 1);
        verify(generator).writeRaw("]\n");
    }

    @Test
    public void testIndentArraysWith() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = mock(Indenter.class);
        prettyPrinter.indentArraysWith(indenter);
        assertEquals(prettyPrinter._arrayIndenter, indenter);
    }

    @Test
    public void testIndentObjectsWith() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter indenter = mock(Indenter.class);
        prettyPrinter.indentObjectsWith(indenter);
        assertEquals(prettyPrinter._objectIndenter, indenter);
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter withSpaces = prettyPrinter.withSpacesInObjectEntries();
        assertNotEquals(prettyPrinter, withSpaces);
        assertTrue(withSpaces._spacesInObjectEntries);
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators(": ", ", ", "[", "]");
        DefaultPrettyPrinter withSeparators = prettyPrinter.withSeparators(separators);
        assertEquals(separators, withSeparators._separators);
        assertEquals(separators.getObjectFieldValueSeparator(), withSpaces._objectFieldValueSeparatorWithSpaces);
    }
}