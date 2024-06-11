package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstructor() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        AnnotatedConstructor constructor = Mockito.mock(AnnotatedConstructor.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testInnerClassPropertyTransientConstructor() {
        SettableBeanProperty src = Mockito.mock(SettableBeanProperty.class);
        AnnotatedConstructor ann = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);
        assertEquals(src, innerClassProperty);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(ann, innerClassProperty._annotated);
    }

    @Test
    public void testInnerClassPropertyJsonDeserializer() throws InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        SettableBeanProperty src = Mockito.mock(SettableBeanProperty.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
        assertEquals(src, innerClassProperty);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(deser, innerClassProperty._valueDeserializer);
    }

    @Test
    public void testInnerClassPropertyPropertyName() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        SettableBeanProperty src = Mockito.mock(SettableBeanProperty.class);
        PropertyName newName = Mockito.mock(PropertyName.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
        assertEquals(src, innerClassProperty);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(newName, innerClassProperty._name);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object bean = Mockito.mock(Object.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        AnnotatedConstructor annotated = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object value = Mockito.mock(Object.class);
        when(creator.newInstance(bean)).thenReturn(value);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertEquals(value, innerClassProperty.getBean().getProperty(innerClassProperty.getName()));
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        Object bean = Mockito.mock(Object.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        AnnotatedConstructor annotated = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.getBean().getProperty(innerClassProperty.getName()));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetMissingConstructor() throws IOException {
        Object bean = Mockito.mock(Object.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        AnnotatedConstructor annotated = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        when(creator.getDeclaringClass().getName()).thenReturn("NonExistentClass");

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object bean = Mockito.mock(Object.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        AnnotatedConstructor annotated = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object value = Mockito.mock(Object.class);
        when(creator.newInstance(bean)).thenReturn(value);

        assertEquals(value, innerClassProperty.deserializeSetAndReturn(jp, ctxt, bean));
    }

    @Test
    public void testSet() throws IOException {
        Object bean = Mockito.mock(Object.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        Object value = Mockito.mock(Object.class);

        innerClassProperty.set(bean, value);
        assertEquals(value, innerClassProperty.getBean().getProperty(innerClassProperty.getName()));
    }

    @Test
    public void testSetNull() throws IOException {
        Object bean = Mockito.mock(Object.class);
        SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
        Constructor<?> creator = Mockito.mock(Constructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);

        innerClassProperty.set(bean, null);
        assertNull(innerClassProperty.getBean().getProperty(innerClassProperty.getName()));
    }

    @Test
    public void testReadResolve() {
        SettableBeanProperty src = Mockito.mock(SettableBeanProperty.class);
        AnnotatedConstructor ann = Mockito.mock(AnnotatedConstructor.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);

        assertEquals(innerClassProperty, innerClassProperty.readResolve());
    }

    @Test
    public void testWriteReplace() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        SettableBeanProperty src = Mockito.mock(SettableBeanProperty.class);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, src._creator, null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);

        assertEquals(innerClassProperty, innerClassProperty.writeReplace());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteReplaceMissingConstructor() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        SettableBeanProperty src = Mockito.mock(SettableBeanProperty.class);
        AnnotatedConstructor ann = new AnnotatedConstructor(null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);

        innerClassProperty.writeReplace();
    }
}