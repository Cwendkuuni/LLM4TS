package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MethodPropertyTest {
    
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
    
    @Mock
    private Method methodAnnotated;
    
    @Mock
    private JsonDeserializer<?> deser;
    
    @Mock
    private NullValueProvider nva;
    
    @Mock
    private PropertyName newName;

    @Mock
    private DeserializationConfig config;

    @Mock
    private JsonParser p;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private Object instance;

    @Mock
    private JsonDeserializer<?> valueDeserializer;

    @Mock
    private Object value;

    @Mock
    private Exception exception;
    
    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(method.getAnnotated()).thenReturn(methodAnnotated);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
        
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructor() {
        assertEquals(propDef, methodProperty.getMember());
    }

    @Test
    public void testConstructorWithDeserAndNva() {
        MethodProperty constructedMethodProperty = new MethodProperty(methodProperty, deser, nva);
        
        assertEquals(propDef, constructedMethodProperty.getMember());
    }

    @Test
    public void testConstructorWithNewName() {
        MethodProperty constructedMethodProperty = new MethodProperty(methodProperty, newName);
        
        assertEquals(propDef, constructedMethodProperty.getMember());
    }

    @Test
    public void testConstructorWithMethod() {
        MethodProperty constructedMethodProperty = new MethodProperty(methodProperty, methodAnnotated);
        
        assertEquals(propDef, constructedMethodProperty.getMember());
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty methodPropertyWithNullProvider = (MethodProperty) methodProperty.withNullProvider(nva);

        assertNull(methodPropertyWithNullProvider.getConfig());

    }

    @Test
    public void testDeserializeAndSet() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty.getNullProvider()).thenReturn(nva);
        when(nva.getNullValue(ctxt)).thenReturn(value);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty, times(2)).getNullProvider();
        verify(nva).getNullValue(ctxt);
    }

    @Test
    public void testDeserializeAndSetWithoutValueDeserializer() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(p.getValueAsToken()).thenReturn(JsonToken.VALUE_FALSE);
        when(methodProperty.getValueDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(value);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(p).getValueAsToken();
        verify(methodProperty).getValueDeserializer();
        verify(valueDeserializer).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeAndSetWithoutValueTypeDeserializer() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(methodProperty.getValueDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserializeWithType(p, ctxt, null)).thenReturn(value);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty).getValueDeserializer();
        verify(valueDeserializer).deserializeWithType(p, ctxt, null);
    }

    @Test
    public void testDeserializeAndSetWithSkipNulls() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty.getNullProvider()).thenReturn(nva);
        when(nva.getNullValue(ctxt)).thenReturn(value);
        methodProperty = spy(methodProperty);
        doReturn(true).when(methodProperty).isSkipNulls();

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty, times(2)).getNullProvider();
        verify(nva).getNullValue(ctxt);
        verify(methodProperty).isSkipNulls();
        verify(nva, never()).getNullValue(ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetThrowsIOException() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty.getNullProvider()).thenReturn(nva);
        when(nva.getNullValue(ctxt)).thenReturn(value);
        when(methodProperty.getSetter()).thenReturn(methodAnnotated);
        when(methodAnnotated.invoke(instance, value)).thenThrow(exception);

        methodProperty.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty.getNullProvider()).thenReturn(nva);
        when(nva.getNullValue(ctxt)).thenReturn(value);

        methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty, times(2)).getNullProvider();
        verify(nva).getNullValue(ctxt);
    }

    @Test
    public void testDeserializeSetAndReturnWithoutValueDeserializer() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(p.getValueAsToken()).thenReturn(JsonToken.VALUE_FALSE);
        when(methodProperty.getValueDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(value);

        methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(p).getValueAsToken();
        verify(methodProperty).getValueDeserializer();
        verify(valueDeserializer).deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeSetAndReturnWithoutValueTypeDeserializer() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(methodProperty.getValueDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserializeWithType(p, ctxt, null)).thenReturn(value);

        methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty).getValueDeserializer();
        verify(valueDeserializer).deserializeWithType(p, ctxt, null);
    }

    @Test
    public void testDeserializeSetAndReturnWithSkipNulls() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty.getNullProvider()).thenReturn(nva);
        when(nva.getNullValue(ctxt)).thenReturn(value);
        methodProperty = spy(methodProperty);
        doReturn(true).when(methodProperty).isSkipNulls();

        methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(p).hasToken(JsonToken.VALUE_NULL);
        verify(methodProperty, times(2)).getNullProvider();
        verify(nva).getNullValue(ctxt);
        verify(methodProperty).isSkipNulls();
        verify(nva, never()).getNullValue(ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnThrowsIOException() throws Exception {

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty.getNullProvider()).thenReturn(nva);
        when(nva.getNullValue(ctxt)).thenReturn(value);
        when(methodProperty.getSetter()).thenReturn(methodAnnotated);
        when(methodAnnotated.invoke(instance, value)).thenThrow(exception);

        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test
    public void testSet() throws Exception {

        methodProperty.set(instance, value);

        verify(methodProperty.getSetter()).invoke(instance, value);
    }

    @Test(expected = IOException.class)
    public void testSetThrowsIOException() throws Exception {

        when(methodProperty.getSetter()).thenReturn(methodAnnotated);
        when(methodAnnotated.invoke(instance, value)).thenThrow(exception);

        methodProperty.set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws Exception {

        methodProperty.setAndReturn(instance, value);

        verify(methodProperty.getSetter()).invoke(instance, value);
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnThrowsIOException() throws Exception {

        when(methodProperty.getSetter()).thenReturn(methodAnnotated);
        when(methodAnnotated.invoke(instance, value)).thenThrow(exception);

        methodProperty.setAndReturn(instance, value);
    }

    @Test
    public void testFixAccess() throws Exception {

        methodProperty.fixAccess(config);

        verify(method).fixAccess(false);
    }

    @Test
    public void testGetAnnotation() throws Exception {

        when(method.getAnnotated()).thenReturn(methodAnnotated);

        methodProperty.getAnnotation(Annotation.class);

        verify(methodAnnotated).getAnnotation(Annotation.class);
    }

    @Test
    public void testWithConfig() throws Exception {

        methodProperty.withConfig(config);

        verify(methodAnnotation).withConfig(config);
    }
}