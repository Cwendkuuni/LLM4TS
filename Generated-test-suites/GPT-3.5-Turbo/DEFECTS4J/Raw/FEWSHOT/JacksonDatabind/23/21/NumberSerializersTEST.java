package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        // Test cases for ShortSerializer
        ShortSerializer serializer = new ShortSerializer();
        assertEquals(1, serializer._numberType.getValue());
        assertEquals("number", serializer._schemaType);

        // TODO: Add more test cases for the serialize method
    }

    @Test
    public void testIntegerSerializer() {
        // Test cases for IntegerSerializer
        IntegerSerializer serializer = new IntegerSerializer();
        assertEquals(3, serializer._numberType.getValue());
        assertEquals("integer", serializer._schemaType);

        // TODO: Add more test cases for the serialize method
    }

    @Test
    public void testIntLikeSerializer() {
        // Test cases for IntLikeSerializer
        IntLikeSerializer serializer = new IntLikeSerializer();
        assertEquals(3, serializer._numberType.getValue());
        assertEquals("integer", serializer._schemaType);

        // TODO: Add more test cases for the serialize method
    }

    @Test
    public void testLongSerializer() {
        // Test cases for LongSerializer
        LongSerializer serializer = new LongSerializer();
        assertEquals(2, serializer._numberType.getValue());
        assertEquals("number", serializer._schemaType);

        // TODO: Add more test cases for the serialize method
    }

    @Test
    public void testFloatSerializer() {
        // Test cases for FloatSerializer
        FloatSerializer serializer = new FloatSerializer();
        assertEquals(5, serializer._numberType.getValue());
        assertEquals("number", serializer._schemaType);

        // TODO: Add more test cases for the serialize method
    }

    @Test
    public void testDoubleSerializer() {
        // Test cases for DoubleSerializer
        DoubleSerializer serializer = new DoubleSerializer();
        assertEquals(6, serializer._numberType.getValue());
        assertEquals("number", serializer._schemaType);

        // TODO: Add more test cases for the serialize method
    }
}