package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty(new Object(), new Object().getClass().getDeclaredConstructor());
        Constructor<?> constructor = new Object().getClass().getDeclaredConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertNotNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
    }

    @Test
    public void testInnerClassPropertyAnnotatedConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty(new Object(), new Object().getClass().getDeclaredConstructor());
        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor(null, new Object().getClass().getDeclaredConstructor(), null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor);
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertNotNull(innerClassProperty._annotated);
    }

    @Test
    public void testInnerClassPropertyWithDeserializer() {
        SettableBeanProperty delegate = new SettableBeanProperty(new Object(), new Object().getClass().getDeclaredConstructor());
        JsonDeserializer<?> deserializer = new JsonDeserializer<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, deserializer);
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertNotNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
        assertNotNull(innerClassProperty._valueDeserializer);
    }

    @Test
    public void testInnerClassPropertyWithName() {
        SettableBeanProperty delegate = new SettableBeanProperty(new Object(), new Object().getClass().getDeclaredConstructor());
        PropertyName newName = new PropertyName("testProperty");
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, newName);
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertNotNull(innerClassProperty._creator);
        assertNull(innerClassProperty._annotated);
        assertEquals(newName, innerClassProperty.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInnerClassPropertyNullAnnotatedConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty(new Object(), new Object().getClass().getDeclaredConstructor());
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetNullValue() throws IOException {
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(new SettableBeanProperty(bean, new Object().getClass().getDeclaredConstructor()), null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetMissingConstructor() throws IOException {
        Object bean = new Object();
        SettableBeanProperty delegate = new SettableBeanProperty(bean, new Object().getClass().getDeclaredConstructor());
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetFailedInstantiation() throws IOException {
        Object bean = new Object();
        SettableBeanProperty delegate = new SettableBeanProperty(bean, new Object().getClass().getDeclaredConstructor());
        Constructor<?> constructor = new Object().getClass()
                .getDeclaredConstructor(Object.class); // constructor with a parameter
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }

    @Test
    public void testReadResolve() throws IOException {
        Object bean = new Object();
        SettableBeanProperty delegate = new SettableBeanProperty(bean, new Object().getClass().getDeclaredConstructor());
        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor(null, new Object().getClass().getDeclaredConstructor(), null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor);
        Object readResolve = innerClassProperty.readResolve();
        assertNotNull(readResolve);
        assertEquals(innerClassProperty.getClass(), readResolve.getClass());
    }

    @Test
    public void testWriteReplace() throws IOException {
        Object bean = new Object();
        SettableBeanProperty delegate = new SettableBeanProperty(bean, new Object().getClass().getDeclaredConstructor());
        AnnotatedConstructor annotatedConstructor = new AnnotatedConstructor(null, new Object().getClass().getDeclaredConstructor(), null, null);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor);
        Object writeReplace = innerClassProperty.writeReplace();
        assertNotNull(writeReplace);
        assertEquals(innerClassProperty.getClass(), writeReplace.getClass());
    }
}