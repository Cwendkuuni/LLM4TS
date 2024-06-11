package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstructor() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        assertNotNull(printer);
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.withRootSeparator(" ");
        assertNotNull(printer);
    }

    // Additional test cases for other methods can be added here

}