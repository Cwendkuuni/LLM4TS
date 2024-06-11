package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstruction() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithName() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        PropertyName newName = PropertyName.construct("newName");

        MethodProperty methodProperty = new MethodProperty(methodProperty, newName);
        assertEquals(methodProperty, methodProperty.getOriginalProperty());
        assertEquals(newName, methodProperty.getName());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, valueDeserializer);
        assertEquals(methodProperty, methodProperty.getOriginalProperty());
        assertEquals(valueDeserializer, methodProperty.getValueDeserializer());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithNullProvider() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, nva);
        assertEquals(methodProperty, methodProperty.getOriginalProperty());
        assertEquals(nva, methodProperty.getNullProvider());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithDeserializerAndNullProvider() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, valueDeserializer, nva);
        assertEquals(methodProperty, methodProperty.getOriginalProperty());
        assertEquals(valueDeserializer, methodProperty.getValueDeserializer());
        assertEquals(nva, methodProperty.getNullProvider());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    @Test
    public void testMethodPropertyWithSkipNulls() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method, true);
        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertTrue(methodProperty.isSkipNulls());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullBeanPropertyDefinition() {
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        new MethodProperty(null, type, typeDeser, contextAnnotations, method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullType() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        new MethodProperty(propDef, null, typeDeser, contextAnnotations, method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullTypeDeserializer() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        new MethodProperty(propDef, type, null, contextAnnotations, method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullContextAnnotations() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        new MethodProperty(propDef, type, typeDeser, null, method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullAnnotatedMethod() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);

        new MethodProperty(propDef, type, typeDeser, contextAnnotations, null);
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object annotation = new Object();
        when(method.getAnnotation(annotation.getClass())).thenReturn(annotation);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(annotation, methodProperty.getAnnotation(annotation.getClass()));
    }

    @Test
    public void testGetMember() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(method, methodProperty.getMember());
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        method.fixAccess(true);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        methodProperty.fixAccess(true);
        verify(method).fixAccess(true);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object value = new Object();

        when(method.getAnnotated()).thenReturn(method);
        when(method.getDeclaringClass()).thenReturn(instance.getClass());
        when(method.getName()).thenReturn("setX");

        when(typeDeser.deserialize(eq(instance), any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(value);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        methodProperty.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertEquals(value, instance.getClass().getMethod("setX").invoke(instance, value));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object value = new Object();
        Object result = new Object();

        when(method.getAnnotated()).thenReturn(method);
        when(method.getDeclaringClass()).thenReturn(instance.getClass());
        when(method.getName()).thenReturn("setX");
        when(method.getReturnType()).thenReturn(result.getClass());

        when(typeDeser.deserialize(eq(instance), any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(value);

        when(method.invoke(instance, anyObject())).thenReturn(result);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object returnValue = methodProperty.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertEquals(result, returnValue);
    }

    @Test
    public void testSet() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object value = new Object();

        when(method.getAnnotated()).thenReturn(method);
        when(method.getDeclaringClass()).thenReturn(instance.getClass());
        when(method.getName()).thenReturn("setX");

        methodProperty.set(instance, value);
        assertEquals(value, instance.getClass().getMethod("setX").invoke(instance, value));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object value = new Object();
        Object result = new Object();

        when(method.getAnnotated()).thenReturn(method);
        when(method.getDeclaringClass()).thenReturn(instance.getClass());
        when(method.getName()).thenReturn("setX");
        when(method.getReturnType()).thenReturn(result.getClass());

        when(method.invoke(instance, anyObject())).thenReturn(result);

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
        Object returnValue = methodProperty.setAndReturn(instance, value);
        assertEquals(result, returnValue);
    }

    @Test
    public void testReadResolve() throws ObjectStreamException {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(methodProperty, method);

        Object resolvedObject = methodProperty.readResolve();
        assertEquals(methodProperty, resolvedObject);
    }

}