package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;

public class InnerClassPropertyTest {

    InnerClassProperty innerClassProperty;
    Constructor<?> constructor;
    SettableBeanProperty delegate;
    JsonDeserializer<?> deser;
    PropertyName newName;

    @Before
    public void setUp() throws NoSuchMethodException, SecurityException {
        delegate = new SettableBeanProperty();
        constructor = InnerClassProperty.class.getConstructor(SettableBeanProperty.class, Constructor.class);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
        
        deser = new JsonDeserializer<>();
        innerClassProperty = new InnerClassProperty(innerClassProperty, deser);
        
        newName = new PropertyName();
        innerClassProperty = new InnerClassProperty(innerClassProperty, newName);
    }

    @Test
    public void testConstructor() {
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testWithDeserializer() {
        assertEquals(delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testWithPropertyName() {
        assertEquals(delegate.withName(newName), innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testAssignIndex() {
        innerClassProperty.assignIndex(1);
        assertEquals(1, innerClassProperty._delegate.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndex() {
        delegate.setPropertyIndex(1);
        assertEquals(1, innerClassProperty.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        delegate.setAnnotation(new Annotation());
        assertEquals(delegate.getAnnotation(Annotation.class), innerClassProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        delegate.setMember(new AnnotatedMember());
        assertEquals(delegate.getMember(), innerClassProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.setAndReturn(instance, value);
    }

    @Test
    public void testReadResolve() {
        innerClassProperty._annotated = new AnnotatedConstructor();
        assertEquals(innerClassProperty, innerClassProperty.readResolve());
    }

    @Test
    public void testWriteReplace() {
        innerClassProperty._annotated = null;
        assertEquals(innerClassProperty, innerClassProperty.writeReplace());
    }
}