package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {
    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);
    
    @Test
    public void testSerialize() throws IOException {
        // Given
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        // When
        numberSerializer.serialize(new BigDecimal("10"), g, provider);
        numberSerializer.serialize(new BigInteger("10"), g, provider);
        numberSerializer.serialize(10, g, provider);
        numberSerializer.serialize(10L, g, provider);
        numberSerializer.serialize(10.0, g, provider);
        numberSerializer.serialize(10f, g, provider);
        numberSerializer.serialize((byte) 10, g, provider);
        numberSerializer.serialize((short) 10, g, provider);
        numberSerializer.serialize("10", g, provider);
        
        // Then
        // Verify the result
    }
    
    @Test
    public void testGetSchema() {
        // Given
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = null;
        
        // When
        JsonNode result = numberSerializer.getSchema(provider, typeHint);
        
        // Then
        // Verify the result
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Given
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = null;
        
        // When
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        // Then
        // Verify the result
    }
}