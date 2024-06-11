package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() {
        Converter<Object, String> converter = input -> input.toString();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Object value = 123;
        Object convertedValue = serializer.convertValue(value);
        assertEquals("123", convertedValue);
    }

    @Test
    public void testSerialize() throws IOException {
        Converter<Object, String> converter = input -> input.toString();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(123, generator, provider);
        verify(generator).writeString("123");
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Converter<Object, String> converter = input -> input.toString();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        serializer.serializeWithType(123, generator, provider, typeSerializer);
        verify(generator).writeString("123");
    }

    @Test(expected = IOException.class)
    public void testSerializeWithNullValue() throws IOException {
        Converter<Object, String> converter = input -> input.toString();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(null, generator, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Converter<Object, String> converter = input -> input.toString();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchemaIsOptional() throws JsonMappingException {
        Converter<Object, String> converter = input -> input.toString();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        SerializerProvider provider = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.getSchema(provider, typeHint, true);
    }

}