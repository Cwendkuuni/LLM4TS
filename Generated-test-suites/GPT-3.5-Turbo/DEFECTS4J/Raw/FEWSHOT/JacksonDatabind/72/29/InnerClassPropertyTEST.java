package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }

    @Test
    public void testJDKSerializationConstructor() {
        InnerClassProperty src = new InnerClassProperty();
        AnnotatedConstructor ann = new AnnotatedConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);
        assertNotNull(innerClassProperty);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(ann, innerClassProperty._annotated);
        assertEquals(ann.getAnnotated(), innerClassProperty._creator);
    }

    @Test
    public void testWithDeserializerConstructor() {
        InnerClassProperty src = new InnerClassProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
        assertNotNull(innerClassProperty);
        assertEquals(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }

    @Test
    public void testWithNameConstructor() {
        InnerClassProperty src = new InnerClassProperty();
        PropertyName newName = new PropertyName();
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
        assertNotNull(innerClassProperty);
        assertEquals(src._delegate.withName(newName), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }

    @Test
    public void testGetAnnotation() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        innerClassProperty._delegate = new SettableBeanProperty();
        Class<Annotation> acls = Annotation.class;
        assertEquals(innerClassProperty._delegate.getAnnotation(acls), innerClassProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        innerClassProperty._delegate = new SettableBeanProperty();
        assertEquals(innerClassProperty._delegate.getMember(), innerClassProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        assertNotNull(innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();
        assertNotNull(innerClassProperty.setAndReturn(instance, value));
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        assertNotNull(innerClassProperty.readResolve());
    }

    @Test
    public void testWriteReplace() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        assertNotNull(innerClassProperty.writeReplace());
    }

}