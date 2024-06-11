package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class StdKeyDeserializerTest {

    

    @Test
    public void testBooleanType() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Object result = keyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        try {
            keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("not a valid representation, problem: (java.lang.String) [type=STRING, value='invalid']"));
        }
    }

    @Test
    public void testByteType() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapperWithoutBigDecimal = new ObjectMapper().disableFeatures(SerializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        // Valid byte values
        byte validByte1 = 10;
        byte validByte2 = 127;
        byte validByte3 = -128;

        Object result1 = keyDeserializer.deserializeKey(String.valueOf(validByte1), objectMapper.getDeserializationContext());
        assertEquals(validByte1, result1);

        result1 = keyDeserializer.deserializeKey(String.valueOf(validByte2), objectMapper.getDeserializationContext());
        assertEquals(validByte2, result1);

        result1 = keyDeserializer.deserializeKey(String.valueOf(validByte3), objectMapper.getDeserializationContext());
        assertEquals(validByte3, result1);

        // Invalid byte value
        byte invalidByte = 129;

        try {
            result1 = keyDeserializer.deserializeKey(String.valueOf(invalidByte), objectMapper.getDeserializationContext());
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("overflow, value can not be represented as 8-bit value"));
        }

        // Invalid byte value with ObjectMapper without BigDecimal
        try {
            result1 = keyDeserializer.deserializeKey(String.valueOf(invalidByte), objectMapperWithoutBigDecimal.getDeserializationContext());
            fail();
        } catch (JsonProcessingException e) {
            assertFalse(e.getCause() instanceof IOException);
            assertTrue(e.getMessage().startsWith("not a valid representation, problem: (java.lang.String) [type=STRING, value='129']"));
        }
    }

    // Add more test cases for short, char, int, long, float, double, Locale, UUID, URI, URL, Class, and Currency types

    
}