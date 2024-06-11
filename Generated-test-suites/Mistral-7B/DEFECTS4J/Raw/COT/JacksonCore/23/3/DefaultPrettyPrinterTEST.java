package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonGenerator;

/**
 * Test class for DefaultPrettyPrinter
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        assertNotNull(prettyPrinter);
        assertEquals("test", prettyPrinter.getRootSeparator().getValueAsString());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorStringConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        DefaultPrettyPrinter prettyPrinter2 = new DefaultPrettyPrinter("test");
        assertSame(prettyPrinter, prettyPrinter2);
    }

    @Test
    public void testDefaultPrettyPrinterCopyConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterCopy = new DefaultPrettyPrinter(prettyPrinter);
        assertEquals(prettyPrinter._arrayIndenter, prettyPrinterCopy._arrayIndenter);
        assertEquals(prettyPrinter._objectIndenter, prettyPrinterCopy._objectIndenter);
        assertEquals(prettyPrinter._spacesInObjectEntries, prettyPrinterCopy._spacesInObjectEntries);
        assertEquals(prettyPrinter._nesting, prettyPrinterCopy._nesting);
        assertEquals(prettyPrinter._separators, prettyPrinterCopy._separators);
        assertEquals(prettyPrinter._objectFieldValueSeparatorWithSpaces, prettyPrinterCopy._objectFieldValueSeparatorWithSpaces);
        assertEquals(prettyPrinter._rootSeparator, prettyPrinterCopy._rootSeparator);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorSetter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        SerializedString rootSeparator = new SerializedString("test");
        prettyPrinter.withRootSeparator(rootSeparator);
        assertEquals(rootSeparator, prettyPrinter.getRootSeparator());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorStringSetter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        String rootSeparator = "test";
        prettyPrinter.withRootSeparator(rootSeparator);
        assertEquals(new SerializedString(rootSeparator), prettyPrinter.getRootSeparator());
    }

    @Test
    public void testDefaultPrettyPrinterWithArrayIndenterSetter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter arrayIndenter = NopIndenter.instance;
        prettyPrinter.indentArraysWith(arrayIndenter);
        assertEquals(arrayIndenter, prettyPrinter._arrayIndenter);
    }

    @Test
    public void testDefaultPrettyPrinterWithObjectIndenterSetter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = NopIndenter.instance;
        prettyPrinter.indentObjectsWith(objectIndenter);
        assertEquals(objectIndenter, prettyPrinter._objectIndenter);
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntriesSetter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        boolean spacesInObjectEntries = true;
        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        assertEquals(spacesInObjectEntries, prettyPrinter._spacesInObjectEntries);
        assertTrue(prettyPrinterWithSpacesInObjectEntries._spacesInObjectEntries);
    }

    @Test
    public void testDefaultPrettyPrinterWithoutSpacesInObjectEntriesSetter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        boolean spacesInObjectEntries = false;
        DefaultPrettyPrinter prettyPrinterWithoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
        assertEquals(spacesInObjectEntries, prettyPrinter._spacesInObjectEntries);
        assertFalse(prettyPrinterWithoutSpacesInObjectEntries._spacesInObjectEntries);
    }

    @Test
    public void testDefaultPrettyPrinterWithSeparatorsSetter() {
        Separators separators = new Separators();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withSeparators(separators);
        assertEquals(separators, prettyPrinter._separators);
    }

    @Test
    public void testDefaultPrettyPrinterWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        assertNull(jsonGenerator.getCurrentOutputEntry());
    }

    @Test
    public void testDefaultPrettyPrinterWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeStartObject(jsonGenerator);
        assertEquals("{", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        assertEquals(": ", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        assertEquals(",\n", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        assertEquals("}\n", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeStartArray(jsonGenerator);
        assertEquals("[", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        assertEquals("", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        assertEquals(",\n", jsonGenerator.getOutputStandaloneString());
    }

    @Test
    public void testDefaultPrettyPrinterWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(10), null);
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        assertEquals("]\n", jsonGenerator.getOutputStandaloneString());
    }

    /**
     * Test for edge cases and exception handling
     */

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPrettyPrinterConstructorWithNull() {
        new DefaultPrettyPrinter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPrettyPrinterWithNullRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withRootSeparator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPrettyPrinterWithNullIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter nullIndenter = null;
        try {
            prettyPrinter.indentArraysWith(nullIndenter);
        } catch (IllegalArgumentException e) {
            assertEquals("Argument cannot be null", e.getMessage());
            throw e;
        }
    }
}