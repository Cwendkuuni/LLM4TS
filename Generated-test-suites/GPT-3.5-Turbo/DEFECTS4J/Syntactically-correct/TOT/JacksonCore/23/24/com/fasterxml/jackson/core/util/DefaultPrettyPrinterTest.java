package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeRootValueSeparator(generator);
        
        assertEquals(" ", generator.getRawData());
    }
    
    @Test
    public void testWriteStartObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeStartObject(generator);
        
        String expected = "{";
        assertEquals(expected, generator.getRawData());
        assertEquals(1, printer._nesting);
    }
    
    @Test
    public void testBeforeObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer._nesting = 1;
        printer.beforeObjectEntries(generator);
        
        String expected = "  ";
        assertEquals(expected, generator.getRawData());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer._spacesInObjectEntries = true;
        printer.writeObjectFieldValueSeparator(generator);
        
        String expected = " : ";
        assertEquals(expected, generator.getRawData());
    }
    
    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer._nesting = 1;
        printer.writeObjectEntrySeparator(generator);
        
        String expected = ", ";
        assertEquals(expected, generator.getRawData());
        assertEquals("  ", generator.getIndentation());
    }
    
    @Test
    public void testWriteEndObject() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer._objectIndenter = new DefaultPrettyPrinter.NopIndenter();
        printer._nesting = 1;
        printer.writeEndObject(generator, 0);
        
        String expected = "}";
        assertEquals(expected, generator.getRawData());
    }
    
    @Test
    public void testWriteStartArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer.writeStartArray(generator);
        
        String expected = "[";
        assertEquals(expected, generator.getRawData());
        assertEquals(1, printer._nesting);
    }
    
    @Test
    public void testBeforeArrayValues() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer._nesting = 1;
        printer.beforeArrayValues(generator);
        
        String expected = "  ";
        assertEquals(expected, generator.getRawData());
    }
    
    @Test
    public void testWriteArrayValueSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer._nesting = 1;
        printer.writeArrayValueSeparator(generator);
        
        String expected = ", ";
        assertEquals(expected, generator.getRawData());
        assertEquals("  ", generator.getIndentation());
    }
    
    @Test
    public void testWriteEndArray() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        printer._arrayIndenter = new DefaultPrettyPrinter.NopIndenter();
        printer._nesting = 1;
        printer.writeEndArray(generator, 0);
        
        String expected = "]";
        assertEquals(expected, generator.getRawData());
    }
}