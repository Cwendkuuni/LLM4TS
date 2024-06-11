package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        BigDecimal value = new BigDecimal("10.5");
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        BigInteger value = new BigInteger("10");
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_Integer() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Integer value = 10;
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_Long() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Long value = 10L;
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_Double() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Double value = 10.5;
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_Float() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Float value = 10.5f;
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_Byte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Byte value = 10;
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_Short() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Short value = 10;
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testSerialize_Fallback() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator g = null; // provide a valid JsonGenerator instance
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Number value = 10;
        
        serializer.serialize(value, g, provider);
        
        // Assert the serialized result is as expected
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null; // provide a valid SerializerProvider instance
        Type typeHint = null; // provide a valid Type instance
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        // Assert the schema is as expected
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = null; // provide a valid JsonFormatVisitorWrapper instance
        JavaType typeHint = null; // provide a valid JavaType instance
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Assert the visit is successful
    }
}