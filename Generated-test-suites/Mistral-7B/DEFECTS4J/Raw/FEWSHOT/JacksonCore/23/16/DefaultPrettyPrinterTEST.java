package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    // Test constructor and default initialization
    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        assertNull(defaultPrinter._rootSeparator);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, defaultPrinter._objectIndenter);
        assertEquals(FixedSpaceIndenter.instance, defaultPrinter._arrayIndenter);
        assertTrue(defaultPrinter._spacesInObjectEntries);
        assertEquals(0, defaultPrinter._nesting);
        assertNull(defaultPrinter._separators);
        assertNull(defaultPrinter._objectFieldValueSeparatorWithSpaces);
    }

    // Test constructor with rootSeparator
    @Test
    public void testConstructorWithRootSeparator() {
        String rootSeparator = ", ";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        assertEquals(new SerializedString(rootSeparator), prettyPrinter._rootSeparator);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter._objectIndenter);
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter._arrayIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);
    }

    // Test withRootSeparator method
    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter = defaultPrinter.withRootSeparator(new SerializedString(", "));
        assertEquals(new SerializedString(", "), prettyPrinter._rootSeparator);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter._objectIndenter);
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter._arrayIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);
        
        DefaultPrettyPrinter defaultPrinter2 = new DefaultPrettyPrinter(new SerializedString(""));
        DefaultPrettyPrinter prettyPrinter2 = defaultPrinter2.withRootSeparator(null);
        assertEquals(null, prettyPrinter2._rootSeparator);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter2._objectIndenter);
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter2._arrayIndenter);
        assertTrue(prettyPrinter2._spacesInObjectEntries);
        assertEquals(0, prettyPrinter2._nesting);
        assertNull(prettyPrinter2._separators);
        assertNull(prettyPrinter2._objectFieldValueSeparatorWithSpaces);
    }

    // Test withRootSeparator method with String
    @Test
    public void testWithRootSeparatorString() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter = defaultPrinter.withRootSeparator(", ");
        assertNull(prettyPrinter._rootSeparator);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter._objectIndenter);
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter._arrayIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);

        DefaultPrettyPrinter prettyPrinter2 = defaultPrinter.withRootSeparator("");
        assertEquals(new SerializedString(""), prettyPrinter2._rootSeparator);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter2._objectIndenter);
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter2._arrayIndenter);
        assertTrue(prettyPrinter2._spacesInObjectEntries);
        assertEquals(0, prettyPrinter2._nesting);
        assertNull(prettyPrinter2._separators);
        assertNull(prettyPrinter2._objectFieldValueSeparatorWithSpaces);
    }

    // Test indentArraysWith method
    @Test
    public void testIndentArraysWith() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new FixedSpaceIndenter();
        DefaultPrettyPrinter prettyPrinter = defaultPrinter.indentArraysWith(indenter);
        assertEquals(defaultPrinter._arrayIndenter, prettyPrinter._arrayIndenter);
        assertEquals(indenter, prettyPrinter._arrayIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);
    }

    // Test indentObjectsWith method
    @Test
    public void testIndentObjectsWith() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        Indenter indenter = new FixedSpaceIndenter();
        DefaultPrettyPrinter prettyPrinter = defaultPrinter.indentObjectsWith(indenter);
        assertEquals(defaultPrinter._objectIndenter, prettyPrinter._objectIndenter);
        assertEquals(indenter, prettyPrinter._objectIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);
    }

    // Test withArrayIndenter method
    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter = defaultPrinter.withArrayIndenter(new FixedSpaceIndenter());
        assertEquals(defaultPrinter._arrayIndenter, prettyPrinter._arrayIndenter);
        assertEquals(new FixedSpaceIndenter(), prettyPrinter._arrayIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);
    }

    // Test withObjectIndenter method
    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter = defaultPrinter.withObjectIndenter(new FixedSpaceIndenter());
        assertEquals(defaultPrinter._objectIndenter, prettyPrinter._objectIndenter);
        assertEquals(new FixedSpaceIndenter(), prettyPrinter._objectIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);
    }

    // Test withSpacesInObjectEntries method
    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinter = defaultPrinter.withSpacesInObjectEntries();
        assertEquals(defaultPrinter._spacesInObjectEntries, prettyPrinter._spacesInObjectEntries);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(0, prettyPrinter._nesting);
        assertNull(prettyPrinter._separators);
        assertNull(prettyPrinter._objectFieldValueSeparatorWithSpaces);

        DefaultPrettyPrinter prettyPrinter2 = defaultPrinter.withoutSpacesInObjectEntries();
        assertFalse(prettyPrinter2._spacesInObjectEntries);
        assertEquals(0, prettyPrinter2._nesting);
        assertNull(prettyPrinter2._separators);
        assertNull(prettyPrinter2._objectFieldValueSeparatorWithSpaces);
    }

    // Test writeRootValueSeparator method
    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(" "));
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals(new SerializedString(" ").getValue(), new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test writeStartObject method
    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeStartObject(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals("[{\n", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test beforeObjectEntries method
    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals("\n  ", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test writeObjectFieldValueSeparator method
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals(" : ", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test writeObjectEntrySeparator method
    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals("\n  {\n", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test writeEndObject method
    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeEndObject(jsonGenerator, 1);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals("\n}", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test writeStartArray method
    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeStartArray(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals("[", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test beforeArrayValues method
    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.beforeArrayValues(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals("\n  ", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test writeArrayValueSeparator method
    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals(",\n  ", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }

    // Test writeEndArray method
    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new ByteArrayOutputStream(100));
        prettyPrinter.writeEndArray(jsonGenerator, 1);
        byte[] bytes = jsonGenerator.getOutputBytes();
        assertEquals("\n]", new String(bytes, 0, bytes.length));
        jsonGenerator.close();
    }
}