package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class InnerClassPropertyTest {

    // Create test doubles
    SettableBeanProperty delegate = Mockito.mock(SettableBeanProperty.class);
    Constructor<?> ctor = Mockito.mock(Constructor.class);
    AnnotatedConstructor ann = Mockito.mock(AnnotatedConstructor.class);
    JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
    PropertyName newName = Mockito.mock(PropertyName.class);
    InnerClassProperty src = new InnerClassProperty(delegate, ctor);

    @Test
    public void testInnerClassProperty() {
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);
        assertNotNull(property);
    }

    @Test
    public void testInnerClassPropertyWithAnnotatedConstructor() {
        InnerClassProperty property = new InnerClassProperty(src, ann);
        assertNotNull(property);
    }

    @Test
    public void testInnerClassPropertyWithJsonDeserializer() {
        InnerClassProperty property = new InnerClassProperty(src, deser);
        assertNotNull(property);
    }

    @Test
    public void testInnerClassPropertyWithPropertyName() {
        InnerClassProperty property = new InnerClassProperty(src, newName);
        assertNotNull(property);
    }

    @Test
    public void testWithName() {
        InnerClassProperty property = src.withName(newName);
        assertNotNull(property);
    }

    @Test
    public void testWithValueDeserializer() {
        InnerClassProperty property = src.withValueDeserializer(deser);
        assertNotNull(property);
    }

    @Test
    public void testAssignIndex() {
        int index = 1;
        src.assignIndex(index);
        assertEquals(index, src.getPropertyIndex());
    }

    @Test
    public void testGetAnnotation() {
        Class<?> acls = null;
        assertNull(src.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        assertNull(src.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = new Object();
        src.deserializeAndSet(jp, ctxt, bean);
        // Verify that set method is called on delegate
        Mockito.verify(delegate, Mockito.times(1)).set(Mockito.any(), Mockito.any());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithException() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object bean = new Object();
        // Throw exception when calling newInstance method
        Mockito.when(ctor.newInstance(bean)).thenThrow(new Exception());
        src.deserializeAndSet(jp, ctxt, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser jp = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        src.deserializeSetAndReturn(jp, ctxt, instance);
        // Verify that setAndReturn method is called on delegate
        Mockito.verify(delegate, Mockito.times(1)).setAndReturn(Mockito.any(), Mockito.any());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        src.setAndReturn(instance, value);
        // Verify that setAndReturn method is called on delegate
        Mockito.verify(delegate, Mockito.times(1)).setAndReturn(Mockito.any(), Mockito.any());
    }

}