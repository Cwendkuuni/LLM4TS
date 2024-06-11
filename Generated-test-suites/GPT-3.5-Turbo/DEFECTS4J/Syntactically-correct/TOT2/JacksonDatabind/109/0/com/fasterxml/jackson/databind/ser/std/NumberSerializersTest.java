package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        // Typical use case
        // edge case: negative short value
        // edge case: maximum short value
        // error scenario: null value
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        // Typical use case
        // edge case: negative integer value
        // edge case: maximum integer value
        // error scenario: null value
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        // Typical use case
        // edge case: negative number value
        // edge case: maximum integer value
        // error scenario: null value
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        // Typical use case
        // edge case: negative long value
        // edge case: maximum long value
        // error scenario: null value
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        // Typical use case
        // edge case: negative float value
        // edge case: maximum float value
        // error scenario: null value
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        // Typical use case
        // edge case: negative double value
        // edge case: maximum double value
        // error scenario: null value
    }
}