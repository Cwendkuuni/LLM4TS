
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class InnerClassPropertyTest {
    InnerClassProperty innerClassProperty;
    SettableBeanProperty settableBeanProperty;

    @Before
    public void setup() {
        Constructor<?> constructor = mock(Constructor.class);
        settableBeanProperty = mock(SettableBeanProperty.class);
        innerClassProperty = new InnerClassProperty(settableBeanProperty, constructor);
    }

    @Test
    public void testConstructor() {
        assertEquals(settableBeanProperty, innerClassProperty._delegate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_noAnnotatedConstructor() {
        innerClassProperty = new InnerClassProperty(innerClassProperty, null);
    }

    @Test
    public void testConstructor_withAnnotatedConstructor() {
        AnnotatedConstructor annotatedConstructor = mock(AnnotatedConstructor.class);
        when(annotatedConstructor.getAnnotated()).thenReturn(innerClassProperty._creator);
        innerClassProperty = new InnerClassProperty(innerClassProperty, annotatedConstructor);
        assertEquals(annotatedConstructor, innerClassProperty._annotated);
    }

    @Test
    public void testConstructor_withJsonDeserializer() {
        JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);
        innerClassProperty = new InnerClassProperty(innerClassProperty, jsonDeserializer);
        assertEquals(jsonDeserializer, innerClassProperty._delegate.getValueDeserializer());
    }

    @Test
    public void testConstructor_withPropertyName() {
        PropertyName propertyName = mock(PropertyName.class);
        innerClassProperty = new InnerClassProperty(innerClassProperty, propertyName);
        assertEquals(propertyName, innerClassProperty._delegate.getName());
    }

    @Test
    public void testGetName() {
        innerClassProperty.getName();
        verify(settableBeanProperty).getName();
    }

    @Test
    public void testWithType() {
        JavaType javaType = mock(JavaType.class);
        innerClassProperty.withType(javaType);
        verify(settableBeanProperty).withType(javaType);
    }

    @Test
    public void testWithIgnorals() {
        String[] ignorals = {"a", "b"};
        innerClassProperty.withIgnorals(ignorals);
        verify(settableBeanProperty).withIgnorals(ignorals);
    }

    @Test
    public void testWithMerge() {
        SettableBeanProperty[] properties = {mock(SettableBeanProperty.class)};
        innerClassProperty.withMerge(properties);
        verify(settableBeanProperty).withMerge(properties);
    }

    @Test
    public void testAnnotations() {
        innerClassProperty.annotations();
        verify(settableBeanProperty).annotations();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        innerClassProperty.withValueDeserializer(deserializer);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value");

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(settableBeanProperty).set(bean, "value");
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        innerClassProperty.withValueDeserializer(deserializer);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(deserializer.getNullValue(deserializationContext)).thenReturn("null");

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(settableBeanProperty).set(bean, "null");
    }

    @Test
    public void testDeserializeAndSet_WithType() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        innerClassProperty.withValueDeserializer(deserializer);
        innerClassProperty.withTypeDeserializer(typeDeserializer);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer)).thenReturn("value");

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(settableBeanProperty).set(bean, "value");
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);

        innerClassProperty.withValueDeserializer(deserializer);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value");

        innerClassProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(settableBeanProperty).setAndReturn(instance, "value");
    }

    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        innerClassProperty.set(instance, value);

        verify(settableBeanProperty).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        innerClassProperty.setAndReturn(instance, value);

        verify(settableBeanProperty).setAndReturn(instance, value);
    }

    @Test
    public void testHashCode() {
        innerClassProperty.hashCode();
        verify(settableBeanProperty).hashCode();
    }

    @Test
    public void testEquals() {
        Object o = mock(Object.class);

        innerClassProperty.equals(o);
        verify(settableBeanProperty).equals(o);
    }

    @Test
    public void testAssigned() {
        innerClassProperty.assignIndex(1);
        verify(settableBeanProperty).assignIndex(1);
    }

    @Test
    public void testGetPropertyIndex() {
        innerClassProperty.getPropertyIndex();
        verify(settableBeanProperty).getPropertyIndex();
    }

    @Test
    public void testFindJsonValueMethod() {
        innerClassProperty.findJsonValueAccessor();
        verify(settableBeanProperty).findJsonValueAccessor();
    }

    @Test
    public void testGetNullValue() {
        JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);

        innerClassProperty.withValueDeserializer(jsonDeserializer);
        when(jsonDeserializer.getNullValue(null)).thenReturn(null);

        innerClassProperty.getNullValue(null);

        verify(jsonDeserializer).getNullValue(null);
    }
}
