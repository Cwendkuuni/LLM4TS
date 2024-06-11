package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ReadableObjectId;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(deserializer, objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testConstructorWithDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, deserializer);
        assertEquals(objectIdReader.propertyName, newObjectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), newObjectIdValueProperty.getType());
        assertEquals(metadata, newObjectIdValueProperty.getMetadata());
        assertEquals(deserializer, newObjectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, newObjectIdValueProperty.getObjectIdReader());
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, newName);
        assertEquals(newName, newObjectIdValueProperty.getFullName());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newObjectIdValueProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, newObjectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);

        verify(objectIdReader.generator, times(1)).generateId(eq(instance));
        verify(ctxt, times(1)).findObjectId(any(), eq(objectIdReader.generator), eq(objectIdReader.resolver));
        verify(objectIdReader.idProperty, times(1)).setAndReturn(eq(instance), any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        assertNull(objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance));

        verify(objectIdReader.generator, times(1)).generateId(eq(instance));
        verify(ctxt, times(1)).findObjectId(any(), eq(objectIdReader.generator), eq(objectIdReader.resolver));
        verify(objectIdReader.idProperty, times(1)).setAndReturn(eq(instance), any());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        objectIdValueProperty.setAndReturn(instance, value);
    }
}