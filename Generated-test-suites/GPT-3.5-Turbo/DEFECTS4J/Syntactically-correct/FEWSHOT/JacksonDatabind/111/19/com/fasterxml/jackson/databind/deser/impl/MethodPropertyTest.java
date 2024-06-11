package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
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
    private Method annotatedMethod;
    @Mock
    private JsonDeserializer<?> deser;
    @Mock
    private NullValueProvider nvp;
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
    private Object value;
    @Mock
    private Object result;
    @Mock
    private IOException ioe;
    @Mock
    private Throwable cause;

    private MethodProperty methodProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        when(p.hasToken(any())).thenReturn(true);
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructor() {
        assertEquals(method, methodProperty._annotated);
        assertEquals(annotatedMethod, methodProperty._setter);
        assertFalse(methodProperty._skipNulls);
    }

    @Test
    public void testConstructorWithDeserAndNvp() {
        MethodProperty newMethodProperty = new MethodProperty(methodProperty, deser, nvp);
        assertEquals(method, newMethodProperty._annotated);
        assertEquals(annotatedMethod, newMethodProperty._setter);
        assertTrue(newMethodProperty._skipNulls);
    }

    @Test
    public void testConstructorWithNewName() {
        when(methodProperty._skipNulls).thenReturn(true);
        MethodProperty newMethodProperty = new MethodProperty(methodProperty, newName);
        assertEquals(method, newMethodProperty._annotated);
        assertEquals(annotatedMethod, newMethodProperty._setter);
        assertTrue(newMethodProperty._skipNulls);
    }

    @Test
    public void testConstructorWithMethod() {
        MethodProperty newMethodProperty = new MethodProperty(methodProperty, annotatedMethod);
        assertEquals(method, newMethodProperty._annotated);
        assertEquals(annotatedMethod, newMethodProperty._setter);
        assertFalse(newMethodProperty._skipNulls);
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty newMethodProperty = mock(MethodProperty.class);
        when(newMethodProperty.withNullProvider(nvp)).thenCallRealMethod();
        MethodProperty result = newMethodProperty.withNullProvider(nvp);
        assertEquals(newMethodProperty, result);
    }

    @Test
    public void testFixAccess() {
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        methodProperty.fixAccess(config);
        verify(method).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        when(method.getAnnotation(acls)).thenReturn(null);
        Annotation result = methodProperty.getAnnotation(acls);
        assertNull(result);
        verify(method).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        when(methodProperty.getMember()).thenCallRealMethod();
        AnnotatedMember result = methodProperty.getMember();
        assertEquals(method, result);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(true);
        methodProperty.deserializeAndSet(p, ctxt, instance);
        verifyZeroInteractions(ctxt, instance);
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializer() throws IOException {
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._valueDeserializer.deserialize(p, ctxt)).thenReturn(value);
        methodProperty.deserializeAndSet(p, ctxt, instance);
        verify(method).invoke(instance, value);
    }

    @Test
    public void testDeserializeAndSetWithSkipNulls() throws IOException {
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._skipNulls).thenReturn(true);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(value);
        methodProperty.deserializeAndSet(p, ctxt, instance);
        verify(method).invoke(instance, value);
    }

    @Test
    public void testDeserializeAndSetWithNullValueDeserializer() throws IOException {
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._valueDeserializer.deserialize(p, ctxt)).thenReturn(null);
        when(methodProperty._skipNulls).thenReturn(true);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(value);
        methodProperty.deserializeAndSet(p, ctxt, instance);
        verify(method).invoke(instance, value);
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializerWithTypeDeserializer() throws IOException {
        when(methodProperty._valueDeserializer.deserializeWithType(p, ctxt, methodProperty._valueTypeDeserializer))
                .thenReturn(value);
        methodProperty.deserializeAndSet(p, ctxt, instance);
        verify(method).invoke(instance, value);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithException() throws Exception {
        when(method.invoke(instance, value)).thenThrow(ioe);
        methodProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(true);
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        verifyZeroInteractions(ctxt);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithValueDeserializer() throws IOException {
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._valueDeserializer.deserialize(p, ctxt)).thenReturn(value);
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(method).invoke(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithSkipNulls() throws IOException {
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._skipNulls).thenReturn(true);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(value);
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(method).invoke(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValueDeserializer() throws IOException {
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._valueDeserializer.deserialize(p, ctxt)).thenReturn(null);
        when(methodProperty._skipNulls).thenReturn(true);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(value);
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(method).invoke(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithValueDeserializerWithTypeDeserializer() throws IOException {
        when(methodProperty._valueDeserializer.deserializeWithType(p, ctxt, methodProperty._valueTypeDeserializer))
                .thenReturn(value);
        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        verify(method).invoke(instance, value);
        assertEquals(instance, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithException() throws Exception {
        when(method.invoke(instance, value)).thenThrow(ioe);
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        methodProperty.set(instance, value);
        verify(method).invoke(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        when(method.invoke(instance, value)).thenReturn(result);
        Object resultObj = methodProperty.setAndReturn(instance, value);
        verify(method).invoke(instance, value);
        assertEquals(result, resultObj);
    }

    @Test
    public void testSetAndReturnWithNullResult() throws IOException {
        when(method.invoke(instance, value)).thenReturn(null);
        Object resultObj = methodProperty.setAndReturn(instance, value);
        verify(method).invoke(instance, value);
        assertEquals(instance, resultObj);
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnWithException() throws Exception {
        when(method.invoke(instance, value)).thenThrow(cause);
        methodProperty.setAndReturn(instance, value);
        verify(cause).getCause();
    }

    @Test
    public void testReadResolve() {
        MethodProperty newMethodProperty = new MethodProperty(methodProperty, annotatedMethod);
        MethodProperty result = (MethodProperty) newMethodProperty.readResolve();
        assertEquals(newMethodProperty, result);
    }
}