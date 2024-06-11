
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    
    @Test
    public void testSerializeBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider.Impl()
                                        .createInstance(mapper.getSerializationConfig(), new SerializerFactory());
        BigDecimal value = new BigDecimal("123.45");
        StringBuilder json = new StringBuilder();
        numberSerializer.serialize(value, mapper.getFactory().createGenerator(json), provider);
        assertEquals("123.45", json.toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider.Impl()
                                        .createInstance(mapper.getSerializationConfig(), new SerializerFactory());
        BigInteger value = new BigInteger("123456789012345678901234567890");
        StringBuilder json = new StringBuilder();
        numberSerializer.serialize(value, mapper.getFactory().createGenerator(json), provider);
        assertEquals("123456789012345678901234567890", json.toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider.Impl()
                                        .createInstance(mapper.getSerializationConfig(), new SerializerFactory());
        Long value = 123L;
        StringBuilder json = new StringBuilder();
        numberSerializer.serialize(value, mapper.getFactory().createGenerator(json), provider);
        assertEquals("123", json.toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider.Impl()
                                        .createInstance(mapper.getSerializationConfig(), new SerializerFactory());
        Double value = 123.45;
        StringBuilder json = new StringBuilder();
        numberSerializer.serialize(value, mapper.getFactory().createGenerator(json), provider);
        assertEquals("123.45", json.toString());
    }

    @Test
    public void testSerializeInvalidNumberType() {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider.Impl()
                                        .createInstance(mapper.getSerializationConfig(), new SerializerFactory());
        Byte value = 1;
        StringBuilder json = new StringBuilder();
        try {
            numberSerializer.serialize(value, mapper.getFactory().createGenerator(json), provider);
            fail("Should have thrown an exception for invalid Number type");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("not a valid Number type"));
        }
    }

    // Add more test cases to cover edge cases and exception handling scenarios
    
}
