package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

public class NumberSerializerTest {
    @Test
    public void testCreateContextual() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        BeanProperty property = null;

        try {
            JsonSerializer<?> contextualSerializer = serializer.createContextual(null, property);
            assertTrue(contextualSerializer instanceof NumberSerializer);
        } catch (JsonMappingException e) {
            fail();
        }
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator g = null;
        SerializerProvider provider = null;
        NumberSerializer serializer = new NumberSerializer(Number.class);

        try {
            serializer.serialize(new BigDecimal(10), g, provider);
            serializer.serialize(new BigInteger("10"), g, provider);
            serializer.serialize(new Long(10), g, provider);
           (serializer.serialize(new Double(10), g, provider));
            serializer.serialize(new Float(10), g, provider);
            serializer.serialize(new Integer(10), g, provider);
            serializer.serialize(Byte.valueOf("10"), g, provider);
            serializer.serialize(Short.valueOf("10"), g, provider);
            serializer.serialize(null, g, provider);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null;

        try {
            JsonNode schema = serializer.getSchema(provider, Number.class);
            assertEquals("number", schema.get("type"));
            assertTrue(schema.get("required").asBoolean());
        } catch (JsonMappingException e) {
            fail();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        NumberSerializer serializer = new NumberSerializer(Number.class);

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail();
        }
    }
}