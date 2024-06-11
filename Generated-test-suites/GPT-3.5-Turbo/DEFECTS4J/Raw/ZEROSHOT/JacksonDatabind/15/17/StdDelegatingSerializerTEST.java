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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private Converter<Object, ?> converter;
    private JavaType delegateType;
    private JsonSerializer<?> delegateSerializer;
    private JsonSerializer<?> contextDelegateSerializer;
    private SerializerProvider provider;
    private JsonGenerator generator;

    @Before
    public void setup() {
        converter = mock(Converter.class);
        delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);
        contextDelegateSerializer = mock(JsonSerializer.class);
        provider = mock(SerializerProvider.class);
        generator = mock(JsonGenerator.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ResolvableSerializer delegate = mock(ResolvableSerializer.class);
        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        when(delegateSerializer).thenReturn(delegate);
        serializer.resolve(provider);
        verify(delegate).resolve(provider);
    }

    @Test
    public void testCreateContextualWithDelegateSerializer() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer<?> delSer = serializer._delegateSerializer;
        when(provider.findValueSerializer(delegateType)).thenReturn(contextDelegateSerializer);
        when(contextDelegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(provider.handleSecondaryContextualization(contextDelegateSerializer, property)).thenReturn(contextDelegateSerializer);
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertSame(contextDelegateSerializer, result);
        assertSame(delSer, serializer._delegateSerializer);
    }

    @Test
    public void testCreateContextualWithoutDelegateSerializer() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer<?> delSer = serializer._delegateSerializer;
        when(provider.findValueSerializer(delegateType)).thenReturn(contextDelegateSerializer);
        when(contextDelegateSerializer instanceof ContextualSerializer).thenReturn(false);
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertSame(serializer, result);
        assertSame(delSer, serializer._delegateSerializer);
    }

    @Test
    public void testGetConverter() {
        Converter<Object, ?> result = serializer.getConverter();
        assertSame(converter, result);
    }

    @Test
    public void testGetDelegatee() {
        JsonSerializer<?> result = serializer.getDelegatee();
        assertSame(delegateSerializer, result);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer.serialize(value, generator, provider);
        verify(delegateSerializer).serialize(delegateValue, generator, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object value = new Object();
        Object delegateValue = new Object();
        TypeSerializer typeSer = mock(TypeSerializer.class);
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer.serializeWithType(value, generator, provider, typeSer);
        verify(delegateSerializer).serializeWithType(delegateValue, generator, provider, typeSer);
    }

    @Test
    public void testIsEmpty() {
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);
        boolean result = serializer.isEmpty(provider, value);
        assertTrue(result);
    }

    @Test
    public void testGetSchemaWithSchemaAwareDelegateSerializer() throws JsonMappingException {
        Type typeHint = mock(Type.class);
        SchemaAware schemaAwareDelegate = mock(SchemaAware.class);
        JsonNode expected = mock(JsonNode.class);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint)).thenReturn(expected);
        JsonNode result = serializer.getSchema(provider, typeHint);
        assertSame(expected, result);
    }

    @Test
    public void testGetSchemaWithoutSchemaAwareDelegateSerializer() throws JsonMappingException {
        Type typeHint = mock(Type.class);
        JsonNode expected = mock(JsonNode.class);
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);
        when(serializer.getSchema(provider, typeHint)).thenReturn(expected);
        JsonNode result = serializer.getSchema(provider, typeHint);
        assertSame(expected, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() {
        Object value = new Object();
        Object expected = new Object();
        when(converter.convert(value)).thenReturn(expected);
        Object result = serializer.convertValue(value);
        assertSame(expected, result);
    }

}