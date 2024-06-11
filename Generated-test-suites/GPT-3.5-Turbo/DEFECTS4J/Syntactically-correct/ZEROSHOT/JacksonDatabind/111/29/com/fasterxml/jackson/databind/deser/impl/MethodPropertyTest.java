package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MethodPropertyTest {

    private MethodProperty methodProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;
    private Method setter;
    private boolean skipNulls;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private PropertyName newName;
    private Method m;
    private JsonParser p;
    private DeserializationContext ctxt;
    private Object instance;
    private Object value;
    private Exception e;
    private Object result;

    @Before
    public void setUp() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        method = mock(AnnotatedMethod.class);
        setter = mock(Method.class);
        skipNulls = false;
        deser = mock(JsonDeserializer.class);
        nva = mock(NullValueProvider.class);
        newName = mock(PropertyName.class);
        m = mock(Method.class);
        p = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        instance = new Object();
        value = new Object();
        e = new Exception();
        result = new Object();
    }

    @Test
    public void testConstructor() {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(propDef, methodProperty.getPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getMember());
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        methodProperty = new MethodProperty(propDef, deser, nva);
        assertEquals(propDef, methodProperty.getPropertyDefinition());
        assertEquals(deser, methodProperty.getValueDeserializer());
        assertEquals(nva, methodProperty.getNullValueProvider());
    }

    @Test
    public void testConstructorWithNewName() {
        methodProperty = new MethodProperty(methodProperty, newName);
        assertEquals(methodProperty, methodProperty.withName(newName));
    }

    @Test
    public void testConstructorWithNullProvider() {
        methodProperty = new MethodProperty(methodProperty, deser, nva);
        assertEquals(methodProperty, methodProperty.withNullProvider(nva));
    }

    @Test
    public void testFixAccess() {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(any())).thenReturn(true);
        methodProperty.fixAccess(config);
        verify(method).fixAccess(anyBoolean());
    }

    @Test
    public void testGetAnnotation() {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Class<?> acls = mock(Class.class);
        when(method.getAnnotation(acls)).thenReturn(null);
        assertEquals(null, methodProperty.getAnnotation(acls));
        verify(method).getAnnotation(acls);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(method.getAnnotated()).thenReturn(setter);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        methodProperty.deserializeAndSet(p, ctxt, instance);
        assertEquals(null, instance);
    }

    @Test
    public void testDeserializeAndSetWithNonNullValue() throws IOException {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(method.getAnnotated()).thenReturn(setter);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn("test");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        methodProperty.deserializeAndSet(p, ctxt, instance);
        assertEquals("test", instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(method.getAnnotated()).thenReturn(setter);
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, instance);
    }

    @Test
    public void testDeserializeSetAndReturnWithNonNullValue() throws IOException {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(method.getAnnotated()).thenReturn(setter);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn("test");
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals("test", instance);
    }

    @Test
    public void testSetWithException() throws IOException {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(method.getAnnotated()).thenReturn(setter);
        when(setter.invoke(instance, value)).thenThrow(e);
        JsonParser parser = mock(JsonParser.class);
        try {
            methodProperty.set(instance, value);
        } catch (IOException ex) {
            assertEquals(e, ex.getCause());
        }
        verify(setter).invoke(instance, value);
    }

    @Test
    public void testSetWithNoException() throws IOException {
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(method.getAnnotated()).thenReturn(setter);
        when(setter.invoke(instance, value)).thenReturn(result);
        methodProperty.set(instance, value);
        verify(setter).invoke(instance, value);
    }
}