package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    private StdDelegatingSerializer serializer;
    private Converter<Object, ?> converter;
    private JavaType delegateType;
    private JsonSerializer<?> delegateSerializer;
    private JsonSerializer<Object> mockDelegateSerializer;
    private Object value;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private TypeSerializer typeSer;
    private BeanProperty property;

    @Before
    public void setup() {
        converter = mock(Converter.class);
        delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);
        mockDelegateSerializer = mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        value = new Object();
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        typeSer = mock(TypeSerializer.class);
        property = mock(BeanProperty.class);
    }

    @Test
    public void testSerialize() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serialize(value, gen, provider);

        assertEquals(delegateValue, serializer.serialize(value, gen, provider));
    }

    @Test
    public void testSerialize_NullValue() throws IOException {
        when(converter.convert(null)).thenReturn(null);
        when(provider.getDefaultNullValueSerializer()).thenReturn(delegateSerializer);

        serializer.serialize(null, gen, provider);

        assertNull(serializer.serialize(null, gen, provider));
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serializeWithType(value, gen, provider, typeSer);

        assertEquals(delegateValue, serializer.serializeWithType(value, gen, provider, typeSer));
    }

    @Test
    public void testIsEmpty() {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(mockDelegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        assertEquals(true, serializer.isEmpty(value));

        when(mockDelegateSerializer.isEmpty(delegateValue)).thenReturn(false);

        assertEquals(false, serializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty_WithSerializerProvider() {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        when(mockDelegateSerializer.isEmpty(provider, delegateValue)).thenReturn(true);

        assertEquals(true, serializer.isEmpty(provider, value));

        when(mockDelegateSerializer.isEmpty(provider, delegateValue)).thenReturn(false);

        assertEquals(false, serializer.isEmpty(provider, value));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        JsonNode expectedSchema = mock(JsonNode.class);
        when(((SchemaAware) delegateSerializer).getSchema(provider, delegateType)).thenReturn(expectedSchema);

        JsonNode actualSchema = serializer.getSchema(provider, delegateType);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetSchema_WithIsOptional() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        JsonNode expectedSchema = mock(JsonNode.class);
        when(((SchemaAware) delegateSerializer).getSchema(provider, delegateType, true)).thenReturn(expectedSchema);

        JsonNode actualSchema = serializer.getSchema(provider, delegateType, true);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, delegateType);

        delegateSerializer.acceptJsonFormatVisitor(visitor, delegateType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConvertValue() {
        serializer.convertValue(value);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ResolvableSerializer mockResolvableSerializer = mock(ResolvableSerializer.class);
        when(mockDelegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        when(mockDelegateSerializer).thenReturn(mockResolvableSerializer);

        serializer.resolve(provider);

        mockResolvableSerializer.resolve(provider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        when(provider.findValueSerializer(delegateType)).thenReturn(mockDelegateSerializer);
        when(mockDelegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(provider.handleSecondaryContextualization(mockDelegateSerializer, property)).thenReturn(mockDelegateSerializer);
        ContextualSerializer result = serializer.createContextual(provider, property);

        assertEquals(mockDelegateSerializer, result);
    }
}