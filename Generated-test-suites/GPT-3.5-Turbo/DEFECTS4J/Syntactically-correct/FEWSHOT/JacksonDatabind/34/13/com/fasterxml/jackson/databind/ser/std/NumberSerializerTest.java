package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {
    
    // Test serialize() method
    @Test
    public void testSerialize() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        BigDecimal bigDecimal = new BigDecimal("1234.5678");
        serializer.serialize(bigDecimal, generator, provider);
        // Assertion
        
        BigInteger bigInteger = new BigInteger("1234567890");
        serializer.serialize(bigInteger, generator, provider);
        // Assertion
        
        Integer integer = 100;
        serializer.serialize(integer, generator, provider);
        // Assertion
        
        Long longValue = 1000L;
        serializer.serialize(longValue, generator, provider);
        // Assertion
        
        Double doubleValue = 12.34;
        serializer.serialize(doubleValue, generator, provider);
        // Assertion
        
        Float floatValue = 3.14f;
        serializer.serialize(floatValue, generator, provider);
        // Assertion
        
        Byte byteValue = 1;
        serializer.serialize(byteValue, generator, provider);
        // Assertion
        
        Short shortValue = 10;
        serializer.serialize(shortValue, generator, provider);
        // Assertion
        
        List<Number> list = Arrays.asList(bigDecimal, bigInteger, integer, longValue, doubleValue, floatValue, byteValue, shortValue);
        for (Number num : list) {
            serializer.serialize(num, generator, provider);
            // Assertion
        }
    }
    
    // Test getSchema() method
    @Test
    public void testGetSchema() {
        NumberSerializer serializer = NumberSerializer.instance;
        SerializerProvider provider = null;
        JavaType typeHint = null;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        // Assertion
        
        assertTrue(schema.isObject());
        // Assertion
    }
    
    // Test acceptJsonFormatVisitor() method
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonSerializer<?> jsonSerializer = null;
        JavaType typeHint = null;
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Assertion
    }
}