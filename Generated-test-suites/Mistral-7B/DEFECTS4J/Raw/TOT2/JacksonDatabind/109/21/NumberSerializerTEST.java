package com.example; // Replace with your package name

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws Exception {
        //...
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        //...
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        //...
    }

    @Test
    public void testSerializeNull() throws Exception {
        //...
    }

    @Test
    public void testSerializeNegativeNumber() throws Exception {
        //...
    }

    @Test
    public void testSerializeLargeNumber() throws Exception {
        //...
    }

    @Test
    public void testSerializeNaN() throws Exception {
        //...
    }

    // Add other test cases for Infinity, -Infinity, and unsupported input types

    @Test
    public void testCreateContextual() throws Exception {
        // Test createContextual method
    }

    @Test
    public void testSerialize() throws Exception {
        // Test serialize method
    }

    @Test
    public void testGetSchema() throws Exception {
        // Test getSchema method
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        // Test acceptJsonFormatVisitor method
    }

}