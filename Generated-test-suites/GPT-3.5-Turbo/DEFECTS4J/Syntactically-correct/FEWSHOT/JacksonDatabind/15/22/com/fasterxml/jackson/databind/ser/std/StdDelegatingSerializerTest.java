package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    @Mock
    private Converter<Object, Object> converter;
    @Mock
    private JsonSerializer<Object> delegateSerializer;
    @Mock
    private SerializerProvider provider;
    @Mock
    private BeanProperty property;
    @Mock
    private JsonGenerator gen;
    @Mock
    private TypeSerializer typeSer;
    @Mock
    private Object value;
    @Mock
    private Object delegateValue;
    @Mock
    private JavaType delegateType;

    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serializer = mock(StdDelegatingSerializer.class, CALLS_REAL_METHODS);
        serializer._converter = converter;
        serializer._delegateSerializer = delegateSerializer;
        serializer._delegateType = delegateType;
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
    public void testCreateContextual() throws JsonMappingException {
        when(serializer.getClass()).thenReturn(StdDelegatingSerializer.class);
        when(property.getType()).thenReturn(delegateType);
        when(serializer.withDelegate(converter, delegateType, delegateSerializer)).thenReturn(serializer);

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextualWithSecondaryContextualization() throws JsonMappingException {
        when(property.getType()).thenReturn(delegateType);
        ContextualSerializer contextualDelegate = mock(ContextualSerializer.class);
        when(serializer._delegateSerializer).thenReturn(contextualDelegate);
        when(contextualDelegate.createContextual(provider, property)).thenReturn(delegateSerializer);

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(delegateSerializer, result);
    }

    @Test
    public void testSerializeNonNull() throws IOException {
        when(serializer.convertValue(value)).thenReturn(delegateValue);

        serializer.serialize(value, gen, provider);

        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void testSerializeNull() throws IOException {
        when(serializer.convertValue(null)).thenReturn(null);

        serializer.serialize(null, gen, provider);

        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        when(serializer.convertValue(value)).thenReturn(delegateValue);

        serializer.serializeWithType(value, gen, provider, typeSer);

        verify(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);
    }

    @Test
    public void testIsEmptyNonNull() {
        when(serializer.convertValue(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(false);

        boolean result = serializer.isEmpty(provider, value);

        assertFalse(result);
    }

    @Test
    public void testIsEmptyNull() {
        when(serializer.convertValue(null)).thenReturn(null);
        when(delegateSerializer.isEmpty(null)).thenReturn(true);

        boolean result = serializer.isEmpty(provider, null);

        assertTrue(result);
    }

    @Test
    public void testGetSchemaWithSchemaAwareDelegateSerializer() throws JsonMappingException {
        SchemaAware delegate = mock(SchemaAware.class);
        when(serializer._delegateSerializer).thenReturn(delegate);
        JsonNode schemaNode = mock(JsonNode.class);
        when(delegate.getSchema(provider, delegateType)).thenReturn(schemaNode);

        JsonNode result = serializer.getSchema(provider, delegateType);

        assertEquals(schemaNode, result);
    }

    @Test
    public void testGetSchemaWithoutSchemaAwareDelegateSerializer() throws JsonMappingException {
        when(serializer._delegateSerializer).thenReturn(mock(JsonSerializer.class));

        JsonNode result = serializer.getSchema(provider, delegateType);

        assertNull(result);
    }

    @Test
    public void testGetSchemaWithSchemaAwareDelegateSerializerAndIsOptional() throws JsonMappingException {
        SchemaAware delegate = mock(SchemaAware.class);
        when(serializer._delegateSerializer).thenReturn(delegate);
        JsonNode schemaNode = mock(JsonNode.class);
        when(delegate.getSchema(provider, delegateType, true)).thenReturn(schemaNode);

        JsonNode result = serializer.getSchema(provider, delegateType, true);

        assertEquals(schemaNode, result);
    }

    @Test
    public void testGetSchemaWithoutSchemaAwareDelegateSerializerAndIsOptional() throws JsonMappingException {
        when(serializer._delegateSerializer).thenReturn(mock(JsonSerializer.class));

        JsonNode result = serializer.getSchema(provider, delegateType, true);

        assertNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        when(serializer._delegateSerializer).thenReturn(delegateSerializer);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitorWrapper, typeHint);
    }

    @Test
    public void testConvertValue() {
        when(converter.convert(value)).thenReturn(delegateValue);

        Object result = serializer.convertValue(value);

        assertEquals(delegateValue, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithDelegate() {
        serializer.withDelegate(converter, delegateType, delegateSerializer);
    }
}