package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.FixedSpaceIndenter;

public class DefaultPrettyPrinterTest {

    private JsonGenerator mockJsonGenerator;
    private DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setUp() {
        mockJsonGenerator = mock(JsonGenerator.class);
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, defaultPrettyPrinter.getRootSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, defaultPrettyPrinter.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, defaultPrettyPrinter.getArrayIndenter());
    }

    @Test
    public void testConstructorWithSeparator() {
        String rootSeparator = "rootSeparator";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        assertEquals(new SerializedString(rootSeparator), prettyPrinter.getRootSeparator());
    }

    @Test
    public void testConstructorWithSerializerString() {
        SerializableString rootSeparator = new SerializedString("rootSeparator");
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        assertEquals(rootSeparator, prettyPrinter.getRootSeparator());
    }

    @Test
    public void testWithRootSeparator() {
        SerializableString rootSeparator = new SerializedString("rootSeparator");
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withRootSeparator(rootSeparator);
        assertEquals(rootSeparator, newPrettyPrinter.getRootSeparator());
    }

    @Test
    public void testWithRootSeparatorString() {
        String rootSeparator = "rootSeparator";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter newPrettyPrinter = prettyPrinter.withRootSeparator(rootSeparator);
        assertEquals(new SerializedString(rootSeparator), newPrettyPrinter.getRootSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.withRootSeparator(null);
    }

    @Test
    public void testIndentArraysWith() {
        Indenter indenter = mock(Indenter.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(indenter);
        assertEquals(indenter, prettyPrinter.getArrayIndenter());
    }

    @Test
    public void testIndentObjectsWith() {
        Indenter indenter = mock(Indenter.class);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(indenter);
        assertEquals(indenter, prettyPrinter.getObjectIndenter());
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        String rootSeparator = "rootSeparator";
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(rootSeparator);
    }

    @Test
    public void testWriteStartObject() throws IOException, JsonProcessingException {
        when(mockJsonGenerator.writeRaw('{')).thenReturn(true);
        prettyPrinter.writeStartObject(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw('{');
    }

    @Test
    public void testWriteEndObject() throws IOException, JsonProcessingException {
        when(mockJsonGenerator.writeRaw('}')).thenReturn(true);
        when(mockJsonGenerator.writeRaw(eq(_separators.getObjectEntrySeparator()))).thenReturn(true);
        when(mockJsonGenerator.writeRaw(' ')).thenReturn(true);
        prettyPrinter.writeEndObject(mockJsonGenerator, 2);
        verify(mockJsonGenerator).writeRaw('}');
        verify(mockJsonGenerator, times(2)).writeRaw(_separators.getObjectEntrySeparator());
        verify(mockJsonGenerator).writeRaw(' ');
    }

    @Test
    public void testWriteStartArray() throws IOException, JsonProcessingException {
        when(mockJsonGenerator.writeRaw('[')).thenReturn(true);
        prettyPrinter.writeStartArray(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw('[');
    }

    @Test
    public void testWriteEndArray() throws IOException, JsonProcessingException {
        when(mockJsonGenerator.writeRaw(']')).thenReturn(true);
        when(mockJsonGenerator.writeRaw(eq(_separators.getArrayValueSeparator()))).thenReturn(true);
        when(mockJsonGenerator.writeRaw(' ')).thenReturn(true);
        prettyPrinter.writeEndArray(mockJsonGenerator, 2);
        verify(mockJsonGenerator).writeRaw(']');
        verify(mockJsonGenerator, times(2)).writeRaw(_separators.getArrayValueSeparator());
        verify(mockJsonGenerator).writeRaw(' ');
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        when(mockJsonGenerator.writeRaw(":")).thenReturn(true);
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(":");
    }

    @Test
    public void testWriteObjectFieldValueSeparatorWithSpaces() throws IOException {
        prettyPrinter.withSpacesInObjectEntries();
        when(mockJsonGenerator.writeRaw(_objectFieldValueSeparatorWithSpaces)).thenReturn(true);
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(_objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        when(mockJsonGenerator.writeRaw(_separators.getObjectEntrySeparator())).thenReturn(true);
        prettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(_separators.getObjectEntrySeparator());
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        when(mockJsonGenerator.writeRaw(_separators.getArrayValueSeparator())).thenReturn(true);
        prettyPrinter.writeArrayValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(_separators.getArrayValueSeparator());
    }

    @Test
    public void testWriteStartObjectWithNullIndenter() throws IOException {
        Indenter nullIndenter = null;
        try {
            prettyPrinter.writeStartObject(mockJsonGenerator, nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWriteStartObjectWithEmptyIndenter() throws IOException {
        when(mockJsonGenerator.writeRaw('{')).thenReturn(true);
        Indenter emptyIndenter = mock(Indenter.class);
        when(emptyIndenter.isInline()).thenReturn(true);
        prettyPrinter.writeStartObject(mockJsonGenerator, emptyIndenter);
        verify(mockJsonGenerator).writeRaw('{');
    }

    @Test
    public void testWriteEndObjectWithNullIndenter() throws IOException {
        Indenter nullIndenter = null;
        try {
            prettyPrinter.writeEndObject(mockJsonGenerator, 0, nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWriteEndObjectWithEmptyIndenter() throws IOException {
        when(mockJsonGenerator.writeRaw('}')).thenReturn(true);
        Indenter emptyIndenter = mock(Indenter.class);
        when(emptyIndenter.isInline()).thenReturn(true);
        prettyPrinter.writeEndObject(mockJsonGenerator, 0, emptyIndenter);
        verify(mockJsonGenerator).writeRaw('}');
    }

    @Test
    public void testWriteStartArrayWithNullIndenter() throws IOException {
        Indenter nullIndenter = null;
        try {
            prettyPrinter.writeStartArray(mockJsonGenerator, nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWriteStartArrayWithEmptyIndenter() throws IOException {
        when(mockJsonGenerator.writeRaw('[')).thenReturn(true);
        Indenter emptyIndenter = mock(Indenter.class);
        when(emptyIndenter.isInline()).thenReturn(true);
        prettyPrinter.writeStartArray(mockJsonGenerator, emptyIndenter);
        verify(mockJsonGenerator).writeRaw('[');
    }

    @Test
    public void testWriteEndArrayWithNullIndenter() throws IOException {
        Indenter nullIndenter = null;
        try {
            prettyPrinter.writeEndArray(mockJsonGenerator, 0, nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWriteEndArrayWithEmptyIndenter() throws IOException {
        when(mockJsonGenerator.writeRaw(']')).thenReturn(true);
        Indenter emptyIndenter = mock(Indenter.class);
        when(emptyIndenter.isInline()).thenReturn(true);
        prettyPrinter.writeEndArray(mockJsonGenerator, 0, emptyIndenter);
        verify(mockJsonGenerator).writeRaw(']');
    }

    @Test
    public void testWriteObjectEntrySeparatorWithNullIndenter() throws IOException {
        Indenter nullIndenter = null;
        try {
            prettyPrinter.writeObjectEntrySeparator(mockJsonGenerator, nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWriteObjectEntrySeparatorWithEmptyIndenter() throws IOException {
        when(mockJsonGenerator.writeRaw(_separators.getObjectEntrySeparator())).thenReturn(true);
        Indenter emptyIndenter = mock(Indenter.class);
        when(emptyIndenter.isInline()).thenReturn(true);
        prettyPrinter.writeObjectEntrySeparator(mockJsonGenerator, emptyIndenter);
        verify(mockJsonGenerator).writeRaw(_separators.getObjectEntrySeparator());
    }

    @Test
    public void testWriteArrayValueSeparatorWithNullIndenter() throws IOException {
        Indenter nullIndenter = null;
        try {
            prettyPrinter.writeArrayValueSeparator(mockJsonGenerator, nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWriteArrayValueSeparatorWithEmptyIndenter() throws IOException {
        when(mockJsonGenerator.writeRaw(_separators.getArrayValueSeparator())).thenReturn(true);
        Indenter emptyIndenter = mock(Indenter.class);
        when(emptyIndenter.isInline()).thenReturn(true);
        prettyPrinter.writeArrayValueSeparator(mockJsonGenerator, emptyIndenter);
        verify(mockJsonGenerator).writeRaw(_separators.getArrayValueSeparator());
    }

    @Test
    public void testGetObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getObjectIndenter());
    }

    @Test
    public void testGetArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getArrayIndenter());
    }

    @Test
    public void testGetRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getRootSeparator());
    }

    @Test
    public void testIsInlineObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertFalse(prettyPrinter.isInlineObjectIndenter());
    }

    @Test
    public void testIsInlineArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertTrue(prettyPrinter.isInlineArrayIndenter());
    }
}