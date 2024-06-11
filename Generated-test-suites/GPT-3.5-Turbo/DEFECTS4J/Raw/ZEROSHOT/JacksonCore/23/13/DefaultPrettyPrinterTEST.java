package com.fasterxml.jackson.core.util;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultPrettyPrinterTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(new SerializedString(" "));
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        verify(jsonGenerator).writeRaw('{');
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.beforeObjectEntries(jsonGenerator);
        verify(jsonGenerator).writeRaw(' ');
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(new SerializedString(" : "));
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(new SerializedString(", "));
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeEndObject(jsonGenerator, 0);
        verify(jsonGenerator).writeRaw(new SerializedString(" }"));
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        verify(jsonGenerator).writeRaw('[');
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.beforeArrayValues(jsonGenerator);
        verify(jsonGenerator).writeRaw(' ');
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        verify(jsonGenerator).writeRaw(new SerializedString(", "));
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeEndArray(jsonGenerator, 0);
        verify(jsonGenerator).writeRaw(new SerializedString(" ]"));
    }

}