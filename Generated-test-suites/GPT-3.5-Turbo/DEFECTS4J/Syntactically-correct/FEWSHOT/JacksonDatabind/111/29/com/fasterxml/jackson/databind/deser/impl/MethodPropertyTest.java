package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
    private Method setter;
    
    @Mock
    private JsonDeserializer<?> deser;

    @Mock
    private NullValueProvider nva;

    @Mock
    private PropertyName newName;

    @Mock
    private DeserializationConfig config;

    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        methodProperty = new MethodProperty(methodProperty, deser, nva);
        methodProperty = new MethodProperty(methodProperty, newName);
        methodProperty = new MethodProperty(methodProperty, setter);
        methodProperty = methodProperty.withName(newName);
        methodProperty = methodProperty.withValueDeserializer(deser);
        methodProperty = methodProperty.withNullProvider(nva);
    }

    @Test
    public void testFixAccess() {
        methodProperty.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(methodProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(methodProperty.getMember(), method);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(_skipNulls).thenReturn(true);
        methodProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(_skipNulls).thenReturn(true);
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetThrowException() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(_skipNulls).thenReturn(true);
        when(_setter.invoke(instance, null)).thenThrow(new Exception());
        methodProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        methodProperty.set(instance, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        methodProperty.setAndReturn(instance, null);
    }

    @Test(expected = IOException.class)
    public void testSetThrowException() throws IOException {
        Object instance = new Object();
        when(_setter.invoke(instance, null)).thenThrow(new Exception());
        methodProperty.set(instance, null);
    }

    @Test
    public void testSetAndReturnThrowException() throws IOException {
        Object instance = new Object();
        when(_setter.invoke(instance, null)).thenThrow(new Exception());
        methodProperty.setAndReturn(instance, null);
    }

    @Test
    public void testReadResolve() {
        assertEquals(methodProperty.readResolve(), new MethodProperty(methodProperty, _annotated.getAnnotated()));
    }
}