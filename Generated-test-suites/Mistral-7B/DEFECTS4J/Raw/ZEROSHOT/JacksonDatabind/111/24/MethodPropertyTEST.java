package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MethodProperty class.
 */
public class MethodPropertyTest {

    private MethodProperty methodProperty;
    private AnnotatedMethod annotatedMethod;
    private Method setterMethod;
    private Object instance;
    private Object value;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() throws Exception {
        Annotation annotation = mock(Annotation.class);
        annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotated()).thenReturn(annotation);
        setterMethod = mock(Method.class);

        when(annotatedMethod.getAnnotated()).thenReturn(annotatedMethod);
        when(annotatedMethod.getName()).thenReturn("testMethod");
        when(annotatedMethod.getDeclaringClass()).thenReturn(Object.class);
        when(annotatedMethod.getParameterTypes()).thenReturn(new Class[0]);
        when(annotatedMethod.getParameterTypes()[0]).thenReturn(String.class);
        when(annotatedMethod.getReturnType()).thenReturn(String.class);

        when(setterMethod.getName()).thenReturn("setTestMethod");
        when(setterMethod.getParameterTypes()).thenReturn(new Class[1]);
        when(setterMethod.getParameterTypes()[0]).thenReturn(String.class);

        methodProperty = new MethodProperty(null, null, null, null, annotatedMethod);
        methodProperty = new MethodProperty(methodProperty, null, null);
        instance = new Object();
        value = "testValue";
    }

    @Test
    public void testMethodPropertyConstructors() {
        assertSame(methodProperty.getAnnotated(), annotatedMethod);
        assertSame(methodProperty._setter, setterMethod);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object result = methodProperty.setAndReturn(instance, value);
        assertSame(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        methodProperty.set(instance, value);

        String testMethod = (String) BeanUtil.getRawValue(instance, "testMethod");
        assertSame(value, testMethod);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getValueAsString()).thenReturn(value);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        String testMethod = (String) BeanUtil.getRawValue(instance, "testMethod");
        assertSame(value, testMethod);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getValueAsString()).thenReturn(value);

        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertSame(instance, result);

        String testMethod = (String) BeanUtil.getRawValue(instance, "testMethod");
        assertSame(value, testMethod);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        MethodProperty newMethodProperty = methodProperty.withName(newName);

        assertSame(methodProperty, newMethodProperty);
        assertSame(newName, newMethodProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(valueDeserializer);

        assertSame(methodProperty, newMethodProperty);
        assertSame(valueDeserializer, newMethodProperty._valueDeserializer);
        assertSame(nullValueProvider, newMethodProperty._nullProvider);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        MethodProperty newMethodProperty = methodProperty.withNullProvider(nullValueProvider);

        assertSame(methodProperty, newMethodProperty);
        assertSame(nullValueProvider, newMethodProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        methodProperty.fixAccess(config);

        // TODO: Add assertions for annotatedMethod.getModifiers()
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);

        assertSame(annotation, methodProperty.getAnnotation(annotation.getClass()));
        assertNull(methodProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertSame(annotatedMethod, methodProperty.getMember());
    }
}