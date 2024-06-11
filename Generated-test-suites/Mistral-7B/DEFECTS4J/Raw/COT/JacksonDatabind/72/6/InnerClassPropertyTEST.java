package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testConstructorWithDelegateAndCreator() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> creator = mockConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, creator);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(creator, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithInnerClassProperty() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        AnnotatedConstructor ann = mockAnnotatedConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertNull(innerClassProperty._creator);
        assertEquals(ann, innerClassProperty._annotated);
    }

    @Test
    public void testConstructorWithJsonDeserializer() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(deser, innerClassProperty._valueDeserializer);
        assertNull(innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithPropertyName() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        PropertyName newName = mock(PropertyName.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(newName, innerClassProperty._name);
        assertNull(innerClassProperty._creator);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object bean = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(mock(SettableBeanProperty.class), mockConstructor());
        JsonParser jp = mockJsonParser();
        DeserializationContext ctxt = mockContext();
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        // Verify that the _delegate's deserializeAndSet method was called
        verify(innerClassProperty._delegate).deserializeAndSet(jp, ctxt, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        InnerClassProperty innerClassProperty = new InnerClassProperty(mock(SettableBeanProperty.class), mockConstructor());
        JsonParser jp = mockJsonParser();
        DeserializationContext ctxt = mockContext();
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        // Verify that the _delegate's set method was called
        verify(innerClassProperty._delegate).set(instance, result);
        assertEquals(result, innerClassProperty.get(instance));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingConstructor() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(mock(InnerClassProperty.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingAnnotatedConstructor() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(mock(InnerClassProperty.class), mock(JsonDeserializer.class));
    }

    @Test
    public void testReadResolve() throws IOException {
        InnerClassProperty innerClassProperty = mock(InnerClassProperty.class);
        AnnotatedConstructor ann = mockAnnotatedConstructor();
        InnerClassProperty innerClassPropertyResolved = innerClassProperty.readResolve();
        assertEquals(innerClassProperty._delegate, innerClassPropertyResolved._delegate);
        assertEquals(ann, innerClassPropertyResolved._annotated);
    }

    @Test
    public void testWriteReplace() throws IOException {
        InnerClassProperty innerClassProperty = mock(InnerClassProperty.class);
        AnnotatedConstructor constructor = mockAnnotatedConstructor();
        Object result = innerClassProperty.writeReplace();
        assertEquals(innerClassProperty, result);
        assertEquals(constructor, result._annotated);
    }

    // Helper methods

    private Constructor<?> mockConstructor() {
        try {
            return Mockito.mock(Constructor.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AnnotatedConstructor mockAnnotatedConstructor() {
        try {
            return Mockito.mock(AnnotatedConstructor.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JsonParser mockJsonParser() {
        try {
            return Mockito.mock(JsonParser.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DeserializationContext mockContext() {
        try {
            return Mockito.mock(DeserializationContext.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}