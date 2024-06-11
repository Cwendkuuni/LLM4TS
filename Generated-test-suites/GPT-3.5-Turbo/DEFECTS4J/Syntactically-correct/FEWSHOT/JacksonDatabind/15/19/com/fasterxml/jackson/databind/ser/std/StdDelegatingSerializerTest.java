package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    
    @Mock
    private JsonSerializer<Object> delegateSerializer;
    
    @Mock
    private JsonGenerator generator;
    
    @Mock
    private SerializerProvider provider;
    
    @Mock
    private TypeSerializer typeSerializer;
    
    @Mock
    private Type typeHint;
    
    @Mock
    private JsonFormatVisitorWrapper visitor;
    
    @Mock
    private JavaType javaType;
    
    @Mock
    private BeanProperty property;
    
    @Mock
    private SerializerProvider prov;
    
    @Mock
    private Object value;
    
    @Mock
    private Object delegateValue;
    
    @Test
    public void testSerialize() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        serializer.serialize(value, generator, provider);
        
        assertNull(serializer.getDelegatee());
    }
    
    @Test
    public void testSerializeWithType() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        serializer.serializeWithType(value, generator, provider, typeSerializer);
        
        assertNull(serializer.getDelegatee());
    }
    
    @Test
    public void testIsEmpty() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        assertEquals(true, serializer.isEmpty(value));
    }
    
    @Test
    public void testIsEmptyWithProvider() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        assertEquals(true, serializer.isEmpty(prov, value));
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        assertEquals(null, serializer.getSchema(provider, typeHint));
    }
    
    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        assertEquals(null, serializer.getSchema(provider, typeHint, false));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        serializer.acceptJsonFormatVisitor(visitor, javaType);
    }
    
    @Test
    public void testConvertValue() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<Object,?>) null);
        
        Object result = serializer.convertValue(value);
        
        assertEquals(null, result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testWithDelegate() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<Object,?>) null);
        
        serializer.withDelegate(null, javaType, delegateSerializer);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        MockitoAnnotations.initMocks(this);
        
        when(property.getType()).thenReturn(javaType);
        when(_delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(_delegateSerializer.isUnwrappingSerializer()).thenReturn(false);
        when(provider.handleSecondaryContextualization(any(JsonSerializer.class), eq(property))).thenReturn(delegateSerializer);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<Object,?>) null, javaType, delegateSerializer);
        
        assertEquals(delegateSerializer, serializer.createContextual(provider, property));
        
        verify(serializer, times(1)).withDelegate(any(Converter.class), any(JavaType.class), eq(delegateSerializer));
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer((Converter<?,?>) null);
        
        serializer.resolve(provider);
        
        assertNull(serializer.getDelegatee());
    }
}