package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonGenerator;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        
        printer.writeRootValueSeparator(generator);
        
        Mockito.verify(generator).writeRaw(Mockito.anyString());
    }

    @Test
    public void testWriteStartObject() throws Exception {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        
        printer.writeStartObject(generator);
        
        Mockito.verify(generator).writeRaw(Mockito.eq('{'));
    }

    // Add more test cases for other public methods in DefaultPrettyPrinter

}