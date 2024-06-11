package com.fasterxml.jackson.databind.ser.std;
import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializer_instance() {
        NumberSerializer ns = new NumberSerializer(Number.class);
        assertNotNull(ns);
    }

    @Test
    public void testSerialize_BigDecimal() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of BigDecimal
        // Add test case here
    }

    @Test
    public void testSerialize_BigInteger() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of BigInteger
        // Add test case here
    }

    @Test
    public void testSerialize_Long() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of Long
        // Add test case here
    }

    @Test
    public void testSerialize_Double() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of Double
        // Add test case here
    }

    @Test
    public void testSerialize_Float() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of Float
        // Add test case here
    }

    @Test
    public void testSerialize_Integer() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of Integer
        // Add test case here
    }

    @Test
    public void testSerialize_Byte() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of Byte
        // Add test case here
    }

    @Test
    public void testSerialize_Short() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test serialization of Short
        // Add test case here
    }

    @Test
    public void testSerialize_Fallback() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test fallback serialization
        // Add test case here
    }

    @Test
    public void testGetSchema() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test getting schema
        // Add test case here
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer ns = NumberSerializer.instance;
        // Test accepting Json format visitor
        // Add test case here
    }

}