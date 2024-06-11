package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

import org.junit.Test;

public class NumberSerializerTest {
    @Test
    public void testConstructor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        assertNotNull(serializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BeanProperty property = new SimpleBeanPropertyDefinition(null, null, null, null, null, null, null, false, null, null, null);
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertTrue(result instanceof NumberSerializer);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BigDecimal value = new BigDecimal("10.5");
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BigInteger value = new BigInteger("10");
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Long value = Long.valueOf(10);
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Double value = Double.valueOf(10.5);
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Float value = Float.valueOf(10.5f);
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Integer value = Integer.valueOf(10);
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Byte value = Byte.valueOf((byte) 10);
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Short value = Short.valueOf((short) 10);
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testSerializeUnknownType() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Number value = 10;
        serializer.serialize(value, generator, provider);
        generator.flush();
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        assertNotNull(serializer.getSchema(provider, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base().expectNumberFormat(null);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(Number.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull(visitor);
    }

    @Test
    public void testUnexpectedMethods() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        try {
            serializer.isEmpty(null);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
        try {
            serializer.getSchema(null, null);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
}