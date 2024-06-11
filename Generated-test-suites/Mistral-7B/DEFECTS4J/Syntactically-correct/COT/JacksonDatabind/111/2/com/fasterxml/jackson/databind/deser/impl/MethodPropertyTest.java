package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.TypeDeserializerBase;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * Test class for MethodProperty
 */
public class MethodPropertyTest {

    // Basic test cases for public methods

    @Test
    public void testMethodPropertyConstructor() throws Exception {
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        MethodProperty property = new MethodProperty(null, null, null, null, annotatedMethod);
        assertNotNull(property);
    }

    @Test
    public void testMethodPropertyWithName() throws Exception {
        PropertyName newName = Mockito.mock(PropertyName.class);
        MethodProperty property = new MethodProperty(null, null, null, null, null).withName(newName);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        MethodProperty property = new MethodProperty(null, null, null, null, null).withValueDeserializer(deserializer);
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testMethodPropertyWithNullProvider() throws Exception {
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        MethodProperty property = new MethodProperty(null, null, null, null, null).withNullProvider(nva);
        assertEquals(nva, property.getNullProvider());
    }

    @Test
    public void testMethodPropertyFixAccess() throws Exception {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.fixAccess(null);
        // Cannot directly assert on a private method
    }

    @Test
    public void testMethodPropertyGetAnnotation() throws Exception {
        Annotation annotation = Mockito.mock(Annotation.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        when(method.getAnnotation(Annotation.class)).thenReturn(annotation);
        MethodProperty property = new MethodProperty(null, null, null, null, method);
        assertEquals(annotation, property.getAnnotation(Annotation.class));
    }

    @Test
    public void testMethodPropertyGetMember() throws Exception {
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        MethodProperty property = new MethodProperty(null, null, null, null, method);
        assertEquals(method, property.getMember());
    }

    // Test cases for methods that throw exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullType() throws Exception {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.withType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullPropDef() throws Exception {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.withBeanPropertyDefinition(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullAnnotatedMethod() throws Exception {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        property.withAnnotatedMethod(null);
    }

    @Test(expected = IllegalAccessException.class)
    public void testDeserializeAndSetWithPrivateSetter() throws Exception {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method privateSetter = Mockito.mock(Method.class);
        when(privateSetter.isAccessible()).thenReturn(false);
        when(property._setter).thenReturn(privateSetter);
        property.set(instance, "value");
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetWithPrivateSetter() throws Exception {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method privateSetter = Mockito.mock(Method.class);
        when(privateSetter.isAccessible()).thenReturn(false);
        when(property._setter).thenReturn(privateSetter);
        property.set(instance, "value");
    }

    @Test(expected = InvocationTargetException.class)
    public void testDeserializeAndSetWithExceptionThrowingSetter() throws Exception {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, "value")).thenThrow(new RuntimeException());
        when(property._setter).thenReturn(setter);
        property.deserializeAndSet(null, null, instance);
    }

    // Test cases for deserialization methods

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = new Object();
        Object value = "value";
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, value)).thenReturn(value);
        when(property._setter).thenReturn(setter);
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getText()).thenReturn("value");
        property.deserializeAndSet(parser, null, instance);
        assertEquals(value, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = "value";
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, value)).thenReturn(value);
        when(property._setter).thenReturn(setter);
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getText()).thenReturn("value");
        Object result = property.deserializeSetAndReturn(parser, null, instance);
        assertEquals(value, result);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws Exception {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, null)).thenReturn(null);
        when(property._setter).thenReturn(setter);
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        property.deserializeAndSet(parser, null, instance);
        assertNull(instance);
    }

    @Test
    public void testDeserializeAndSetWithValueDeserializer() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        when(deserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(value);
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, value)).thenReturn(value);
        when(property._setter).thenReturn(setter);
        when(property._valueDeserializer).thenReturn(deserializer);
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getText()).thenReturn("valueText");
        property.deserializeAndSet(parser, null, instance);
        assertEquals(value, instance);
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(deserializer.deserializeWithType(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class), Mockito.any(TypeDeserializer.class))).thenReturn(value);
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, value)).thenReturn(value);
        when(property._setter).thenReturn(setter);
        when(property._valueDeserializer).thenReturn(deserializer);
        when(property._valueTypeDeserializer).thenReturn(typeDeserializer);
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getText()).thenReturn("valueText");
        property.deserializeAndSet(parser, null, instance);
        assertEquals(value, instance);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = "value";
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, value)).thenReturn(value);
        when(property._setter).thenReturn(setter);
        Object result = property.setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testSetAndReturnWithNullValue() throws Exception {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, null)).thenReturn(null);
        when(property._setter).thenReturn(setter);
        Object result = property.setAndReturn(instance, null);
        assertNull(result);
    }

    @Test
    public void testSetAndReturnWithValueDeserializer() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        when(deserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(value);
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, value)).thenReturn(value);
        when(property._setter).thenReturn(setter);
        when(property._valueDeserializer).thenReturn(deserializer);
        Object result = property.setAndReturn(instance, null);
        assertEquals(value, result);
    }

    @Test
    public void testSetAndReturnWithTypeDeserializer() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(deserializer.deserializeWithType(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class), Mockito.any(TypeDeserializer.class))).thenReturn(value);
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Method setter = Mockito.mock(Method.class);
        when(setter.invoke(instance, value)).thenReturn(value);
        when(property._setter).thenReturn(setter);
        when(property._valueDeserializer).thenReturn(deserializer);
        when(property._valueTypeDeserializer).thenReturn(typeDeserializer);
        Object result = property.setAndReturn(instance, null);
        assertEquals(value, result);
    }
}