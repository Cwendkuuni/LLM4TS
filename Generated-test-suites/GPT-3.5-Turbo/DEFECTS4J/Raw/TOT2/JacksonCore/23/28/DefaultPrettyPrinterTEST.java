package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws IOException {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        MockJsonGenerator generator = mock(MockJsonGenerator.class);

        printer.writeRootValueSeparator(generator);

        verify(generator).writeRaw(any());
    }
  
    // Add additional test cases for other methods here

}