package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(deserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithDeser() {
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        assertEquals(property.getName(), newProperty.getName());
        assertEquals(property.getType(), newProperty.getType());
        assertEquals(property.getMetadata(), newProperty.getMetadata());
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(jp, ctxt, instance);

        verify(objectIdReader.generator, times(1)).bindItem(instance);
        verify(objectIdReader.resolver, times(1)).bindItem(instance);
        verify(objectIdReader.idProperty, times(1)).setAndReturn(instance, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNullIdProperty() throws IOException {
        Object instance = new Object();
        property.setAndReturn(instance, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProperty);
        Object instance = new Object();
        Object value = new Object();

        property.setAndReturn(instance, value);

        verify(idProperty, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testGetAnnotation() {
        Class<?> acls = Object.class;
        assertNull(property.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }
}