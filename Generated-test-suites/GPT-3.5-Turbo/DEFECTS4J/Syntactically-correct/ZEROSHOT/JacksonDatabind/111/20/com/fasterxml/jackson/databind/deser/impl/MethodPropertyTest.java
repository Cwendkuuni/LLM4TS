package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

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

import static org.mockito.Mockito.mock;

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
    private JsonDeserializer<?> deser;

    @Mock
    private NullValueProvider nva;

    @Mock
    private PropertyName newName;

    @Mock
    private DeserializationConfig config;

    @Test
    public void testConstructor() {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Assert.assertEquals(method, methodProperty._annotated);
        Assert.assertEquals(method.getAnnotated(), methodProperty._setter);
        Assert.assertFalse(methodProperty._skipNulls);
    }

    @Test
    public void testCopyConstructorWithDeserializerAndNullValueProvider() {
        MethodProperty src = mock(MethodProperty.class);
        MethodProperty methodProperty = new MethodProperty(src, deser, nva);
        Assert.assertEquals(src._annotated, methodProperty._annotated);
        Assert.assertEquals(src._setter, methodProperty._setter);
        Assert.assertTrue(methodProperty._skipNulls);
    }

    @Test
    public void testCopyConstructorWithNewName() {
        MethodProperty src = mock(MethodProperty.class);
        MethodProperty methodProperty = new MethodProperty(src, newName);
        Assert.assertEquals(src._annotated, methodProperty._annotated);
        Assert.assertEquals(src._setter, methodProperty._setter);
        Assert.assertEquals(src._skipNulls, methodProperty._skipNulls);
    }

    @Test
    public void testCopyConstructorWithMethod() {
        MethodProperty src = mock(MethodProperty.class);
        Method m = mock(Method.class);
        MethodProperty methodProperty = new MethodProperty(src, m);
        Assert.assertEquals(src._annotated, methodProperty._annotated);
        Assert.assertEquals(m, methodProperty._setter);
        Assert.assertEquals(src._skipNulls, methodProperty._skipNulls);
    }

    @Test
    public void testWithName() {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        SettableBeanProperty result = methodProperty.withName(newName);
        Assert.assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        SettableBeanProperty result = methodProperty.withValueDeserializer(deser);
        Assert.assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        SettableBeanProperty result = methodProperty.withNullProvider(nva);
        Assert.assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        methodProperty.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Class<A> acls = mock(Class.class);
        Annotation result = methodProperty.getAnnotation(acls);
        Assert.assertNull(result);
    }

    @Test
    public void testGetMember() {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        AnnotatedMember result = methodProperty.getMember();
        Assert.assertEquals(method, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        methodProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        methodProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        methodProperty.setAndReturn(instance, value);
    }

    @Test
    public void testReadResolve() {
        MethodProperty src = mock(MethodProperty.class);
        MethodProperty methodProperty = new MethodProperty(src, method);
        Object result = methodProperty.readResolve();
        Assert.assertNotSame(src, result);
    }
}