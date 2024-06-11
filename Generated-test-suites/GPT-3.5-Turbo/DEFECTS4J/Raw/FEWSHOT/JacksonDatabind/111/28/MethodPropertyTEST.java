package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {
    private MethodProperty methodProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;
    private Method setter;
    
    @Before
    public void setUp() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        method = mock(AnnotatedMethod.class);
        setter = mock(Method.class);
        
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructor() {
        assertEquals(propDef, methodProperty.getPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        assertFalse(methodProperty.isSkipNulls());
        assertFalse(methodProperty.getValueDeserializer().hasNullValue());
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(methodProperty.getValueDeserializer().deserialize(p, ctxt)).thenReturn(value);
        when(methodProperty.getNullProvider().getNullValue(ctxt)).thenReturn(null);
        
        methodProperty.deserializeAndSet(p, ctxt, instance);
        verify(setter, times(1)).invoke(instance, value);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        assertTrue(methodProperty.isSkipNulls());
        assertTrue(methodProperty.getValueDeserializer().hasNullValue());
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        
        methodProperty.deserializeAndSet(p, ctxt, instance);
        verify(setter, never()).invoke(anyObject(), anyObject());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        assertFalse(methodProperty.isSkipNulls());
        assertFalse(methodProperty.getValueDeserializer().hasNullValue());
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(methodProperty.getValueDeserializer().deserialize(p, ctxt)).thenReturn(value);
        when(methodProperty.getNullProvider().getNullValue(ctxt)).thenReturn(null);
        
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(setter, times(1)).invoke(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        assertTrue(methodProperty.isSkipNulls());
        assertTrue(methodProperty.getValueDeserializer().hasNullValue());
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(setter, never()).invoke(anyObject(), anyObject());
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.set(instance, value);
        verify(setter, times(1)).invoke(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = new Object();
        
        when(setter.invoke(instance, value)).thenReturn(result);
        
        Object actualResult = methodProperty.setAndReturn(instance, value);
        verify(setter, times(1)).invoke(instance, value);
        assertEquals(result, actualResult);
    }
}