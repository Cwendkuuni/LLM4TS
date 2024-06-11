
package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Mock
    private JsonGenerator jsonGenerator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    // Test writeRootValueSeparator method
    @Test
    public void testWriteRootValueSeparator() throws IOException {
        SerializedString separator = new SerializedString(" ");
        defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw(separator);
    }

    // Test writeStartObject method
    @Test
    public void testWriteStartObject() throws IOException {
        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw('{');
    }

    // Test beforeObjectEntries method
    @Test
    public void testBeforeObjectEntries() throws IOException {
        int level = 0;
        defaultPrettyPrinter.beforeObjectEntries(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw('\t');
        verify(jsonGenerator, times(1)).writeRaw('\t');
    }

    // Test writeObjectFieldValueSeparator method
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw(' ');
        verify(jsonGenerator, times(1)).writeRaw(' ');
    }

    // Test writeObjectEntrySeparator method
    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw(',');
        verify(jsonGenerator, times(1)).writeRaw('\t');
    }

    // Test writeEndObject method
    @Test
    public void testWriteEndObject() throws IOException {
        int nrOfEntries = 0;
        defaultPrettyPrinter.writeEndObject(jsonGenerator, nrOfEntries);
        verify(jsonGenerator, times(1)).writeRaw(' ');
        verify(jsonGenerator, times(1)).writeRaw('}');
    }

    // Test writeStartArray method
    @Test
    public void testWriteStartArray() throws IOException {
        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw('[');
    }

    // Test beforeArrayValues method
    @Test
    public void testBeforeArrayValues() throws IOException {
        defaultPrettyPrinter.beforeArrayValues(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw('\t');
        verify(jsonGenerator, times(1)).writeRaw('\t');
    }

    // Test writeArrayValueSeparator method
    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw(',');
        verify(jsonGenerator, times(1)).writeRaw('\t');
    }

    // Test writeEndArray method
    @Test
    public void testWriteEndArray() throws IOException {
        int nrOfValues = 0;
        defaultPrettyPrinter.writeEndArray(jsonGenerator, nrOfValues);
        verify(jsonGenerator, times(1)).writeRaw(' ');
        verify(jsonGenerator, times(1)).writeRaw(']');
    }
}
