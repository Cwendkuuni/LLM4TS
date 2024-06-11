package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeRootValueSeparator(g);

        verify(g, times(1)).writeRaw(anyString());
    }

    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.writeStartObject(g);

        verify(g, times(1)).writeRaw(anyChar());
    }

    // More test cases for other methods can be added here

}