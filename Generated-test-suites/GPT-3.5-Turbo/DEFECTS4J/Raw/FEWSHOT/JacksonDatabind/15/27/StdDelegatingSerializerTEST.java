package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class StdDelegatingSerializerTest {
    @Mock
    private Converter<?, ?> converter;

    @Mock
    private JavaType delegateType;

    @Mock
    private JsonSerializer<?> delegateSerializer;

    @Mock
    private SerializerProvider provider;

    @Mock
    private BeanProperty property;

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() {
        //Initialize the serializer with mocks
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testResolve() throws Exception {
        ResolvableSerializer resolvableDelegateSerializer = mock(ResolvableSerializer.class);
        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        when(delegateSerializer).thenReturn(resolvableDelegateSerializer);
        serializer.resolve(provider);
        verify(resolvableDelegateSerializer).resolve(provider);
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonSerializer<?> secondaryDelSer = mock(JsonSerializer.class);
        when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(provider.handleSecondaryContextualization(delegateSerializer, property)).thenReturn(secondaryDelSer);
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertEquals(secondaryDelSer, result);
    }

    @Test
    public void testSerialize() throws Exception {
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        JsonGenerator gen = mock(JsonGenerator.class);
        _delegateSerializer.serialize(delegateValue, gen, provider);
        verify(_delegateSerializer).serialize(delegateValue, gen, provider);

        serializer.serialize(value, gen, provider);
        verify(gen).defaultSerializeNull();

        //Test with null delegate value
        when(converter.convert(value)).thenReturn(null);
        serializer.serialize(value, gen, provider);
        verify(gen, times(2)).defaultSerializeNull();
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        JsonGenerator gen = mock(JsonGenerator.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        _delegateSerializer.serializeWithType(delegateValue, gen, provider, typeSer);
        verify(_delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);

        serializer.serializeWithType(value, gen, provider, typeSer);
        verify(gen).setType(typeSer);
    }

    @Test
    public void testIsEmpty() throws Exception {
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        when(_delegateSerializer.isEmpty(delegateValue)).thenReturn(true);
        boolean result = serializer.isEmpty(value);
        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithProvider() throws Exception {
        Object value = new Object();
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        when(_delegateSerializer.isEmpty(provider, delegateValue)).thenReturn(true);
        boolean result = serializer.isEmpty(provider, value);
        assertEquals(true, result);
    }

    @Test
    public void testGetSchema() throws Exception {
        Type typeHint = mock(Type.class);
        JsonNode schemaNode = mock(JsonNode.class);
        when(_delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) _delegateSerializer).getSchema(provider, typeHint)).thenReturn(schemaNode);

        JsonNode result = serializer.getSchema(provider, typeHint);
        assertEquals(schemaNode, result);
    }

    @Test
    public void testGetSchemaWithOptional() throws Exception {
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        JsonNode schemaNode = mock(JsonNode.class);
        when(_delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) _delegateSerializer).getSchema(provider, typeHint, isOptional)).thenReturn(schemaNode);

        JsonNode result = serializer.getSchema(provider, typeHint, isOptional);
        assertEquals(schemaNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        _delegateSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(_delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(2)).expectAnyFormat(typeHint);
    }

    @Test
    public void testConvertValue() throws Exception {
        Object value = new Object();
        Object convertedValue = new Object();
        when(converter.convert(value)).thenReturn(convertedValue);

        Object result = serializer.convertValue(value);
        assertEquals(convertedValue, result);
    }
}