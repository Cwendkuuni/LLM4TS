package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();

        // Test serialize method
        assertEquals("12", serializer.serialize((short) 12));
        assertEquals("-45", serializer.serialize((short) -45));

        // Test getSchema method
        assertNull(serializer.getSchema(null, null));

        // Test acceptJsonFormatVisitor method
        // Not feasible to test here since it requires mocking.

        // Test createContextual method
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);

        // Test serialize method
        assertEquals("123", serializer.serialize(123));
        assertEquals("-789", serializer.serialize(-789));

        // Test getSchema method
        assertNull(serializer.getSchema(null, null));

        // Test acceptJsonFormatVisitor method
        // Not feasible to test here since it requires mocking.

        // Test createContextual method
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();

        // Test serialize method
        assertEquals("456", serializer.serialize(456));
        assertEquals("-7891", serializer.serialize(-7891));

        // Test getSchema method
        assertNull(serializer.getSchema(null, null));

        // Test acceptJsonFormatVisitor method
        // Not feasible to test here since it requires mocking.

        // Test createContextual method
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer(Long.class);

        // Test serialize method
        assertEquals("1234567890", serializer.serialize(1234567890L));
        assertEquals("-9876543210", serializer.serialize(-9876543210L));

        // Test getSchema method
        assertNull(serializer.getSchema(null, null));

        // Test acceptJsonFormatVisitor method
        // Not feasible to test here since it requires mocking.

        // Test createContextual method
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();

        // Test serialize method
        assertEquals("1.23", serializer.serialize(1.23f));
        assertEquals("-4.56", serializer.serialize(-4.56f));

        // Test getSchema method
        assertNull(serializer.getSchema(null, null));

        // Test acceptJsonFormatVisitor method
        // Not feasible to test here since it requires mocking.

        // Test createContextual method
        assertEquals(serializer, serializer.createContextual(null, null));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);

        // Test serialize method
        assertEquals("1.23456789", serializer.serialize(1.23456789));
        assertEquals("-9.87654321", serializer.serialize(-9.87654321));

        // Test getSchema method
        assertNull(serializer.getSchema(null, null));

        // Test acceptJsonFormatVisitor method
        // Not feasible to test here since it requires mocking.

        // Test createContextual method
        assertEquals(serializer, serializer.createContextual(null, null));
    }
}