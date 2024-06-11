package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

    @InjectMocks
    private MethodProperty methodProperty;
    
    @Mock
    private BeanPropertyDefinition propDef;
    
    @Mock
    private JavaType type;
    
    @Mock
    private TypeDeserializer typeDeser;
    
    @Mock
    private Annotations contextAnnotations;
    
    @Mock
    private AnnotatedMethod method;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }
    
    @Test
    public void testConstructorAndGetters() {
        assertEquals(method, methodProperty.getMember());
        assertEquals(NullValueProvider.NoProvider.instance, methodProperty.getNullProvider());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.deserializeAndSet(p, ctxt, instance);
        
        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(p).getValueAsToken();
        verify(ctxt).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        verify(p).isNull();
        verify(method).invoke(instance, value);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(p).getValueAsToken();
        verify(ctxt).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        verify(p).isNull();
        verify(method).invoke(instance, value);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.set(instance, value);
        
        verify(method).invoke(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.setAndReturn(instance, value);
        
        verify(method).invoke(instance, value);
    }
}