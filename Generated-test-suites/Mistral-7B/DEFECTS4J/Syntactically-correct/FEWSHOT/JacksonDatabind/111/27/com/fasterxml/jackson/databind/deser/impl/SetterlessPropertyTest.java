package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    @SuppressWarnings("unchecked")
    private static final BeanPropertyDefinition testPropertyDefinition = mock(BeanPropertyDefinition.class);
    @SuppressWarnings("unchecked")
    private static final JavaType testType = mock(JavaType.class);
    @SuppressWarnings("unchecked")
    private static final TypeDeserializer testTypeDeserializer = mock(TypeDeserializer.class);
    @SuppressWarnings("unchecked")
    private static final Annotations testContextAnnotations = mock(Annotations.class);
    @SuppressWarnings("unchecked")
    private static final AnnotatedMethod testAnnotatedMethod = mock(AnnotatedMethod.class);

    @Test
    public void testConstructor() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        assertSame(testPropertyDefinition, setterlessProperty.getBeanPropertyDefinition());
        assertSame(testType, setterlessProperty.getType());
        assertSame(testTypeDeserializer, setterlessProperty.getTypeDeserializer());
        assertSame(testContextAnnotations, setterlessProperty.getContextAnnotations());
        assertSame(testAnnotatedMethod, setterlessProperty.getAnnotated());
        assertSame(testAnnotatedMethod.getMethod(), setterlessProperty.getGetMethod());
    }

    @Test
    public void testWithName() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        AnnotatedMember newAnnotatedMember = mock(AnnotatedMember.class);
        SetterlessProperty newSetterlessProperty = setterlessProperty.withName(new PropertyName("newName"));
        assertSame(setterlessProperty, newSetterlessProperty);
        assertSame(newAnnotatedMember, newSetterlessProperty.getAnnotated());
    }

    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        SetterlessProperty newSetterlessProperty = setterlessProperty.withValueDeserializer(jsonDeserializer);
        assertSame(jsonDeserializer, newSetterlessProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        SetterlessProperty newSetterlessProperty = setterlessProperty.withNullProvider(nullValueProvider);
        assertSame(nullValueProvider, newSetterlessProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        setterlessProperty.fixAccess(mock(DeserializationConfig.class));
    }

    @Test
    public void testGetAnnotation() {
        Class<JsonProperty> jsonPropertyAnnotationClass = JsonProperty.class;
        JsonProperty jsonPropertyAnnotation = mock(JsonProperty.class);
        when(testAnnotatedMethod.getAnnotation(jsonPropertyAnnotationClass)).thenReturn(jsonPropertyAnnotation);
        Object annotation = setterlessProperty.getAnnotation(jsonPropertyAnnotationClass);
        assertSame(jsonPropertyAnnotation, annotation);
    }

    @Test
    public void testGetMember() {
        assertSame(testAnnotatedMethod, setterlessProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        Method getter = mock(Method.class);
        when(testAnnotatedMethod.getMethod()).thenReturn(getter);
        when(getter.invoke(instance, (Object[]) null)).thenReturn(new Object());
        JsonParser jsonParser = mock(JsonParser.class);
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(jsonDeserializer.deserialize(jsonParser, mock(DeserializationContext.class), new Object())).thenReturn(new Object());
        Object value = new Object();
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        setterlessProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), instance);
        verify(instance).getClass();
        verify(getter).invoke(instance, (Object[]) null);
        verify(jsonDeserializer).deserialize(jsonParser, mock(DeserializationContext.class), new Object());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Method getter = mock(Method.class);
        when(testAnnotatedMethod.getMethod()).thenReturn(getter);
        when(getter.invoke(instance, (Object[]) null)).thenReturn(new Object());
        JsonParser jsonParser = mock(JsonParser.class);
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(jsonDeserializer.deserialize(jsonParser, mock(DeserializationContext.class), new Object())).thenReturn(new Object());
        Object value = new Object();
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        Object result = setterlessProperty.deserializeSetAndReturn(jsonParser, mock(DeserializationContext.class), instance);
        verify(instance).getClass();
        verify(getter).invoke(instance, (Object[]) null);
        verify(jsonDeserializer).deserialize(jsonParser, mock(DeserializationContext.class), new Object());
        assertSame(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        throw new AssertionError("Should not call set() on setterless property");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        throw new AssertionError("Should not call set() on setterless property");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedSet() throws IOException {
        Object instance = mock(Object.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(testPropertyDefinition, testType, testTypeDeserializer, testContextAnnotations, testAnnotatedMethod);
        setterlessProperty.set(instance, new Object());
    }
}