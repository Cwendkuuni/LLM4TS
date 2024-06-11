package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter defaultPrettyPrinter;

    @Before
    public void setUp() throws Exception {
        defaultPrettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.writeRootValueSeparator(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.writeRootValueSeparator(g2);
        // add assertions
        
    }

    @Test
    public void testWriteStartObject() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.writeStartObject(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.writeStartObject(g2);
        // add assertions
        
    }

    @Test
    public void testBeforeObjectEntries() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.beforeObjectEntries(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.beforeObjectEntries(g2);
        // add assertions
        
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(g2);
        // add assertions
        
    }

    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.writeObjectEntrySeparator(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.writeObjectEntrySeparator(g2);
        // add assertions
        
    }

    @Test
    public void testWriteEndObject() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        int nrOfEntries1 = 0;
        defaultPrettyPrinter.writeEndObject(g1, nrOfEntries1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        int nrOfEntries2 = 0;
        defaultPrettyPrinter.writeEndObject(g2, nrOfEntries2);
        // add assertions
        
    }

    @Test
    public void testWriteStartArray() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.writeStartArray(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.writeStartArray(g2);
        // add assertions
        
    }

    @Test
    public void testBeforeArrayValues() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.beforeArrayValues(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.beforeArrayValues(g2);
        // add assertions
        
    }

    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        defaultPrettyPrinter.writeArrayValueSeparator(g1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        defaultPrettyPrinter.writeArrayValueSeparator(g2);
        // add assertions
        
    }

    @Test
    public void testWriteEndArray() throws Exception {
        // Test case 1
        JsonGenerator g1 = new JsonGenerator();
        int nrOfValues1 = 0;
        defaultPrettyPrinter.writeEndArray(g1, nrOfValues1);
        // add assertions
        

        // Test case 2
        JsonGenerator g2 = new JsonGenerator();
        int nrOfValues2 = 0;
        defaultPrettyPrinter.writeEndArray(g2, nrOfValues2);
        // add assertions
        
    }

}