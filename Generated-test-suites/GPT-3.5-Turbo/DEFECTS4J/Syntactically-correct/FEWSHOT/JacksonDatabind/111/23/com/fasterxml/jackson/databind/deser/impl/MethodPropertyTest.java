package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {
    
    private MethodProperty methodProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private ObjectMapper mapper;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private MethodProperty src;
    private PropertyName newName;
    private Method m;
    private DeserializationConfig config;
    
    @Before
    public void setup() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        deser = mock(JsonDeserializer.class);
        nva = mock(NullValueProvider.class);
        src = mock(MethodProperty.class);
        newName = mock(PropertyName.class);
        m = mock(Method.class);
        config = mock(DeserializationConfig.class);
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, m);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(propDef, methodProperty.getPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(m, methodProperty.getMember().getAnnotated());
        assertEquals(false, methodProperty.isSkipNulls());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nva.getNullValue(any(DeserializationContext.class))).thenReturn(null);
        
        methodProperty.setDeserializer(deser);
        methodProperty.setNullProvider(nva);
        methodProperty.deserializeAndSet(p, ctxt, instance);
        
        // verify that _valueDeserializer.deserialize is invoked
        // verify that _setter.invoke is invoked
        // verify that _throwAsIOE is invoked if an exception is thrown
        // verify that the object is set correctly
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nva.getNullValue(any(DeserializationContext.class))).thenReturn(null);
        
        methodProperty.setDeserializer(deser);
        methodProperty.setNullProvider(nva);
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        // verify that _valueDeserializer.deserialize is invoked
        // verify that _setter.invoke is invoked
        // verify that _throwAsIOE is invoked if an exception is thrown
        // verify that the object is set correctly
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.set(instance, value);
        
        // verify that _setter.invoke is invoked
        // verify that _throwAsIOE is invoked if an exception is thrown
        // verify that the object is set correctly
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.setAndReturn(instance, value);
        
        // verify that _setter.invoke is invoked
        // verify that _throwAsIOE is invoked if an exception is thrown
        // verify that the object is set correctly
    }
    
    @Test
    public void testWithNames() {
        methodProperty.withName(newName);
        
        // verify that a new MethodProperty is returned with the specified PropertyName
    }
    
    @Test
    public void testWithValueDeserializer() {
        methodProperty.withValueDeserializer(deser);
        
        // verify that a new MethodProperty is returned with the specified JsonDeserializer
    }
    
    @Test
    public void testWithNullProvider() {
        methodProperty.withNullProvider(nva);
        
        // verify that a new MethodProperty is returned with the specified NullValueProvider
    }
    
    @Test
    public void testFixAccess() {
        methodProperty.fixAccess(config);
        
        // verify that AnnotationIntrospector#fixAccess is called
        // verify that _annotated.fixAccess is called
    }
    
    @Test
    public void testGetAnnotation() {
        methodProperty.getAnnotation(Annotation.class);
        
        // verify that _annotated.getAnnotations is called
    }
    
    @Test
    public void testReadResolve() {
        methodProperty.readResolve();
        
        // verify that a new MethodProperty is returned with the Method
    }
}