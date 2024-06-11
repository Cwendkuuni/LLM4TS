package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.io.*;
import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class DefaultPrettyPrinterTest {
    
    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeRootValueSeparator(gen);
        verify(gen).writeRaw(any(SerializedString.class));
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeStartObject(gen);
        verify(gen).writeRaw('{');
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.beforeObjectEntries(gen);
        verifyZeroInteractions(gen);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeObjectFieldValueSeparator(gen);
        verify(gen).writeRaw(anyString());
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeObjectEntrySeparator(gen);
        verify(gen).writeRaw(anyString());
    }

    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeEndObject(gen, 0);
        verify(gen).writeRaw(anyChar());
    }

    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeStartArray(gen);
        verify(gen).writeRaw(anyChar());
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.beforeArrayValues(gen);
        verifyZeroInteractions(gen);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeArrayValueSeparator(gen);
        verify(gen).writeRaw(anyString());
    }

    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeEndArray(gen, 0);
        verify(gen).writeRaw(anyChar());
    }   
}