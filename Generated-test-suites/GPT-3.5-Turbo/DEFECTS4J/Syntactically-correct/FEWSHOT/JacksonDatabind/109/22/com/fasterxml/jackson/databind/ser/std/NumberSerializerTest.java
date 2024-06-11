package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.impl.ToStringSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class NumberSerializerTest {

    @Test
    public void testConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertTrue(numberSerializer instanceof StdScalarSerializer);
        assertTrue(numberSerializer instanceof ContextualSerializer);
        assertEquals(Number.class, numberSerializer.handledType());
        assertTrue(numberSerializer._isInt);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider mockSerializerProvider = null;
        BeanProperty mockBeanProperty = null;
        JsonSerializer<?> result = numberSerializer.createContextual(mockSerializerProvider, mockBeanProperty);
        assertEquals(numberSerializer, result);
    }

    @Test
    public void testSerialize() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator mockJsonGenerator = null;
        SerializerProvider mockSerializerProvider = null;
        
        numberSerializer.serialize(Long.valueOf(10), mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(Double.valueOf(10.0), mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(Float.valueOf(10.0f), mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(Integer.valueOf(10), mockJsonGenerator, mockSerializerProvider);
        assertNull(numberSerializer.serialize("abc", mockJsonGenerator, mockSerializerProvider));
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider mockSerializerProvider = null;
        Type mockType = null;
        JsonNode result = numberSerializer.getSchema(mockSerializerProvider, mockType);
        assertTrue(result.isObject());
        assertTrue(result.get("type").isTextual());
        assertEquals("integer", result.get("type").asText());
        assertTrue(result.get("required").isBoolean());
        assertTrue(result.get("required").booleanValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = null;
        JavaType mockJavaType = null;
        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
    }

}