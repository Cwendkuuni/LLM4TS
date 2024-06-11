
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider prov = null;
        BeanProperty property = null;
        JsonSerializer<?> result = serializer.createContextual(prov, property);
        assertEquals(serializer, result);
    }

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator g = null;
        SerializerProvider provider = null;

        // Test for BigDecimal
        BigDecimal value1 = new BigDecimal("123.45");
        serializer.serialize(value1, g, provider);
        // Write assertion code here

        // Test for BigInteger
        BigInteger value2 = new BigInteger("123456789");
        serializer.serialize(value2, g, provider);
        // Write assertion code here

        // Test for Long
        Long value3 = 12345L;
        serializer.serialize(value3, g, provider);
        // Write assertion code here

        // Test for Double
        Double value4 = 123.45;
        serializer.serialize(value4, g, provider);
        // Write assertion code here

        // Test for Float
        Float value5 = 123.45f;
        serializer.serialize(value5, g, provider);
        // Write assertion code here

        // Test for Integer
        Integer value6 = 123;
        serializer.serialize(value6, g, provider);
        // Write assertion code here

        // Test for Byte
        Byte value7 = 123;
        serializer.serialize(value7, g, provider);
        // Write assertion code here

        // Test for Short
        Short value8 = 123;
        serializer.serialize(value8, g, provider);
        // Write assertion code here

        // Test for unsupported type
        // Write assertion code here
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonNode result = serializer.getSchema(provider, typeHint);
        // Write assertion code here
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Write assertion code here
    }
}
