package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultPrettyPrinterTest {
    private DefaultPrettyPrinter printer;

    @Before
    public void setup() {
        printer = new DefaultPrettyPrinter();
    }

    @Test
    public void testWriteRootValueSeparator() {
        // Test case for root value separator

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();
        
        // Set the expected output
        String expectedOutput = " ";

        // Call the method under test
        printer.writeRootValueSeparator(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testWriteStartObject() {
        // Test case for starting object

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = "{";

        // Call the method under test
        printer.writeStartObject(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testBeforeObjectEntries() {
        // Test case for before object entries

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = "";

        // Call the method under test
        printer.beforeObjectEntries(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        // Test case for object field value separator

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = " : ";

        // Call the method under test
        printer.writeObjectFieldValueSeparator(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        // Test case for object entry separator

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = ", ";

        // Call the method under test
        printer.writeObjectEntrySeparator(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testWriteEndObject() {
        // Test case for ending object

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = "}";

        // Call the method under test
        printer.writeEndObject(generator, 0);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testWriteStartArray() {
        // Test case for starting array

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = "[";

        // Call the method under test
        printer.writeStartArray(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testBeforeArrayValues() {
        // Test case for before array values

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = "";

        // Call the method under test
        printer.beforeArrayValues(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testWriteArrayValueSeparator() {
        // Test case for array value separator

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = ", ";

        // Call the method under test
        printer.writeArrayValueSeparator(generator);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

    @Test
    public void testWriteEndArray() {
        // Test case for ending array

        // Create a mock JsonGenerator
        MockJsonGenerator generator = new MockJsonGenerator();

        // Set the expected output
        String expectedOutput = "]";

        // Call the method under test
        printer.writeEndArray(generator, 0);

        // Verify the output
        assertEquals(expectedOutput, generator.getRawOutput());
    }

}