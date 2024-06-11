package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

import org.junit.Test;

/**
 * Unit tests for DefaultPrettyPrinter class.
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        assertNull(prettyPrinter._rootSeparator);
        assertInstanceOf(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE.getClass(), prettyPrinter._objectIndenter);
        assertInstanceOf(FixedSpaceIndenter.instance.getClass(), prettyPrinter._arrayIndenter);
    }

    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        assertNotNull(prettyPrinter);
        assertEquals("test", prettyPrinter._rootSeparator.getValue());
        assertInstanceOf(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE.getClass(), prettyPrinter._objectIndenter);
        assertInstanceOf(FixedSpaceIndenter.instance.getClass(), prettyPrinter._arrayIndenter);
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeRootValueSeparator(generator);
        assertNull(prettyPrinter._rootSeparator);
        verify(generator).writeRaw("");
    }

    @Test
    public void testWriteRootValueSeparatorWithRootSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeRootValueSeparator(generator);
        assertEquals("test", prettyPrinter._rootSeparator.getValue());
        verify(generator).writeRaw("test");
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeStartObject(generator);
        verify(generator).writeRaw("{");
    }

    @Test
    public void testWriteStartObjectWithObjectIndenter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter objectIndenter = mock(Indenter.class);
        when(objectIndenter.isInline()).thenReturn(false);
        whenever(objectIndenter.writeIndentation(generator, 0)).thenReturn(null);
        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.writeStartObject(generator);
        verify(generator).writeRaw("{");
        verify(objectIndenter).writeIndentation(generator, 1);
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter objectIndenter = mock(Indenter.class);
        whenever(objectIndenter.isInline()).thenReturn(false);
        whenever(objectIndenter.writeIndentation(generator, 1)).thenReturn(null);
        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.beforeObjectEntries(generator);
        verify(objectIndenter).writeIndentation(generator, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteObjectFieldValueSeparatorWithNullObjectIndenter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter nullObjectIndenter = null;
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        prettyPrinter.withObjectIndenter(nullObjectIndenter);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        verify(generator).writeRaw("  ");

        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter._spacesInObjectEntries = false;
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        verify(generator).writeRaw(",");
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter objectIndenter = mock(Indenter.class);
        whenever(objectIndenter.isInline()).thenReturn(false);
        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.writeObjectEntrySeparator(generator);
        verify(objectIndenter).writeIndentation(generator, 2);
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter objectIndenter = mock(Indenter.class);
        whenever(objectIndenter.isInline()).thenReturn(false);
        whenever(objectIndenter.writeIndentation(generator, 0)).thenReturn(null);
        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.writeEndObject(generator, 1);
        verify(generator).writeRaw("}");
        verify(objectIndenter).writeIndentation(generator, 0);
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeStartArray(generator);
        verify(generator).writeRaw("[");
    }

    @Test
    public void testWriteStartArrayWithArrayIndenter() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter arrayIndenter = mock(Indenter.class);
        when(arrayIndenter.isInline()).thenReturn(false);
        whenever(arrayIndenter.writeIndentation(generator, 0)).thenReturn(null);
        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.writeStartArray(generator);
        verify(generator).writeRaw("[");
        verify(arrayIndenter).writeIndentation(generator, 1);
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter arrayIndenter = mock(Indenter.class);
        whenever(arrayIndenter.isInline()).thenReturn(false);
        whenever(arrayIndenter.writeIndentation(generator, 1)).thenReturn(null);
        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.beforeArrayValues(generator);
        verify(arrayIndenter).writeIndentation(generator, 2);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        prettyPrinter.writeArrayValueSeparator(generator);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        verify(generator).writeRaw("  ");

        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter._spacesInObjectEntries = false;
        prettyPrinter.writeArrayValueSeparator(generator);
        verify(generator).writeRaw(",");
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = mock(JsonGenerator.class);
        Indenter arrayIndenter = mock(Indenter.class);
        whenever(arrayIndenter.isInline()).thenReturn(false);
        whenever(arrayIndenter.writeIndentation(generator, 0)).thenReturn(null);
        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.writeEndArray(generator, 1);
        verify(generator).writeRaw("]");
        verify(arrayIndenter).writeIndentation(generator, 0);
    }
}