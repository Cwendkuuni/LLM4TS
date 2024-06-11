package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();

        // Typical use cases
        assertEquals("123", shortSerializer.serialize(new Short("123")));
        assertEquals("-45", shortSerializer.serialize(new Short("-45")));

        // Edge cases
        assertEquals("0", shortSerializer.serialize(new Short("0")));
        assertEquals("32767", shortSerializer.serialize(new Short("32767")));
        assertEquals("-32768", shortSerializer.serialize(new Short("-32768")));

        // Error scenarios
        try {
            shortSerializer.serialize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();

        // Typical use cases
        assertEquals("123", integerSerializer.serialize(new Integer("123")));
        assertEquals("-45", integerSerializer.serialize(new Integer("-45")));

        // Edge cases
        assertEquals("0", integerSerializer.serialize(new Integer("0")));
        assertEquals("2147483647", integerSerializer.serialize(new Integer("2147483647")));
        assertEquals("-2147483648", integerSerializer.serialize(new Integer("-2147483648")));

        // Error scenarios
        try {
            integerSerializer.serialize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        // Typical use cases
        assertEquals("123", intLikeSerializer.serialize(new Integer("123")));
        assertEquals("-45", intLikeSerializer.serialize(new Integer("-45")));

        // Edge cases
        assertEquals("0", intLikeSerializer.serialize(new Integer("0")));
        assertEquals("2147483647", intLikeSerializer.serialize(new Integer("2147483647")));
        assertEquals("-2147483648", intLikeSerializer.serialize(new Integer("-2147483648")));

        // Error scenarios
        try {
            intLikeSerializer.serialize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();

        // Typical use cases
        assertEquals("123", longSerializer.serialize(new Long("123")));
        assertEquals("-45", longSerializer.serialize(new Long("-45")));

        // Edge cases
        assertEquals("0", longSerializer.serialize(new Long("0")));
        assertEquals("9223372036854775807", longSerializer.serialize(new Long("9223372036854775807")));
        assertEquals("-9223372036854775808", longSerializer.serialize(new Long("-9223372036854775808")));

        // Error scenarios
        try {
            longSerializer.serialize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();

        // Typical use cases
        assertEquals("123.0", floatSerializer.serialize(new Float("123.0")));
        assertEquals("-45.67", floatSerializer.serialize(new Float("-45.67")));

        // Edge cases
        assertEquals("0.0", floatSerializer.serialize(new Float("0.0")));
        assertEquals("1.4E-45", floatSerializer.serialize(new Float("1.4E-45")));
        assertEquals("3.4028235E38", floatSerializer.serialize(new Float("3.4028235E38")));

        // Error scenarios
        try {
            floatSerializer.serialize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();

        // Typical use cases
        assertEquals("123.0", doubleSerializer.serialize(new Double("123.0")));
        assertEquals("-45.67", doubleSerializer.serialize(new Double("-45.67")));

        // Edge cases
        assertEquals("0.0", doubleSerializer.serialize(new Double("0.0")));
        assertEquals("4.9E-324", doubleSerializer.serialize(new Double("4.9E-324")));
        assertEquals("1.7976931348623157E308", doubleSerializer.serialize(new Double("1.7976931348623157E308")));

        // Error scenarios
        try {
            doubleSerializer.serialize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }
}