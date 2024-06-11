package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {
    private StdDelegatingSerializer serializer;
    
    @Before
    public void setUp() {
        serializer = new StdDelegatingSerializer();
    }
    
    @Test
    public void testConstructor() {
        Converter<Object, ?> converter = null;
        serializer = new StdDelegatingSerializer(converter);
        
        assertNotNull(serializer);
        assertNull(serializer.getDelegatee());
        assertNull(serializer.getConverter());
    }
    
    @Test
    public void testConstructorWithClass() {
        Converter<Object, ?> converter = null;
        Class<Object> cls = Object.class;
        serializer = new StdDelegatingSerializer(cls, converter);
        
        assertNotNull(serializer);
        assertNull(serializer.getDelegatee());
        assertNull(serializer.getConverter());
    }
    
    @Test
    public void testConstructorWithDelegate() {
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        
        assertNotNull(serializer);
        assertNull(serializer.getDelegatee());
        assertNull(serializer.getConverter());
    }
    
    @Test
    public void testWithDelegate() {
        Converter<Object, ?> converter = null;
        JavaType delegateType = null;
        JsonSerializer<?> delegateSerializer = null;
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        
        StdDelegatingSerializer newSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);
        
        assertNotNull(newSerializer);
        assertNull(newSerializer.getDelegatee());
        assertNull(newSerializer.getConverter());
    }
    
    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = null;
        
        serializer.resolve(provider);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = null;
        BeanProperty property = null;
        
        serializer.createContextual(provider, property);
    }
    
    @Test
    public void testSerialize() throws IOException {
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        
        serializer.serialize(value, gen, provider);
    }
    
    @Test
    public void testSerializeWithType() throws IOException {
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        TypeSerializer typeSer = null;
        
        serializer.serializeWithType(value, gen, provider, typeSer);
    }
    
    @Test
    public void testIsEmpty() {
        Object value = null;
        
        boolean result = serializer.isEmpty(value);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsEmptyWithProvider() {
        SerializerProvider prov = null;
        Object value = null;
        
        boolean result = serializer.isEmpty(prov, value);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        
        serializer.getSchema(provider, typeHint);
    }
    
    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;
        
        serializer.getSchema(provider, typeHint, isOptional);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testConvertValue() {
        Object value = null;
        
        Object result = serializer.convertValue(value);
        
        assertNull(result);
    }
    
    @Test
    public void testHelperMethod() {
        Object value = null;
        StdDelegatingSerializer newSerializer = new StdDelegatingSerializer();
        
        Object result = newSerializer.convertValue(value);
        
        assertNull(result);
    }
}