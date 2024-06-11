package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class InnerClassPropertyTest {
    @Test
    public void testInnerClassPropertyConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<?>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }

    @Test
    public void testInnerClassPropertyConstructorWithAnnotatedConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        AnnotatedConstructor ann = new AnnotatedConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ann);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ann, innerClassProperty._annotated);
        assertEquals(ann.getAnnotated(), innerClassProperty._creator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInnerClassPropertyConstructorWithAnnotatedConstructorMissingConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        AnnotatedConstructor ann = new AnnotatedConstructor();
        ann.setAnnotated(null);
        new InnerClassProperty(delegate, ann);
    }

    @Test
    public void testInnerClassPropertyConstructorWithValueDeserializer() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, deser);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(deser, innerClassProperty.getValueDeserializer());
    }

    @Test
    public void testInnerClassPropertyConstructorWithNewName() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        PropertyName newName = new PropertyName();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, newName);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(newName, innerClassProperty.getFullName());
    }

    @Test
    public void testInnerClassPropertyWithIndex() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty.assignIndex(1);
        assertEquals(1, innerClassProperty.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        Class<?> acls = Annotation.class;
        assertNull(innerClassProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        assertNull(innerClassProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();

        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);

        //Assert statements
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);

        //Assert statements
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        innerClassProperty.set(instance, value);

        //Assert statements
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty delegate = new SettableBeanProperty();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        Object result = innerClassProperty.setAndReturn(instance, value);

        //Assert statements
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        InnerClassProperty resolvedProperty = innerClassProperty.readResolve();
        assertEquals(innerClassProperty, resolvedProperty);
    }

    @Test
    public void testWriteReplaceWithAnnotatedConstructor() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, new AnnotatedConstructor());
        Object replacedProperty = innerClassProperty.writeReplace();
        assertEquals(innerClassProperty, replacedProperty);
    }

    @Test
    public void testWriteReplaceNoAnnotatedConstructor() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        Object replacedProperty = innerClassProperty.writeReplace();
        assertEquals(innerClassProperty, replacedProperty);
    }
}