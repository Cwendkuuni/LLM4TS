package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize_nullValue_shouldCallDefaultSerializeNull() throws IOException {
        // arrange
        Converter<Object, ?> converterMock = mock(Converter.class);
        JsonSerializer<Object> delegateSerializerMock = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converterMock, null, delegateSerializerMock);

        Object value = null;
        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        // act
        serializer.serialize(value, genMock, providerMock);

        // assert
        verify(providerMock).defaultSerializeNull(genMock);
    }

    @Test
    public void testSerialize_notNullValue_shouldCallDelegateSerialize() throws IOException {
        // arrange
        Converter<Object, ?> converterMock = mock(Converter.class);
        JsonSerializer<Object> delegateSerializerMock = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converterMock, null, delegateSerializerMock);

        Object value = new Object();
        Object delegateValue = new Object();
        when(converterMock.convert(value)).thenReturn(delegateValue);

        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        // act
        serializer.serialize(value, genMock, providerMock);

        // assert
        verify(delegateSerializerMock).serialize(delegateValue, genMock, providerMock);
    }

    @Test
    public void testSerializeWithType_notNullValue_shouldCallDelegateSerializeWithType() throws IOException {
        // arrange
        Converter<Object, ?> converterMock = mock(Converter.class);
        JsonSerializer<Object> delegateSerializerMock = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converterMock, null, delegateSerializerMock);

        Object value = new Object();
        Object delegateValue = new Object();
        when(converterMock.convert(value)).thenReturn(delegateValue);

        JsonGenerator genMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);
        TypeSerializer typeSerMock = mock(TypeSerializer.class);

        // act
        serializer.serializeWithType(value, genMock, providerMock, typeSerMock);

        // assert
        verify(delegateSerializerMock).serializeWithType(delegateValue, genMock, providerMock, typeSerMock);
    }

    @Test
    public void testIsEmpty_notNullValue_shouldCallDelegateIsEmpty() {
        // arrange
        Converter<Object, ?> converterMock = mock(Converter.class);
        JsonSerializer<Object> delegateSerializerMock = mock(JsonSerializer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converterMock, null, delegateSerializerMock);

        Object value = new Object();
        Object delegateValue = new Object();
        when(converterMock.convert(value)).thenReturn(delegateValue);
        when(delegateSerializerMock.isEmpty(delegateValue)).thenReturn(true);

        SerializerProvider provMock = mock(SerializerProvider.class);

        // act
        boolean result = serializer.isEmpty(provMock, value);

        // assert
        verify(delegateSerializerMock).isEmpty(provMock, delegateValue);
        assertTrue(result);
    }

    @Test
    public void testGetSchema_delegateSerializerImplementsSchemaAware_shouldCallGetSchemaOnDelegate() throws JsonMappingException {
        // arrange
        Converter<Object, ?> converterMock = mock(Converter.class);
        JsonSerializer<Object> delegateSerializerMock = mock(JsonSerializer.class);
        SchemaAware schemaAwareMock = mock(SchemaAware.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converterMock, null, delegateSerializerMock);

        Object value = new Object();
        Object delegateValue = new Object();
        JavaType typeHint = mock(JavaType.class);
        when(converterMock.convert(value)).thenReturn(delegateValue);
        when(delegateSerializerMock instanceof SchemaAware).thenReturn(true);
        when(delegateSerializerMock).thenReturn(schemaAwareMock);
        when(schemaAwareMock.getSchema(any(), any())).thenReturn(mock(JsonNode.class));
        SerializerProvider providerMock = mock(SerializerProvider.class);
        Type typeHintMock = mock(Type.class);

        // act
        JsonNode result = serializer.getSchema(providerMock, typeHintMock);

        // assert
        verify(schemaAwareMock).getSchema(providerMock, typeHintMock);
        assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_shouldCallAcceptJsonFormatVisitorOnDelegate() throws JsonMappingException {
        // arrange
        Converter<Object, ?> converterMock = mock(Converter.class);
        JsonSerializer<Object> delegateSerializerMock = mock(JsonSerializer.class);
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converterMock, null, delegateSerializerMock);

        Object value = new Object();
        Object delegateValue = new Object();
        when(converterMock.convert(value)).thenReturn(delegateValue);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        // act
        serializer.acceptJsonFormatVisitor(visitorMock, typeHint);

        // assert
        verify(delegateSerializerMock).acceptJsonFormatVisitor(visitorMock, typeHint);
    }
}