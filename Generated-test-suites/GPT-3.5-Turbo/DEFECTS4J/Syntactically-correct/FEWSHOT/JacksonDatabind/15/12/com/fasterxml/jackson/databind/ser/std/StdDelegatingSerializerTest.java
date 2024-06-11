package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StdDelegatingSerializerTest {

    @Test
    public void testConstructor_withConverter() {
        Converter<?, ?> converter = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
        assertTrue(serializer.getConverter() instanceof Converter);
    }

    @Test
    public void testConstructor_withGenericConverter() {
        Converter<?, ?> converter = null;
        Class<String> cls = String.class;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(cls, converter);

        assertNull(serializer._delegateType);
        assertNull(serializer._delegateSerializer);
        assertTrue(serializer.getConverter() instanceof Converter);
    }

    @Test
    public void testConstructor_withConverterAndDelegate() {
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        assertEquals(delegateType, serializer._delegateType);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
        assertTrue(serializer.getConverter() instanceof Converter);
    }

    @Test
    public void testWithDelegate() {
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        StdDelegatingSerializer newSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);

        assertEquals(delegateType, newSerializer._delegateType);
        assertEquals(delegateSerializer, newSerializer._delegateSerializer);
        assertTrue(newSerializer.getConverter() instanceof Converter);
    }

    @Test
    public void testResolve() {
        Converter<?, ?> converter = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = null;

        serializer.resolve(provider);
    }

    @Test
    public void testCreateContextual() {
        Converter<?, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        SerializerProvider provider = null;
        BeanProperty property = null;

        JsonSerializer<?> newSerializer = serializer.createContextual(provider, property);

        assertTrue(newSerializer instanceof JsonSerializer);
    }

    @Test
    public void testSerialize() {
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        serializer.serialize(value, gen, provider);
    }

    @Test
    public void testSerializeWithType() {
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        serializer.serializeWithType(value, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty() {
        Object value = null;
        SerializerProvider prov = null;
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        assertTrue(serializer.isEmpty(prov, value));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        serializer.getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        serializer.getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() {
        Object value = null;
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object convertedValue = serializer.convertValue(value);

        assertNull(convertedValue);
    }
}