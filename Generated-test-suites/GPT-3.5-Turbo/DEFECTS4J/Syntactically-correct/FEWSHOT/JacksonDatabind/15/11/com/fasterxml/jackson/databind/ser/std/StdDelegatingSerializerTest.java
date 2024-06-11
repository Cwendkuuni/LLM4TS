package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
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

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {
    
    @Mock
    private Converter<Object, ?> converter;
    
    @Mock
    private JavaType delegateType;
    
    @Mock
    private JsonSerializer<?> delegateSerializer;
    
    @Mock
    private JsonSerializer<Object> mockJsonSerializer;
    
    @Mock
    private Object mockValue;
    
    @Mock
    private JsonGenerator mockJsonGenerator;
    
    @Mock
    private SerializerProvider mockProvider;
    
    @Mock
    private TypeSerializer mockTypeSerializer;
    
    @Mock
    private JsonFormatVisitorWrapper mockVisitor;
    
    @Mock
    private BeanProperty mockProperty;
    
    private StdDelegatingSerializer serializer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        serializer.resolve(mockProvider);
        verify((ResolvableSerializer) delegateSerializer, times(1)).resolve(mockProvider);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(converter.getOutputType(mockProvider.getTypeFactory())).thenReturn(delegateType);
        when(mockProvider.findValueSerializer(delegateType)).thenReturn(mockJsonSerializer);
        when(mockProvider.handleSecondaryContextualization(mockJsonSerializer, mockProperty)).thenReturn(mockJsonSerializer);
        JsonSerializer<?> returnedSerializer = serializer.createContextual(mockProvider, mockProperty);
        verify((ContextualSerializer) delegateSerializer, times(1)).createContextual(mockProvider, mockProperty);
        assertEquals(mockJsonSerializer, returnedSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(mockValue)).thenReturn(delegateValue);
        serializer.serialize(mockValue, mockJsonGenerator, mockProvider);
        verify(delegateSerializer, times(1)).serialize(delegateValue, mockJsonGenerator, mockProvider);
    }
    
    @Test
    public void testSerializeWithType() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(mockValue)).thenReturn(delegateValue);
        serializer.serializeWithType(mockValue, mockJsonGenerator, mockProvider, mockTypeSerializer);
        verify(delegateSerializer, times(1)).serializeWithType(delegateValue, mockJsonGenerator, mockProvider, mockTypeSerializer);
    }
    
    @Deprecated
    @Test
    public void testIsEmptyDeprecated() {
        Object delegateValue = new Object();
        when(converter.convert(mockValue)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);
        assertTrue(serializer.isEmpty(mockValue));
    }
    
    @Test
    public void testIsEmpty() {
        Object delegateValue = new Object();
        when(converter.convert(mockValue)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(mockProvider, delegateValue)).thenReturn(true);
        assertTrue(serializer.isEmpty(mockProvider, mockValue));
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(mockProvider, delegateType)).thenReturn(mock(JsonNode.class));
        serializer.getSchema(mockProvider, delegateType);
        verify((SchemaAware) delegateSerializer, times(1)).getSchema(mockProvider, delegateType);
    }
    
    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(mockProvider, delegateType, true)).thenReturn(mock(JsonNode.class));
        serializer.getSchema(mockProvider, delegateType, true);
        verify((SchemaAware) delegateSerializer, times(1)).getSchema(mockProvider, delegateType, true);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(mockVisitor, delegateType);
        verify(delegateSerializer, times(1)).acceptJsonFormatVisitor(mockVisitor, delegateType);
    }
    
    @Test
    public void testConvertValue() {
        Object delegateValue = new Object();
        when(converter.convert(mockValue)).thenReturn(delegateValue);
        assertEquals(delegateValue, serializer.convertValue(mockValue));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testWithDelegateFail() {
        StdDelegatingSerializer subSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer) {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
                return null;
            }
        };
        subSerializer.resolve(mockProvider);
    }
}