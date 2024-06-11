package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import java.io.*;
import java.lang.reflect.*;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstructor() {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        AnnotatedConstructor annotated = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, creator);
        InnerClassProperty propertyWithAnnotated = new InnerClassProperty(property, annotated);
        assertEquals(delegate, property._delegate);
        assertEquals(creator, property._creator);
        assertEquals(annotated, propertyWithAnnotated._annotated);
    }

    @Test
    public void testInnerClassPropertyWithValueDeserializer() {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, deserializer);
        assertEquals(delegate, property._delegate);
        assertEquals(deserializer, property._valueDeserializer);
    }

    @Test
    public void testInnerClassPropertyWithName() {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        PropertyName newName = PropertyName.construct("testProperty");
        InnerClassProperty property = new InnerClassProperty(delegate, newName);
        assertEquals(delegate, property._delegate);
        assertEquals(newName, property._name);
    }

    @Test
    public void testAssignIndex() throws IllegalAccessException, InstantiationException {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        InnerClassProperty property = new InnerClassProperty(delegate, Mockito.mock(Constructor.class));
        int index = 0;
        property.assignIndex(index);
        assertEquals(index, delegate.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        InnerClassProperty property = new InnerClassProperty(delegate, Mockito.mock(Constructor.class));
        assertEquals(delegate.getPropertyIndex(), property.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        InnerClassProperty property = new InnerClassProperty(delegate, Mockito.mock(Constructor.class));
        Class<?> annotationType = Mockito.mock(Class.class);
        Annotation annotation = Mockito.mock(Annotation.class);
        when(delegate.getAnnotation(annotationType)).thenReturn(annotation);
        assertEquals(annotation, property.getAnnotation(annotationType));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        InnerClassProperty property = new InnerClassProperty(delegate, Mockito.mock(Constructor.class));
        assertEquals(delegate.getMember(), property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, creator);
        Object bean = Mockito.mock(Object.class);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object value = Mockito.mock(Object.class);
        when(creator.newInstance(bean)).thenReturn(value);
        when(valueDeserializer.deserialize(jp, ctxt, value)).thenReturn(value);
        when(delegate.set(bean, value)).thenAnswer(invocation -> value);
        when(delegate.getMember()).thenReturn(Mockito.mock(AnnotatedMember.class));
        property.deserializeAndSet(jp, ctxt, bean);
        verify(delegate).set(bean, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty property = new InnerClassProperty(delegate, creator, valueDeserializer);
        Object instance = Mockito.mock(Object.class);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object value = Mockito.mock(Object.class);
        when(creator.newInstance(instance)).thenReturn(value);
        when(valueDeserializer.deserialize(jp, ctxt, value)).thenReturn(value);
        when(delegate.setAndReturn(instance, value)).thenReturn(value);
        Object result = property.deserializeSetAndReturn(jp, ctxt, instance);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Object bean = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);
        InnerClassProperty property = new InnerClassProperty(delegate, Mockito.mock(Constructor.class));
        property.set(bean, value);
        verify(delegate).set(bean, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Object bean = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);
        InnerClassProperty property = new InnerClassProperty(delegate, Mockito.mock(Constructor.class));
        Object result = property.setAndReturn(bean, value);
        assertEquals(value, result);
    }

    @Test
    public void testReadResolve() throws InstantiationException, IllegalAccessException, IOException {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        AnnotatedConstructor annotated = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, annotated);
        Object result = property.readResolve();
        assertEquals(delegate, result._delegate);
        assertEquals(annotated, result._annotated);
    }

    @Test
    public void testWriteReplace() throws InstantiationException, IllegalAccessException, IOException {
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        InnerClassProperty property = new InnerClassProperty(delegate, creator);
        Object result = property.writeReplace();
        assertEquals(delegate, result._delegate);
        assertEquals(creator, result._creator);
    }
}