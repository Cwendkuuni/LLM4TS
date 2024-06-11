
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, ?> mockConverter;
    @Mock
    private JavaType mockJavaType;
    @Mock
    private JsonSerializer<Object> mockDelegateSerializer;
    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private SerializerProvider mockSerializerProvider;
    @Mock
    private TypeSerializer mockTypeSerializer;
    @Mock
    private BeanProperty mockBeanProperty;
    @Mock
    private SchemaAware mockSchemaAware;
    @Mock
    private JsonNode mockJsonNode;
    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;

    private StdDelegatingSerializer stdDelegatingSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stdDelegatingSerializer = new StdDelegatingSerializer(mockConverter, mockJavaType, mockDelegateSerializer);
    }

    @Test
    public void testResolve() throws Exception {
        ResolvableSerializer mockResolvableSerializer = (ResolvableSerializer) stdDelegatingSerializer.getDelegatee();
        stdDelegatingSerializer.resolve(mockSerializerProvider);
        verify(mockResolvableSerializer, times(1)).resolve(mockSerializerProvider);
    }

    @Test
    public void testCreateContextual() throws Exception {
        ContextualSerializer mockContextualSerializer = (ContextualSerializer) stdDelegatingSerializer.getDelegatee();
        when(mockDelegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(mockSerializerProvider.handleSecondaryContextualization(any(), any())).thenReturn(mockContextualSerializer);
        stdDelegatingSerializer.createContextual(mockSerializerProvider, mockBeanProperty);
        verify(mockSerializerProvider, times(1)).handleSecondaryContextualization(any(), any());
    }

    @Test
    public void testSerialize_Null() throws Exception {
        Object value = null;
        stdDelegatingSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockSerializerProvider, times(1)).defaultSerializeNull(mockJsonGenerator);
        verify(mockDelegateSerializer, times(0)).serialize(any(), any(), any());
    }

    @Test
    public void testSerialize() throws Exception {
        Object value = new Object();
        when(mockConverter.convert(value)).thenReturn(value);
        stdDelegatingSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockDelegateSerializer, times(1)).serialize(value, mockJsonGenerator, mockSerializerProvider);
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Object value = new Object();
        when(mockConverter.convert(value)).thenReturn(value);
        stdDelegatingSerializer.serializeWithType(value, mockJsonGenerator, mockSerializerProvider, mockTypeSerializer);
        verify(mockDelegateSerializer, times(1)).serializeWithType(value, mockJsonGenerator, mockSerializerProvider, mockTypeSerializer);
    }

    @Test
    public void testIsEmpty_Null() throws Exception {
        Object value = null;
        stdDelegatingSerializer.isEmpty(mockSerializerProvider, value);
        verify(mockDelegateSerializer, times(0)).isEmpty(any(), any());
    }

    @Test
    public void testIsEmpty_False() throws Exception {
        Object value = new Object();
        when(mockConverter.convert(value)).thenReturn(value);
        when(mockDelegateSerializer.isEmpty(any(), any())).thenReturn(false);
        stdDelegatingSerializer.isEmpty(mockSerializerProvider, value);
        verify(mockDelegateSerializer, times(1)).isEmpty(any(), any());
    }

    @Test
    public void testIsEmpty_True() throws Exception {
        Object value = new Object();
        when(mockConverter.convert(value)).thenReturn(value);
        when(mockDelegateSerializer.isEmpty(any(), any())).thenReturn(true);
        stdDelegatingSerializer.isEmpty(mockSerializerProvider, value);
        verify(mockDelegateSerializer, times(1)).isEmpty(any(), any());
    }

    @Test
    public void testGetSchema_WithSchemaAware() throws Exception {
        when(mockDelegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) mockDelegateSerializer).getSchema(any(), any())).thenReturn(mockJsonNode);
        stdDelegatingSerializer.getSchema(mockSerializerProvider, mockJavaType);
        verify(((SchemaAware) mockDelegateSerializer), times(1)).getSchema(any(), any());
    }

    @Test
    public void testGetSchema_WithoutSchemaAware() throws Exception {
        stdDelegatingSerializer.getSchema(mockSerializerProvider, mockJavaType);
        verify(mockDelegateSerializer, times(0)).getSchema(any(), any());
    }

    @Test
    public void testGetSchema_WithSchemaAware_IsOptional() throws Exception {
        when(mockDelegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) mockDelegateSerializer).getSchema(any(), any(), anyBoolean())).thenReturn(mockJsonNode);
        stdDelegatingSerializer.getSchema(mockSerializerProvider, mockJavaType, true);
        verify(((SchemaAware) mockDelegateSerializer), times(1)).getSchema(any(), any(), anyBoolean());
    }

    @Test
    public void testGetSchema_WithSchemaAware_IsNotOptional() throws Exception {
        when(mockDelegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) mockDelegateSerializer).getSchema(any(), any(), anyBoolean())).thenReturn(mockJsonNode);
        stdDelegatingSerializer.getSchema(mockSerializerProvider, mockJavaType, false);
        verify(((SchemaAware) mockDelegateSerializer), times(1)).getSchema(any(), any(), anyBoolean());
    }

    @Test
    public void testGetSchema_WithoutSchemaAware_IsOptional() throws Exception {
        stdDelegatingSerializer.getSchema(mockSerializerProvider, mockJavaType, true);
        verify(mockDelegateSerializer, times(0)).getSchema(any(), any(), anyBoolean());
    }

    @Test
    public void testGetSchema_WithoutSchemaAware_IsNotOptional() throws Exception {
        stdDelegatingSerializer.getSchema(mockSerializerProvider, mockJavaType, false);
        verify(mockDelegateSerializer, times(0)).getSchema(any(), any(), anyBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        stdDelegatingSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
        verify(mockDelegateSerializer, times(1)).acceptJsonFormatVisitor(any(), any());
    }

    @Test
    public void testConvertValue() throws Exception {
        Object value = new Object();
        stdDelegatingSerializer.convertValue(value);
        verify(mockConverter, times(1)).convert(value);
    }
}

