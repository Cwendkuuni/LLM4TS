package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, Object> converter;

    @Mock
    private JavaType delegateType;

    @Mock
    private JsonSerializer<Object> delegateSerializer;

    @Mock
    private SerializerProvider provider;

    @Mock
    private JsonGenerator generator;

    @Mock
    private TypeSerializer typeSerializer;
    
    @Mock
    private BeanProperty property;

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testResolve() throws Exception {
        ResolvableSerializer resolvableSerializer = mock(ResolvableSerializer.class);

        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        when(delegateSerializer).thenReturn(resolvableSerializer);

        serializer.resolve(provider);

        verify(resolvableSerializer).resolve(provider);
    }

    @Test
    public void testCreateContextual() throws Exception {
        when(_converter.getOutputType(provider.getTypeFactory())).thenReturn(delegateType);
        when(provider.findValueSerializer(delegateType)).thenReturn(delegateSerializer);
        ContextualSerializer contextualSerializer = mock(ContextualSerializer.class);
        when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(delegateSerializer).thenReturn(contextualSerializer);
        
        JsonSerializer serializer = this.serializer.createContextual(provider, property);

        assertEquals(contextualSerializer, serializer);
        verify(provider, times(1)).handleSecondaryContextualization(serializer, property);
    }

    @Test
    public void testSerialize() throws Exception {
        Object value = new Object();
        Object delegateValue = new Object();

        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serialize(value, generator, provider);

        verify(delegateSerializer).serialize(delegateValue, generator, provider);
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Object value = new Object();
        Object delegateValue = new Object();

        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serializeWithType(value, generator, provider, typeSerializer);

        verify(delegateSerializer).serializeWithType(delegateValue, generator, provider, typeSerializer);
    }

    @Test
    public void testIsEmpty() throws Exception {
        Object value = new Object();
        Object delegateValue = new Object();

        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        boolean isEmpty = serializer.isEmpty(provider, value);

        assertEquals(true, isEmpty);
    }

    @Test
    public void testGetSchema_withSchemaAwareDelegateSerializer() throws Exception {
        SchemaAware schemaAwareDelegateSerializer = mock(SchemaAware.class);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(delegateSerializer).thenReturn(schemaAwareDelegateSerializer);

        Type typeHint = new Object().getClass();
        serializer.getSchema(provider, typeHint);

        verify(schemaAwareDelegateSerializer).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchema_withNonSchemaAwareDelegateSerializer() throws Exception {
        Type typeHint = new Object().getClass();
        serializer.getSchema(provider, typeHint);

        verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testGetSchema_withSchemaAwareDelegateSerializerAndOptionalFlag() throws Exception {
        SchemaAware schemaAwareDelegateSerializer = mock(SchemaAware.class);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(delegateSerializer).thenReturn(schemaAwareDelegateSerializer);

        Type typeHint = new Object().getClass();
        boolean isOptional = true;
        serializer.getSchema(provider, typeHint, isOptional);

        verify(schemaAwareDelegateSerializer).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testGetSchema_withNonSchemaAwareDelegateSerializerAndOptionalFlag() throws Exception {
        Type typeHint = new Object().getClass();
        boolean isOptional = true;
        serializer.getSchema(provider, typeHint, isOptional);

        verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, javaType);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, javaType);
    }

    @Test
    public void testConvertValue() throws Exception {
        Object value = new Object();
        Object convertedValue = new Object();

        when(converter.convert(value)).thenReturn(convertedValue);

        Object result = serializer.convertValue(value);

        assertEquals(convertedValue, result);
    }

    @Test
    public void testWithDelegate() throws Exception {
        Object value = new Object();
        JsonSerializer<Object> newDelegateSerializer = mock(JsonSerializer.class);

        StdDelegatingSerializer newSerializer = serializer.withDelegate(converter, delegateType, newDelegateSerializer);

        assertEquals(converter, newSerializer.getConverter());
        assertEquals(delegateType, newSerializer.handledType());
        assertEquals(newDelegateSerializer, newSerializer.getDelegatee());
    }
}