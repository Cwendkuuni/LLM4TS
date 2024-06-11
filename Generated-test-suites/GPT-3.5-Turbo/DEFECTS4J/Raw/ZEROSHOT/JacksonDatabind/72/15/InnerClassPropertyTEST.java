package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

    private InnerClassProperty innerClassProperty;

    @Before
    public void setup() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor ctor = new Constructor();
        innerClassProperty = new InnerClassProperty(delegate, ctor);
    }

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor ctor = new Constructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);

        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();

        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // assert implementation here
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        // assert implementation here
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        innerClassProperty.set(instance, value);
        // assert implementation here
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = innerClassProperty.setAndReturn(instance, value);
        // assert implementation here
    }

    @Test
    public void testGetAnnotation() {
        Class acls = Class.class;

        innerClassProperty.getAnnotation(acls);
        // assert implementation here
    }

    @Test
    public void testGetMember() {
        innerClassProperty.getMember();
        // assert implementation here
    }

    @Test
    public void testWithPropertyName() {
        PropertyName newName = new PropertyName();

        innerClassProperty.withPropertyName(newName);
        // assert implementation here
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer deser = new JsonDeserializer();

        innerClassProperty.withValueDeserializer(deser);
        // assert implementation here
    }

    @Test
    public void testAssignIndex() {
        int index = 0;

        innerClassProperty.assignIndex(index);
        // assert implementation here
    }

    @Test
    public void testGetPropertyIndex() {
        innerClassProperty.getPropertyIndex();
        // assert implementation here
    }

    @Test
    public void testReadResolve() {
        Object expectedResult = new InnerClassProperty(innerClassProperty, innerClassProperty._annotated);
        Object result = innerClassProperty.readResolve();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testWriteReplace() {
        Object expectedResult = new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, innerClassProperty._creator, null, null));
        Object result = innerClassProperty.writeReplace();

        assertEquals(expectedResult, result);
    }
}