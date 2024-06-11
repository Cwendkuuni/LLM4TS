package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        assertNotNull(serializer);
        // Test serialization of BigDecimal
        // Add test case for typical use case
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        assertNotNull(serializer);
        // Test serialization of BigInteger
        // Add test case for typical use case
    }

    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        assertNotNull(serializer);
        // Test serialization of Long
        // Add test case for typical use case
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        assertNotNull(serializer);
        // Test serialization of Double
        // Add test case for typical use case
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        assertNotNull(serializer);
        // Test serialization of Float
        // Add test case for typical use case
    }

    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        assertNotNull(serializer);
        // Test serialization of Integer
        // Add test case for typical use case
    }

    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        assertNotNull(serializer);
        // Test serialization of Byte
        // Add test case for typical use case
    }

    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        assertNotNull(serializer);
        // Test serialization of Short
        // Add test case for typical use case
    }

    // Add test cases for edge cases and exception handling scenarios

}