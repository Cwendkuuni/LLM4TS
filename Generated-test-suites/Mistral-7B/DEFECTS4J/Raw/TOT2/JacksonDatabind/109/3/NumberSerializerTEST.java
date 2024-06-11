package com.example.junit;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {
    @Test
    public void testSerializeNumber() {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = null; // Replace this with a mock or real JsonGenerator for testing

        Number number = 123; // Integer
        String result = serializer.serialize(number, generator, null); // Test serialization of an integer
        assertEquals("123", result);

        number = Long.MIN_VALUE; // Long with minimum value
        try {
            serializer.serialize(number, generator, null); // Test serialization of the minimum long value
            fail("Expected IOE");
        } catch (IOException e) {
            // Expected exception
        }

        number = Long.MAX_VALUE; // Long with maximum value
        try {
            serializer.serialize(number, generator, null); // Test serialization of the maximum long value
            fail("Expected IOE");
        } catch (IOException e) {
            // Expected exception
        }

        number = new BigDecimal("123.45E+10"); // BigDecimal with exponent
        String expectedResult = "1.2345E+11";
        String result = serializer.serialize(number, generator, null); // Test serialization of a BigDecimal with exponent
        assertEquals(expectedResult, result);

        number = new BigInteger("1234567890123456789012345678901234567890"); // BigInteger with leading zeros
        String expectedResult2 = "1234567890123456789012345678901234567890";
        String result2 = serializer.serialize(number, generator, null); // Test serialization of a BigInteger with leading zeros
        assertEquals(expectedResult2, result2);

        // Test serialization of null
        number = null;
        String result7 = serializer.serialize(number, generator, null); // Test serialization of null
        assertEquals("null", result7);

        // Add test cases for more edge cases and exception handling scenarios here
    }
}