package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Test
    public void testEmptyConstructor() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Assert.assertNull(serializer.getDelegateType());
        Assert.assertNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testConstructorWithConverter() {
        Converter<Object, Serializable> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertEquals(Object.class, serializer.getDelegateType());
        Assert.assertNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testConstructorWithClassAndConverter() {
        Converter<Object, Serializable> converter = mock(Converter.class);
        Class<Serializable> clazz = Serializable.class;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(clazz, converter);
        Assert.assertEquals(clazz, serializer.getDelegateType());
        Assert.assertNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testConstructorWithConverterDelegateTypeAndDelegateSerializer() {
        Converter<Object, Serializable> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertEquals(delegateType, serializer.getDelegateType());
        Assert.assertEquals(delegateSerializer, serializer.getDelegateSerializer());
    }

    @Test
    public void testConvertValueNull() {
        Converter<Object, Serializable> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = null;
        Object result = serializer.convertValue(value);
        Assert.assertNull(result);
    }

    @Test
    public void testConvertValueNonSerializable() {
        Converter<Object, String> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = new Object();
        Object result = serializer.convertValue(value);
        Assert.assertNotNull(result);
        Assert.assertEquals("", result.toString());
    }

    @Test
    public void testConvertValueString() {
        Converter<Object, String> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = "test";
        Object result = serializer.convertValue(value);
        Assert.assertEquals("test", result.toString());
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, IOException {
        Converter<Object, Serializable> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Object value = null;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(value, generator, provider);
        verify(generator, times(1)).writeNull();
    }

    @Test
    public void testSerializeWithTypeNull() throws JsonGenerationException, IOException {
        Converter<Object, Serializable> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Object value = null;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        serializer.serializeWithType(value, generator, provider, typeSerializer);
        verify(typeSerializer, never()).writeTypePrefix(any());
        verify(typeSerializer, never()).writeTypeSuffix(any());
        verify(generator, never()).writeStartObject();
        verify(generator, never()).writeEndObject();
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        Converter<Object, String> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Object value = "test";
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(value, generator, provider);
        verify(delegateSerializer, times(1)).serialize(eq(value), any(), any());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Converter<Object, Serializable> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.resolve(provider);
        if (delegateSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer) delegateSerializer).resolve(provider);
        }
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Converter<Object, Serializable> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testGetConverter() {
        Converter<Object, Serializable> converter = mock(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Converter<Object, ?> result = serializer.getConverter();
        Assert.assertEquals(converter, result);
    }

    @Test
    public void testGetDelegatee() {
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, delegateSerializer);
        JsonSerializer<?> result = serializer.getDelegatee();
        Assert.assertEquals(delegateSerializer, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Converter<Object, Serializable> converter = mock(Converter.class);
        JavaType delegateType = mock(JavaType.class);
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonNode result = serializer.acceptJsonFormatVisitor(visitor, delegateType);
        Assert.assertNull(result);
    }

    @Test
    public void testGetSchemaNull() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, delegateSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        JsonNode result = serializer.getSchema(provider, typeHint);
        Assert.assertNull(result);
    }

    @Test
    public void testGetSchemaWithDelegateSerializerSchemaAware() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        when(delegateSerializer.getSchema(any(), any())).thenReturn(JsonNodeFactory.instance.objectNode());
        JsonSerializer<Object> delegateSerializerSchemaAware = mock(JsonSerializer.class);
        when(delegateSerializerSchemaAware.getSchema(any(), any())).thenReturn(JsonNodeFactory.instance.objectNode());
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, delegateSerializerSchemaAware);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        JsonNode result = serializer.getSchema(provider, typeHint);
        Assert.assertNotNull(result);
        Assert.assertEquals(JsonNodeFactory.instance.objectNode(), result);
    }

    @Test
    public void testGetSchemaWithDelegateSerializerNotSchemaAware() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = mock(JsonSerializer.class);
        JsonSerializer<Object> delegateSerializerSchemaAware = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, delegateSerializer);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Object.class;
        JsonNode result = serializer.getSchema(provider, typeHint);
        Assert.assertEquals(JsonNodeFactory.instance.nullNode(), result);
    }
}