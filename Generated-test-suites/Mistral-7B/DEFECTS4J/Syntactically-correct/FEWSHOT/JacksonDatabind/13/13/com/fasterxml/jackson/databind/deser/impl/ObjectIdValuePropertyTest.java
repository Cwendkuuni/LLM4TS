package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader _objectIdReader;
    private ObjectIdValueProperty _objectIdValueProperty;

    @Before
    public void setUp() {
        _objectIdReader = mock(ObjectIdReader.class);
        _objectIdValueProperty = new ObjectIdValueProperty(_objectIdReader, new PropertyMetadata()) {
            // override to make it testable
            @Override
            public SettableBeanProperty idProperty;
        };
    }

    @Test
    public void testGetAnnotation() {
        assertNull(_objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(_objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "123";
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(_objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(_objectIdReader.resolver).thenReturn(new ObjectIdResolver());

        when(_objectIdValueProperty.getIdType()).thenReturn(Integer.TYPE);
        when(_objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        _objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);

        ObjectIdGenerator generator = (ObjectIdGenerator) _objectIdReader.generator;
        generator.assertNextIdSet(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = "123";
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(_objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(_objectIdReader.resolver).thenReturn(new ObjectIdResolver());

        when(_objectIdValueProperty.getIdType()).thenReturn(Integer.TYPE);
        when(_objectIdValueProperty.idProperty).thenReturn(mock(SettableBeanProperty.class));

        Object returnValue = _objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);

        ObjectIdGenerator generator = (ObjectIdGenerator) _objectIdReader.generator;
        generator.assertNextIdSet(instance, id);
        assertEquals(instance, returnValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        _objectIdValueProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);

        when(_objectIdValueProperty.getIdType()).thenReturn(Integer.TYPE);
        when(_objectIdValueProperty.idProperty).thenReturn(idProp);

        Object returnValue = _objectIdValueProperty.setAndReturn(instance, value);

        verify(idProp).setAndReturn(eq(instance), eq(value));
        assertEquals(instance, returnValue);
    }
}