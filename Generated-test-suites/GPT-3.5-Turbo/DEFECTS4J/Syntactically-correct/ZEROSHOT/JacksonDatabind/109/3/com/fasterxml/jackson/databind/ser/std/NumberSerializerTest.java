package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import org.junit.Assert;
import org.junit.Test;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testCreateContextual() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Assert.assertNotNull(numberSerializer);
        JsonSerializer<?> jsonSerializer = numberSerializer.createContextual(null, null);
        Assert.assertEquals(numberSerializer, jsonSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Assert.assertNotNull(numberSerializer);
        JsonGenerator jsonGenerator = null;  // Provide your own JsonGenerator object
        SerializerProvider serializerProvider = null; // Provide your own SerializerProvider object
        numberSerializer.serialize(null, jsonGenerator, serializerProvider);
        // Perform assertion for serialization
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Assert.assertNotNull(numberSerializer);
        SerializerProvider serializerProvider = null; // Provide your own SerializerProvider object
        Type typeHint = null; // Provide your own Type object
        JsonNode jsonNode = numberSerializer.getSchema(serializerProvider, typeHint);
        // Perform assertion for schema
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Assert.assertNotNull(numberSerializer);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = null; // Provide your own JsonFormatVisitorWrapper object
        JavaType javaType = null; // Provide your own JavaType object
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        // Perform assertion for json format visitor
    }
}