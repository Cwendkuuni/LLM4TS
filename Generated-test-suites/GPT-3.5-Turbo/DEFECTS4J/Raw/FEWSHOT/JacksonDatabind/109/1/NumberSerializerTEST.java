package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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

import org.junit.Before;
import org.junit.Test;

public class NumberSerializerTest {
    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        try {
            JsonSerializer<?> result = numberSerializer.createContextual(provider, property);
            assertTrue(result instanceof NumberSerializer);
        } catch (JsonMappingException e) {
            assertFalse(true);
        }
    }

    @Test
    public void testSerialize() {
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        try {
            numberSerializer.serialize(new BigDecimal("10.22"), generator, provider);
            numberSerializer.serialize(new BigInteger("1000"), generator, provider);
            numberSerializer.serialize(new Long(100L), generator, provider);
            numberSerializer.serialize(new Double(10.22), generator, provider);
            numberSerializer.serialize(new Float(10.22), generator, provider);
            numberSerializer.serialize(new Integer(100), generator, provider);
            numberSerializer.serialize(new Byte((byte) 10), generator, provider);
            numberSerializer.serialize(new Short((short) 10), generator, provider);
            numberSerializer.serialize(null, generator, provider);
        } catch (IOException e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        assertTrue(schema.isNumber());
        assertTrue(schema.isNumberType());
        assertTrue(schema.isInt());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        try {
            numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            assertFalse(true);
        }
    }
}